package com.mingcloud.data.mapper.slave1;


import com.mingcloud.data.entity.TableEntity;

import java.util.List;

public interface Slave1TableMapper {
    /**
     * 查找数据库表所有列名称
     */
    List<TableEntity> selectTableStructure(Object obj);
}
