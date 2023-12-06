package com.msb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class RedisSimpleApp {

    private static final Logger log = Logger.getLogger(RedisSimpleApp.class.getName());

    public static void main(String[] args) {
        log.info("【redis-simple】开始启动...");
        SpringApplication.run(RedisSimpleApp.class, args);
        log.info("【redis-simple】启动完成");
    }
}