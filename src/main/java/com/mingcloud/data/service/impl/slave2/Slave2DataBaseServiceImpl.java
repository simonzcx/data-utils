package com.mingcloud.data.service.impl.slave2;


import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.mapper.slave2.Slave2DataBaseMapper;
import com.mingcloud.data.service.slave2.Slave2DataBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("MasterDataBaseService")
public class Slave2DataBaseServiceImpl implements Slave2DataBaseService {
    private static final Logger logger= LoggerFactory.getLogger(Slave2DataBaseServiceImpl.class);

    @Resource
    private Slave2DataBaseMapper slave2DataBaseMapper;

    @Override
    @DBChange
    public List<DataBaseEntity> findAllTableName(@DBChange DataSourceEntity dataSource,DataBaseEntity entity) {
        List<DataBaseEntity> list = null;
        try {
            list = slave2DataBaseMapper.selectAllTableName(entity);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }
}
