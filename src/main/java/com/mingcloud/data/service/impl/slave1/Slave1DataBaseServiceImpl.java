package com.mingcloud.data.service.impl.slave1;

import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.mapper.slave1.Slave1DataBaseMapper;
import com.mingcloud.data.service.slave1.Slave1DataBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("SlaveDataBaseService")

public class Slave1DataBaseServiceImpl implements Slave1DataBaseService {
    private static final Logger logger= LoggerFactory.getLogger(Slave1DataBaseServiceImpl.class);

    @Resource
    private Slave1DataBaseMapper slave1DataBaseMapper;

    @Override
    @DBChange
    public List<DataBaseEntity> findAllTableName(@DBChange DataSourceEntity datasource,DataBaseEntity entity) {
        List<DataBaseEntity> list = null;
        try {
            list = slave1DataBaseMapper.selectAllTableName(entity);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }
}
