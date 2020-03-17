package com.mingcloud.data.config;

import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.mapper.DataSourceMapper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Component
public class DataBaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseConfig.class);

    private static String masterDataBaseUrl;

    @Getter
    private static String masterDataBase;

    @Getter
    private static String fileName;

    @Getter
    private static String filePath;

    @Getter
    private static String recordSqlTable;


    @Value("${spring.datasource.url}")
    public void setMasterDataBaseUrl(String masterDataBaseUrl) {
        DataBaseConfig.masterDataBaseUrl = masterDataBaseUrl;
        DataBaseConfig.masterDataBase = masterDataBaseUrl.substring(masterDataBaseUrl.lastIndexOf("/") + 1, masterDataBaseUrl.lastIndexOf("?"));
    }
    @Value("${application.fileName}")
    public void setFileName(String fileName) {
        DataBaseConfig.fileName = fileName;
    }
    @Value("${application.filePath}")
    public void setFilePath(String filePath) {
        DataBaseConfig.filePath = filePath;
    }
    @Value("${application.recordSqlTable}")
    public void setRecordSqlTable(String recordSqlTable) {
        DataBaseConfig.recordSqlTable = recordSqlTable;
    }

}
