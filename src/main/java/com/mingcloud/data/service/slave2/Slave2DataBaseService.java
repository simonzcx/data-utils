package com.mingcloud.data.service.slave2;


import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;

import java.io.IOException;
import java.util.List;

public interface Slave2DataBaseService {
    /**
     * @return String
     * @throws
     * @Title: findAllTableName
     * @Description: 获取所有表名
     * @author simon
     * @date 2020/03/06
     */
    @DBChange
    List<DataBaseEntity> findAllTableName(@DBChange DataSourceEntity dataSource,DataBaseEntity entity);

}
