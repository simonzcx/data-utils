package com.mingcloud.data.controller.slave1;


import com.alibaba.fastjson.JSONObject;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.service.DataSourceService;
import com.mingcloud.data.service.slave1.Slave1DataBaseService;
import com.mingcloud.data.service.slave1.Slave1TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/slave1")
public class Slave1DataBaseController {

    @Resource
    private Slave1DataBaseService slave1DataBaseService;

    @Resource
    private Slave1TableService slave1TableService;

    @Resource
    private DataSourceService dataSourceService;

    private String slave1DataBaseName;

    private DataSourceEntity slave1DataSource;



    @RequestMapping(value = "/findAllTableName",method = RequestMethod.POST)
    public ServiceResponse<List<DataBaseEntity>> findAllTableName(){
        slave1DataSource = dataSourceService.getSlave1DataSource();
        slave1DataBaseName = dataSourceService.getSlave1DataBase();
        List<DataBaseEntity> list = null;
        DataBaseEntity entity = new DataBaseEntity();
        entity.setDataBaseName(slave1DataBaseName);
        list = slave1DataBaseService.findAllTableName(slave1DataSource,entity);
        return ServiceResponse.ok(list);
    }

    @RequestMapping(value = "/findTableStructure",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public ServiceResponse<List<TableEntity>> findTableStructure(@RequestBody JSONObject jsonParam){
        slave1DataSource = dataSourceService.getSlave1DataSource();
        slave1DataBaseName = dataSourceService.getSlave1DataBase();
        List<TableEntity> list = null;
        //String tableName = "\"" + jsonParam.getString("tableName") + "\"";
        String tableName = jsonParam.getString("tableName");
        DataBaseEntity entity = new DataBaseEntity(slave1DataBaseName,tableName);
        list = slave1TableService.findTableStructure(slave1DataSource,entity);
        return ServiceResponse.ok(list);
    }


}
