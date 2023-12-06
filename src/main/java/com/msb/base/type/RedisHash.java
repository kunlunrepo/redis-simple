package com.msb.base.type;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-11-25 16:04
 */
@Slf4j
public class RedisHash {

    private static final String REDIS_HOST = "192.168.10.150";

    private static final int REDIS_PORT = 6379;

    private static JedisPool jedisPool; // 连接池

    public final static String RS_HASH_NS = "rh:"; // 字符串存储key的命名空间

    public static void main(String[] args) {

        createJedisPool();
        // 设置值
//        Long resultA = set(RS_HASH_NS + "test1", "a", "Hello a");
//        Long resultB = set(RS_HASH_NS + "test1", "b", "Hello b");
//        log.info("【redis_执行命令_[set]】：resultA={}", resultA);
//        log.info("【redis_执行命令_[set]】：resultB={}", resultB);

        // 获取值
        String getResult = get(RS_HASH_NS + "test1",  "a");
        log.info("【redis_执行命令_[get]】：result={}", getResult);


    }

    /**
     * 创建连接池
     */
    public static void createJedisPool() {
        // 连接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20); // 最大连接数
        config.setMaxIdle(5); // 最大空闲连接数
        config.setMinIdle(3); // 最小空闲连接数

        // 创建jedisPool
        jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT);
        log.info("【redis_初始化连接池完成】：jedisPool={}", jedisPool);
    }

    /**
     * 向redis中存值
     */
    public static Long set(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hset(RS_HASH_NS+key, field, value);
        } catch (Exception e) {
            throw new RuntimeException("向redis中存值失败");
        } finally {
            jedis.close();
        }
    }

    /**
     * 根据key、field获取指定value
     */
    public static String get(String key, String field) {
        return getRaw(RS_HASH_NS+key, field);
    }

    /**
     * 根据key、field获取指定value
     */
    public static String getRaw(String key, String field) {
        Jedis jedis = null;
        String value;
        try {
            jedis = jedisPool.getResource();
            value = jedis.hget(key, field); //
        } catch (Exception e) {
            throw new RuntimeException("批量向redis中存值失败！");
        } finally {
            jedis.close();
        }
        return value;

    }

}
