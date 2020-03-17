package com.mingcloud.data.mapper;

import com.mingcloud.data.DataUtilsApplicationTests;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.mapper.slave2.Slave2TableMapper;
import com.mingcloud.data.service.DataSourceService;
import com.mingcloud.data.service.DataService;
import org.junit.Test;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Slave2TableMapperTest extends DataUtilsApplicationTests {
    @Resource
    private Slave2TableMapper slave2TableMapper;

    @Resource
    private DataSourceService dataSourceService;

    @Resource
    private DataService dataService;

    private DataSourceEntity slave2DataSource;

    private String recordSqlTable;

    @Test
    public void testRunSql() throws SQLException {
        //slave2DataSource = dataSourceService.getSlave2DataSource();
        List<String> list = new ArrayList<>();
        String sql = " insert into `geely2.0`.t_user ( id , `name` , age )  select id , `name` , age from `geely1.0`. t_user where `geely1.0`.t_user.`name` = 'aa' ";
        list.add(sql);
        DataBaseEntity entity = new DataBaseEntity();
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        entity.setTableName(recordSqlTable);
        List<String> sqlsList = dataService.getSqls(entity);
        slave2TableMapper.runSqls(list);
    }
}
