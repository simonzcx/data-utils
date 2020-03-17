package com.mingcloud.data.mapper.slave2;

import com.mingcloud.data.entity.DataBaseEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface Slave2DataBaseMapper {

    List<DataBaseEntity> selectAllTableName(DataBaseEntity entity);
}
