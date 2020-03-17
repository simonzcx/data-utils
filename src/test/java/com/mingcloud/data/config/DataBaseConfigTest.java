package com.mingcloud.data.config;

import com.mingcloud.data.DataUtilsApplicationTests;
import com.mingcloud.data.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

@Slf4j
public class DataBaseConfigTest extends DataUtilsApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseConfigTest.class);
    @Resource
    private DataBaseConfig dataBaseConfig;

    @Resource
    private DataSourceService dataSourceService;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Test
    public void test(){
        System.out.println("masterDataSource:"+dataSourceService.getSlave1DataSource());
        System.out.println("slaveDataSource:"+dataSourceService.getSlave2DataSource());
        System.out.println("masterDataBase:"+dataSourceService.getSlave2DataBase());
        System.out.println("slaveDataBase:"+dataSourceService.getSlave1DataBase());
        System.out.println("recordSqlTable:"+DataBaseConfig.getRecordSqlTable());
        System.out.println("FileName:"+DataBaseConfig.getFileName());
        System.out.println("FilePath:"+DataBaseConfig.getFilePath());
    }
    @Test
    public void testMapperLocation(){
        logger.info("mapperLocations:{}",mapperLocations);
    }



}
