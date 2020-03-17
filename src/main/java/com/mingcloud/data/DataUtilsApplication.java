package com.mingcloud.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.mingcloud.data.mapper","com.mingcloud.data.mapper.slave1","com.mingcloud.data.mapper.slave2"})
public class DataUtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataUtilsApplication.class, args);
    }

}
