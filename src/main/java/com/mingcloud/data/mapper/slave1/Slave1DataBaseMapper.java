package com.mingcloud.data.mapper.slave1;

import com.mingcloud.data.entity.DataBaseEntity;

import java.util.List;

public interface Slave1DataBaseMapper {

    List<DataBaseEntity> selectAllTableName(DataBaseEntity entity);

}
