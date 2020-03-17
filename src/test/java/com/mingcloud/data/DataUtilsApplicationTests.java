package com.mingcloud.data;

import com.mingcloud.data.config.db.DBContextHolder;
import com.mingcloud.data.config.db.DynamicDataSource;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.User;
import com.mingcloud.data.mapper.DataSourceMapper;
import com.mingcloud.data.service.impl.DataSourceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataUtilsApplicationTests {
    @Resource
    private DataSourceMapper dataSourceMapper;

    /*@Resource
    private UserMapper userMapper;
*/
    @Autowired
    private DynamicDataSource dynamicDataSource;
    @Autowired
    private DataSourceServiceImpl dataSourceServiceImpl;

    /**
     * 测试自动切换
     */
    @Test
    public void contextLoads() {
        /*//取出数据库中的第一条数据源配置信息
        List<DataSourceEntity> list=dataSourceMapper.select();
        DataSourceEntity dataSource = list.get(0);
        List<User> list1 = dataSourceServiceImpl.getUser(dataSource);
        list1.forEach(user -> System.out.println(user.getName()));
        *//**
         * 第二次查询不用再创建数据源，直接使用
         *//*
        List<User> list2 = dataSourceServiceImpl.getUser(dataSource);
        list2.forEach(user -> System.out.println(user.getName()));*/
    }

    /**
     * 手动改变数据源
     */
    @Test
    public void get(){
        /*
        List<DataSourceEntity> list=dataSourceMapper.select();
        DataSourceEntity dataSource = list.get(0);
        //创建数据源
        try {
            dynamicDataSource.createDataSourceWithCheck(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //切换数据源
        DBContextHolder.setDataSource(dataSource.getId());
        List<User> list1=userMapper.select();
        list1.forEach(dataSource1 -> System.out.println(dataSource1.getName()));*/

    }
    @Test
    public void testString(){
        String sql = " where aaa = 10 ".trim();

        int where = sql.indexOf("where");
        boolean bl = sql.startsWith("where");
        System.out.println("bl:"+bl);
    }
}
