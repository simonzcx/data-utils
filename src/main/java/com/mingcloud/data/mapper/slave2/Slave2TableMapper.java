package com.mingcloud.data.mapper.slave2;

import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.TableEntity;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

public interface Slave2TableMapper {
    /**
     * 查找数据库表所有列名称
     */
    List<TableEntity> selectTableStructure(DataBaseEntity entity);

    void runSqls(List<String> list) throws SQLException;
}
