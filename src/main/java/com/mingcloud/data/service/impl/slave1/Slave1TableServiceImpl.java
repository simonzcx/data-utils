package com.mingcloud.data.service.impl.slave1;

import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.mapper.slave1.Slave1TableMapper;
import com.mingcloud.data.service.slave1.Slave1TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("SlaveTableService")
public class Slave1TableServiceImpl implements Slave1TableService {
    private static final Logger logger= LoggerFactory.getLogger(Slave1TableServiceImpl.class);

    @Resource
    private Slave1TableMapper slave1TableMapper;

    @Override
    @DBChange
    public List<TableEntity> findTableStructure(@DBChange DataSourceEntity dataSource,Object obj) {
        List<TableEntity> list = null;
        try {
            list =  slave1TableMapper.selectTableStructure(obj);
        } catch (Exception e) {
            logger.error("查询失败!原因是:",e);
        }
        return list;
    }

}
