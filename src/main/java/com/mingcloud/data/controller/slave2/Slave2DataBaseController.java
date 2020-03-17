package com.mingcloud.data.controller.slave2;


import com.alibaba.fastjson.JSONObject;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.TableEntity;
import com.mingcloud.data.service.DataSourceService;
import com.mingcloud.data.service.slave2.Slave2DataBaseService;
import com.mingcloud.data.service.slave2.Slave2TableService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/slave2")
public class Slave2DataBaseController {

    @Resource
    private Slave2DataBaseService slave2DataBaseService;

    @Resource
    private Slave2TableService slave2TableService;

    private String slave2DataBase;

    private DataSourceEntity slave2DataSource;

    @Resource
    private DataSourceService dataSourceService;


    @RequestMapping(value = "/findAllTableName",method = RequestMethod.POST)
    public ServiceResponse<List<DataBaseEntity>> findAllTableName(){
        slave2DataBase = dataSourceService.getSlave2DataBase();
        slave2DataSource = dataSourceService.getSlave2DataSource();
        List<DataBaseEntity> list = null;
        DataBaseEntity entity = new DataBaseEntity();
        entity.setDataBaseName(slave2DataBase);
        list = slave2DataBaseService.findAllTableName(slave2DataSource,entity);
        return ServiceResponse.ok(list);
    }

    @RequestMapping(value = "/findTableStructure",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public ServiceResponse<List<TableEntity>> findTableStructure(@RequestBody JSONObject jsonParam){
        slave2DataBase = dataSourceService.getSlave2DataBase();
        slave2DataSource = dataSourceService.getSlave2DataSource();
        List<TableEntity> list = null;
        //String tableName = "\"" + jsonParam.getString("tableName") + "\"";
        String tableName = jsonParam.getString("tableName");
        DataBaseEntity entity = new DataBaseEntity(slave2DataBase,tableName);
        list = slave2TableService.findTableStructure(slave2DataSource,entity);
        return ServiceResponse.ok(list);
    }

}
