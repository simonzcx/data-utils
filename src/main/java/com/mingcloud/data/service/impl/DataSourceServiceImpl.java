package com.mingcloud.data.service.impl;

import com.mingcloud.data.mapper.DataSourceMapper;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.service.DataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataSourceServiceImpl implements DataSourceService {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    @Resource
    private DataSourceMapper dataSourceMapper;

    List<DataSourceEntity> dataSourceList;


    public DataSourceEntity getSlave1DataSource() {
        dataSourceList = this.get();
        return dataSourceList.get(0);
    }

    public DataSourceEntity getSlave2DataSource() {
        dataSourceList = this.get();
        return dataSourceList.get(1);
    }

    public String getSlave1DataBase() {
        dataSourceList = this.get();
        DataSourceEntity entity = dataSourceList.get(0);
        String slave1DataBaseUrl = entity.getUrl();
        return slave1DataBaseUrl.substring(slave1DataBaseUrl.lastIndexOf("/") + 1, slave1DataBaseUrl.lastIndexOf("?"));
    }

    public String getSlave2DataBase() {
        dataSourceList = this.get();
        DataSourceEntity entity = dataSourceList.get(1);
        String slave2DataBaseUrl = entity.getUrl();
        return slave2DataBaseUrl.substring(slave2DataBaseUrl.lastIndexOf("/") + 1, slave2DataBaseUrl.lastIndexOf("?"));
    }

    /**
     * 使用默认数据源获取数据库中的其他数据库链接信息
     * @return
     */
    public List<DataSourceEntity> get(){
        return dataSourceMapper.select();
    }

    /**
     * 通过注解将数据源改为参数指定的。
     * @param entity
     * @return
     */
   /* @DBChange
    public List<User> getUser(@DBChange DataSourceEntity entity){
       return userMapper.select();
    }*/


}
