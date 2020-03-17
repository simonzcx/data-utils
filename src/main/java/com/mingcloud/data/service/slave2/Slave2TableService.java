package com.mingcloud.data.service.slave2;

import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.TableEntity;

import java.util.List;

public interface Slave2TableService {
    /**
     * @return List<TableEntity>
     * @throws
     * @Title: findTableStructure
     * @Description: 获取表所有字段
     * @author simon
     * @date 2020/03/03
     */
    @DBChange
    List<TableEntity> findTableStructure(@DBChange DataSourceEntity dataSource,DataBaseEntity entity);
}
