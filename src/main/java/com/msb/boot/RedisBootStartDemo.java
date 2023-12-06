package com.msb.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-11-25 14:06
 */
@SpringBootApplication
@Slf4j
public class RedisBootStartDemo {

    public static void main(String[] args) {
        log.info("【RedisBootStartDemo】开始执行...");
        ConfigurableApplicationContext context = SpringApplication.run(RedisBootStartDemo.class, args);
        RedisBootDemo redisBootDemo = context.getBean(RedisBootDemo.class);
//        redisBootDemo.testRedisTemplate();
        redisBootDemo.testStringRedisTemplate();
        log.info("【RedisBootStartDemo】执行完成");
    }
}
