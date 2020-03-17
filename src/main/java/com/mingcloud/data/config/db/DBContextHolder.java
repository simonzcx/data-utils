package com.mingcloud.data.config.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class DBContextHolder {
    private final static Logger log = LoggerFactory.getLogger(DBContextHolder.class);
    // 对当前线程的操作-线程安全的
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    /**
     * 数据源的 key集合，用于切换时判断数据源是否存在
     */
    public static List<Object> dataSourceKeys = new ArrayList<>();

    // 调用此方法，切换数据源
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
        log.info("已切换到数据源:{}",dataSource);
    }

    // 获取数据源
    public static String getDataSource() {
        return contextHolder.get();
    }

    // 删除数据源
    public static void clearDataSource() {
        contextHolder.remove();
        log.info("已切换到主数据源");
    }

}
