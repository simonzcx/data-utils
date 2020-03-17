package com.mingcloud.data.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TableEntity {

    private String tableName;

    private String columnName;

    private String dataType;

    private String columnComment;
}
