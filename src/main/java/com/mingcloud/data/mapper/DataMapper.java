package com.mingcloud.data.mapper;

import com.mingcloud.data.entity.DataBaseEntity;
import com.mingcloud.data.entity.SysSqlAnalysisEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface DataMapper {
    /**
     * @return int
     * @throws
     * @Title: existTable
     * @Description: 判断表是否存在
     * @author simon
     * @date 2020/03/09
     */
    int existTable(DataBaseEntity entity);
    /**
     * @return int
     * @throws
     * @Title: dropTable
     * @Description: 删表
     * @author simon
     * @date 2020/03/09
     */
    int dropTable(DataBaseEntity entity) throws DataAccessException;
    /**
     * @return int
     * @throws
     * @Title: createTable
     * @Description: 建表
     * @author simon
     * @date 2020/03/09
     */
    int createTable(DataBaseEntity entity);
    /**
     * @return void
     * @throws
     * @Title: insertRecordSql
     * @Description: 插入sql记录语句
     * @author simon
     * @date 2020/03/10
     */
    void insertRecordSql(SysSqlAnalysisEntity entity);
    /**
     * @return List<SysSqlAnalysisEntity>
     * @throws
     * @Title: selectSqls
     * @Description: 查询sql记录语句
     * @author simon
     * @date 2020/03/10
     */
    List<SysSqlAnalysisEntity> selectSqls(DataBaseEntity entity);
    /**
     * @return void
     * @throws
     * @Title: selectDataInto
     * @Description: 查询数据迁移
     * @author simon
     * @date 2020/03/10
     */
    void selectDataInto(Object obj) throws DataAccessException;
    /**
     * @return void
     * @throws
     * @Title: selectBySqls
     * @Description: 校验sqls是否已存在
     * @author simon
     * @date 2020/03/10
     */
    int selectBySqls(SysSqlAnalysisEntity entity);

}
