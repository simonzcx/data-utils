package com.mingcloud.data.controller;

import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.dto.TableDto;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.service.DataSourceService;
import com.mingcloud.data.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "/data")
public class DataController {
    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private DataService dataService;

    private String recordSqlTable;

    private String masterDataBase;

    private DataSourceEntity slave2DataSource;

    @Resource
    private DataSourceService dataSourceService;


    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public ServiceResponse<String> SysSqlAnalysisInit() throws IOException {
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        masterDataBase = DataBaseConfig.getMasterDataBase();

        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName(recordSqlTable);
        //entity.setTableName("`"+recordSqlTable+"`");
        entity.setDataBaseName(masterDataBase);
        Boolean init = null;
        init = dataService.sysSqlAnalysisInit(entity);
        if (init) {
            return ServiceResponse.ok("创建sql记录表:" + entity.getTableName() + " 成功，初始化完成！");
        } else {
            return ServiceResponse.error("初始化失败！");
        }
    }

    @RequestMapping(value = "/generateSql", method = RequestMethod.POST)
    public ServiceResponse generateSql(@RequestBody TableDto dto) {
        return dataService.findDataInsertInto(dto);
    }

    @RequestMapping(value = "/downloadSqlFile", method = RequestMethod.GET)
    public ServiceResponse downloadSqlFile(HttpServletRequest request, HttpServletResponse response) {
        ServiceResponse result = null;
        try {
            result = dataService.downloadSqlFile(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public ServiceResponse dropRecordSqlTable(){
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        DataBaseEntity entity = new DataBaseEntity();
        entity.setTableName(recordSqlTable);
        try {
            dataService.dropTable(entity);
            logger.info("删除sql记录表：{} 成功！",recordSqlTable);
            return ServiceResponse.ok("删除sql记录表：" + recordSqlTable + "成功！");
        } catch (DataAccessException e){
            logger.info("删除sql记录表：{} 失败！异常：{}",recordSqlTable,e.getMessage());
            if(e.getMessage().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException")){
                return ServiceResponse.error("sql记录表：" + recordSqlTable + "不存在，删除失败！");
            }
            return ServiceResponse.error("删除sql记录表：" + recordSqlTable + "失败！" + e.getRootCause());
        }
    }

    @RequestMapping(value = "/dataMigration", method = RequestMethod.POST)
    public ServiceResponse dataMigration(){
        DataBaseEntity entity = new DataBaseEntity();
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        entity.setTableName(recordSqlTable);
        List<String> sqlsList = dataService.getSqls(entity);
        slave2DataSource = dataSourceService.getSlave2DataSource();
        return dataService.runSqls(slave2DataSource,sqlsList);
    }
}
