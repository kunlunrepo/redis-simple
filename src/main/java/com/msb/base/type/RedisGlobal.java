package com.msb.base.type;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-11-25 17:05
 */
@Slf4j
public class RedisGlobal {

    private static final String REDIS_HOST = "192.168.10.150";

    private static final int REDIS_PORT = 6379;

    private static JedisPool jedisPool; // 连接池


    public static void main(String[] args) {
        createJedisPool();

//        Set<String> keys = keys("*");
//        log.info("【redis_执行命令_[keys]】：result={}", keys);

//        Long dbsize = dbsize();
//        log.info("【redis_执行命令_[dbsize]】：result={}", dbsize);

//        Boolean isExist = existKey("rs:test20");
//        log.info("【redis_执行命令_[isExist]】：result={}", isExist);

        Long result = expire("rs:test1", 1);
        log.info("【redis_执行命令_[expire]】：result={}", result);

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
     * 获取所有key
     */
    public static Set<String> keys(String pattern) {
        Jedis jedis = null;
        Set<String> keys;
        try {
            jedis = jedisPool.getResource();
            keys = jedis.keys(pattern);
        } catch (Exception e) {
            throw new RuntimeException("获取所有key失败");
        } finally {
            jedis.close();
        }
        return keys;
    }

    /**
     * 数据库大小
     */
    public static Long dbsize() {
        Jedis jedis = null;
        Long dbSize;
        try {
            jedis = jedisPool.getResource();
            dbSize = jedis.dbSize();
        } catch (Exception e) {
            throw new RuntimeException("获取数据库大小失败");
        } finally {
            jedis.close();
        }
        return dbSize;
    }

    /**
     * 查询key是否存在
     */
    public static Boolean existKey(String key) {
        Jedis jedis = null;
        Boolean isExists;
        try {
            jedis = jedisPool.getResource();
            isExists = jedis.exists(key);
        } catch (Exception e) {
            throw new RuntimeException("查询指定key "+key+" 是否存在失败");
        } finally {
            jedis.close();
        }
        return isExists;
    }

    /**
     * 设置指定key的过期时间
     */
    public static Long expire(String key, int seconds) {
        Jedis jedis = null;
        Long result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            throw new RuntimeException("设置指定key "+key+" 的过期时间失败");
        } finally {
            jedis.close();
        }
        return result;
    }

    /**
     * 设置指定key的过期时间
     */
    public static Long ttl(String key) {
        Jedis jedis = null;
        Long duration;
        try {
            jedis = jedisPool.getResource();
            duration = jedis.ttl(key);
        } catch (Exception e) {
            throw new RuntimeException("获取指定key "+key+" 的过期时间失败");
        } finally {
            jedis.close();
        }
        return duration;
    }
}
