package com.mingcloud.data.service;

import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.User;

import java.util.List;

public interface DataSourceService {
    DataSourceEntity getSlave1DataSource();

    DataSourceEntity getSlave2DataSource();

    String getSlave1DataBase();

    String getSlave2DataBase();

    List<DataSourceEntity> get();

    /*@DBChange
    List<User> getUser(@DBChange DataSourceEntity entity);*/
}
