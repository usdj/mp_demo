package com.usdj.database;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gerrydeng
 * @date 2019-07-19 20:15
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.usdj.database.dao")
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
