package com.mingcloud.data.config.db;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DruidDBConfig类被@Configuration标注，用作配置信息；
 * DataSource对象被@Bean声明，为Spring容器所管理，
 *
 * @Primary表示这里定义的DataSource将覆盖其他来源的DataSource。
 * @auther simon
 * @date 2020/3/14
 * @info
 */
@Configuration
@EnableTransactionManagement
//@Import({MybatisInterceptor.class})
public class DruidDBConfig {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //@Autowired
    //private MybatisInterceptor mybatisInterceptor;

    // adi数据库连接信息
    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    // 连接池连接信息
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.maxWait}")
    private int maxWait;
    @Value("${spring.datasource.filters}")
    private String filters;
    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;


    @Bean // 声明其为Bean实例
    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
    @Qualifier("adiDataSource")
    public DataSource dataSource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        // 基础连接信息
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        // 连接池连接信息
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setPoolPreparedStatements(true); //是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
        datasource.setMaxPoolPreparedStatementPerConnectionSize(50);
        datasource.setConnectionProperties("oracle.net.CONNECT_TIMEOUT=6000;oracle.jdbc.ReadTimeout=60000");//对于耗时长的查询sql，会受限于ReadTimeout的控制，单位毫秒
        datasource.setTestOnBorrow(true); //申请连接时执行validationQuery检测连接是否有效，这里建议配置为TRUE，防止取到的连接不可用
        datasource.setTestWhileIdle(true);//建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
        String validationQuery = "select 1 from dual";
        datasource.setValidationQuery(validationQuery); //用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
        //datasource.setFilters("stat");//属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall
        datasource.setTimeBetweenEvictionRunsMillis(60000); //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.setMinEvictableIdleTimeMillis(180000); //配置一个连接在池中最小生存的时间，单位是毫秒，这里配置为3分钟180000
        datasource.setKeepAlive(true); //打开druid.keepAlive之后，当连接池空闲时，池中的minIdle数量以内的连接，空闲时间超过minEvictableIdleTimeMillis，则会执行keepAlive操作，即执行druid.validationQuery指定的查询SQL，一般为select * from dual，只要minEvictableIdleTimeMillis设置的小于防火墙切断连接时间，就可以保证当连接空闲时自动做保活检测，不会被防火墙切断
        datasource.setRemoveAbandoned(true); //是否移除泄露的连接/超过时间限制是否回收。
        datasource.setRemoveAbandonedTimeout(3600); //泄露连接的定义时间(要超过最大事务的处理时间)；单位为秒。这里配置为1小时
        datasource.setLogAbandoned(true); ////移除泄露连接发生是是否记录日志
        try {
            List<Filter> proxyFilters = new ArrayList<Filter>();
            WallFilter statFilter = new WallFilter();
            WallConfig config = new WallConfig();
            config.setMultiStatementAllow(true); // 批量操作
            statFilter.setConfig(config);
            proxyFilters.add(statFilter);
            datasource.setProxyFilters(proxyFilters);
            datasource.setFilters(filters);
        } catch (SQLException e) {
            logger.error("druid configuration initialization filter", e);
        }
        return datasource;
    }

    @Bean(name = "dynamicDataSource")
    @Qualifier("dynamicDataSource")
    public DynamicDataSource dynamicDataSource() throws SQLException {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDebug(false);
        //配置缺省的数据源
        dynamicDataSource.setDefaultTargetDataSource(dataSource());
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("adiDataSource", dataSource());
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //sqlSessionFactoryBean.setPlugins(new Interceptor[]{mybatisInterceptor});
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        // 设置mybatis的主配置文件
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocations));
        //解决驼峰命名失效
        sqlSessionFactoryBean.setConfiguration(configuration());
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    /**
     * 读取驼峰命名设置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration")
    public org.apache.ibatis.session.Configuration configuration() {
        return new org.apache.ibatis.session.Configuration();
    }
}
