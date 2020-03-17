package com.mingcloud.data.entity;

import lombok.Getter;
import lombok.Setter;

public class SysSqlAnalysisEntity {
    // 主键
    @Setter
    @Getter
    private String id;
    // 请求参数
    @Setter
    private String parameter;
    // 完整sql 语句
    @Setter
    @Getter
    private String sqls;

    @Setter
    @Getter
    private String tableName;

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }

}
