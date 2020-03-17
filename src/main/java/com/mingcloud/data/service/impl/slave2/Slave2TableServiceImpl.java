package com.mingcloud.data.service.impl.slave2;

import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.mapper.slave2.Slave2TableMapper;
import com.mingcloud.data.service.slave2.Slave2TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("MasterTableService")
public class Slave2TableServiceImpl implements Slave2TableService {
    private static final Logger logger= LoggerFactory.getLogger(Slave2TableServiceImpl.class);

    @Resource
    private Slave2TableMapper slave2TableMapper;

    @Override
    @DBChange
    public List<TableEntity> findTableStructure(@DBChange DataSourceEntity dataSource,DataBaseEntity entity) {
        List<TableEntity> list = null;
        try {
            list =  slave2TableMapper.selectTableStructure(entity);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }
}
