package com.mingcloud.data.service.impl;

import com.alibaba.fastjson.JSON;
import com.mingcloud.data.common.DBChange;
import com.mingcloud.data.common.ServiceResponse;
import com.mingcloud.data.config.DataBaseConfig;
import com.mingcloud.data.dto.TableDto;
import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.DataSourceEntity;
import com.mingcloud.data.entity.SysSqlAnalysisEntity;
import com.mingcloud.data.mapper.DataMapper;
import com.mingcloud.data.mapper.slave2.Slave2TableMapper;
import com.mingcloud.data.service.DataSourceService;
import com.mingcloud.data.service.DataService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;

@Service
public class DataServiceImpl implements DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);
    @Resource
    private DataMapper dataMapper;

    @Resource
    private Slave2TableMapper slave2TableMapper;

    @Resource
    private DataSourceService dataSourceService;

    private String filePath;

    private String fileName;

    private String recordSqlTable;

    private String slave2DataBase;

    private String slave1DataBase;

    @Override
    public Boolean sysSqlAnalysisInit(DataBaseEntity entity) {
        int existTable = dataMapper.existTable(entity);
        if (existTable == 0) {
            dataMapper.createTable(entity);
        } else if (existTable > 0) {
            int dropTable = dataMapper.dropTable(entity);
            if (dropTable == 0) {
                dataMapper.createTable(entity);
            } else {
                logger.info("删除表：{} 失败！", entity.getTableName());
            }
        } else {
            return false;
        }
        logger.info("初始化成功，新建表：{}", entity.getTableName());
        return true;
    }

    @Override
    public void generateSqlFile(DataBaseEntity entity) {
        List<SysSqlAnalysisEntity> entityList = dataMapper.selectSqls(entity);
        fileName = DataBaseConfig.getFileName();
        filePath = DataBaseConfig.getFilePath();
        List<String> sqlList = new ArrayList<>();
        for (SysSqlAnalysisEntity en : entityList) {
            sqlList.add(en.getSqls());
        }
        try {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                this.deleteIfExists(file);
                file.createNewFile();
            }
            /*
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sqlList.get(i)+";");
            writer.newLine();
            writer.close();
            */
            String sql = null;
            for (int i = 0; i < sqlList.size(); i++) {
                sql = sqlList.get(i) + ";";
                FileUtils.writeStringToFile(file, sql + "\r\n", "UTF-8", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServiceResponse downloadSqlFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataBaseEntity entity = new DataBaseEntity();
        recordSqlTable = DataBaseConfig.getRecordSqlTable();
        entity.setTableName(recordSqlTable);

        this.generateSqlFile(entity);
        File file = new File(filePath + fileName);

        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            //统一转换编码
            fileName = URLEncoder.encode(fileName, "utf-8");
            //取得输入流
            outputStream = response.getOutputStream();
            // 清空输出流 设置输出的格式
            response.reset();
            response.setContentType("application/x-msdownload;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            /*response.setContentType("application/x-download;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"), "iso-8859-1"));*/
            //读取流
            inputStream = new FileInputStream(file);
            //输入流、输出流的转换 commons-io-xx.jar的方法
            IOUtils.copy(inputStream, outputStream);
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            return ServiceResponse.error("文件没找到,error:"+e.getMessage());
        } catch (IOException e) {
            return ServiceResponse.error("下载文件失败,error:"+e.getMessage());
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return null;
    }

    @Override
    public ServiceResponse findDataInsertInto(TableDto dto) throws DataAccessException {
        SysSqlAnalysisEntity analysis = new SysSqlAnalysisEntity();
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataBaseMap = new HashMap<>();
        Map<String, Object> tableMap = new HashMap<>();
        slave1DataBase = dataSourceService.getSlave1DataBase();
        slave2DataBase = dataSourceService.getSlave2DataBase();
        String newDataBase = "`" + slave2DataBase + "`";
        String oldDataBase = "`" + slave1DataBase + "`";
        //dataMapper.selectDataInto(map);
        /* ------------------------------------------------------- */
        /* insert into `geely2.0`.t_user ( id , `name` , age ) select id , `name` , age from `geely1.0`.t_user where `geely1.0`.t_user.`name` = 'aaa' */
        //拼接sql
        StringBuffer sql = new StringBuffer();
        if(newDataBase != null && !newDataBase.equals("")){
            if(dto.getNewTable() != null && !dto.getNewTable().equals("")){
                String prefix = "insert into " + newDataBase + "." + dto.getNewTable() + " ( " ;
                sql.append(prefix);
                //通过Map.entrySet遍历key和value
                if(!dto.columnMap.isEmpty()){
                    for (Map.Entry<String, Object> entry : dto.columnMap.entrySet()) {
                        sql.append(entry.getKey()).append(",");
                    }
                    sql.deleteCharAt(sql.length()-1);
                    String midfix = " ) " + " select ";
                    sql.append(midfix);
                    for (Map.Entry<String, Object> entry : dto.columnMap.entrySet()) {
                        sql.append(entry.getValue()).append(",");
                    }
                    sql.deleteCharAt(sql.length()-1);
                } else {
                    return ServiceResponse.error("columnMap为空："+ dto.columnMap);
                }
                if(oldDataBase != null && !oldDataBase.equals("")) {
                    if (dto.getOldTable() != null && !dto.getOldTable().equals("")) {
                        String suffix = " from " + oldDataBase + "." + dto.getOldTable() + " ";
                        sql.append(suffix);
                        if(dto.getFilterSql() != null && !dto.getFilterSql().equals("")){
                            String filterSql = dto.getFilterSql().trim();
                            if(filterSql.startsWith("where")){
                                sql.append(filterSql);
                            } else {
                                sql.append(" where ");
                                sql.append(filterSql);
                            }
                        }
                        tableMap.put("newTable", dto.getNewTable());
                        tableMap.put("oldTable", dto.getOldTable());
                        dataBaseMap.put("newDataBase", newDataBase);
                        dataBaseMap.put("oldDataBase", oldDataBase);
                        map.put("dataBaseMap", dataBaseMap);
                        map.put("tableMap", tableMap);
                        map.put("columnMap", dto.columnMap);
                    } else {
                        return ServiceResponse.error("数据库表NewTable为："+ dto.getOldTable());
                    }
                } else {
                    return ServiceResponse.error("数据库oldDataBase为："+ newDataBase);
                }
            } else {
                return ServiceResponse.error("数据库表NewTable为："+ dto.getNewTable());
            }
        } else {
            return ServiceResponse.error("数据库newDataBase为："+ newDataBase);
        }
        logger.info("sql:{}",sql);
        analysis.setParameter(JSON.toJSONString(map));
        analysis.setSqls(sql.toString());
        analysis.setTableName(DataBaseConfig.getRecordSqlTable());
        int selectBySqls = dataMapper.selectBySqls(analysis);
        if(selectBySqls > 0){
            return ServiceResponse.error("数据：" + dto.getOldTable() + "迁移到：" + dto.getNewTable() + "sql记录已存在！");
        } else {
            dataMapper.insertRecordSql(analysis);
            return ServiceResponse.ok("数据：" + dto.getOldTable() + "迁移到：" + dto.getNewTable() + "sql记录成功！");
        }
    }

    @Override
    public int dropTable(DataBaseEntity entity) throws DataAccessException {
        return dataMapper.dropTable(entity);
    }

    @Override
    @DBChange
    //@Transactional
    public ServiceResponse runSqls(@DBChange DataSourceEntity dataSource, List<String> list) {
        try {
            slave2TableMapper.runSqls(list);
        } catch (SQLException e) {
            if(e.getMessage().contains("com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException")){
                return ServiceResponse.error("sql语句可能存在错误，数据迁移失败！"+e.getCause());
            } else {
                //e.printStackTrace();
                logger.info("Exception:"+e.getMessage());
                return ServiceResponse.error("数据迁移失败！"+e.getCause());
            }
        }
        return ServiceResponse.ok("数据迁移成功！");
    }

    @Override
    public List<String> getSqls(DataBaseEntity entity) {
        List<SysSqlAnalysisEntity> entityList = dataMapper.selectSqls(entity);
        ArrayList<String> list = new ArrayList<>();
        for (SysSqlAnalysisEntity ent : entityList) {
            list.add(ent.getSqls());
        }
        return list;
    }

    /**
     * 删除文件或文件夹
     */
    public void deleteIfExists(File file) throws IOException {
        if (file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File temp : files) {
                        deleteIfExists(temp);
                    }
                }
                if (!file.delete()) {
                    throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
                }
            }
        }
    }

}
