package com.msb.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msb.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-11-25 13:58
 */
@Component
@Slf4j
public class RedisBootDemo {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * redisTemplate 通用
     */
    public void testRedisTemplate() {
        log.info("【RedisBootDemo】开始...");
        // 创建连接对象
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();

        // 执行set命令
//        Boolean result = connection.set("redis-boot".getBytes(), "项目测试".getBytes());
//        log.info("【RedisBoot_执行命令_[set]】：result={}", result);

        // 执行get命令
        byte[] result = connection.get("redis-boot".getBytes());
        log.info("【RedisBoot_执行命令_[get]】：result={}", new String(result));


        log.info("【RedisBootDemo】结束");
    }

    /**
     * stringRedisTemplate 常见 key为string
     */
    public void testStringRedisTemplate() {
        log.info("【RedisBootDemo】开始...");

        // (1)操作string
//        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//        String key = "string-redis-template";
//        String value = "测试StringRedisTemplate";
//        // 执行set命令
//        operations.set(key, value);
//        // 执行get命令
//        String result = operations.get(key);
//        log.info("【RedisBoot_执行命令_[get]】：result={}", result);

        // (2)操作hash
//        HashOperations<String, Object, Object> operations = stringRedisTemplate.opsForHash();
//        String mapKey = "sean";
//        // 执行set命令
//        operations.put(mapKey, "name", "kunlun");
//        operations.put(mapKey, "age", "22");
//        // 执行get命令
//        Map<Object, Object> result = operations.entries(mapKey);
//        log.info("【RedisBoot_执行命令_[get]】：result={}", result);

        // (3)操作object
        // 设置hash的value的序列化格式
//        stringRedisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
//        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false); // json转hash
//        Person person = new Person();
//        person.setName("zhangsan");
//        person.setAge(22);
//        String mapKey = "sean01";
//        // 执行set命令
//        stringRedisTemplate.opsForHash().putAll(mapKey, jm.toHash(person));
//        // 执行get命令
//        Map<Object, Object> result = stringRedisTemplate.opsForHash().entries(mapKey);
//        Person resultPerson = objectMapper.convertValue(result, Person.class); // 等到的值需要转换
//        log.info("【RedisBoot_执行命令_[get]】：result={}", resultPerson);

        // (4)操作subscribe
        String channel = "kkll";
        // 推送数据
        stringRedisTemplate.convertAndSend(channel, "【前】hello world"); // 收不到该消息
        // 订阅数据
        RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection();
        connection.subscribe(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                byte[] result = message.getBody();
                log.info("【RedisBoot_执行命令_[subscribe]】：result={}", new String(result));
            }
        }, channel.getBytes());
        // 推送数据
        while (true) {
            stringRedisTemplate.convertAndSend(channel, "【后】hello world");
           try {
               Thread.sleep(3000);
           } catch (InterruptedException r) {
               r.printStackTrace();
           }
        }

//        log.info("【RedisBootDemo】结束");
    }

}
