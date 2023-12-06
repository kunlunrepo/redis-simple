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
public class RedisString {

    private static final String REDIS_HOST = "192.168.10.150";

    private static final int REDIS_PORT = 6379;

    private static JedisPool jedisPool; // 连接池

    public final static String RS_STR_NS = "rs:"; // 字符串存储key的命名空间

    public static void main(String[] args) {

        createJedisPool();
        // 设置值
//        String resultA = set(RS_STR_NS + "test1", "Hello a");
//        String resultB = set(RS_STR_NS + "test2", "Hello b");
//        String resultC = set(RS_STR_NS + "test3", "Hello c");
//        String resultD = set(RS_STR_NS + "test4", "Hello d");
//        log.info("【redis_执行命令_[set]】：resultA={}", resultA);
//        log.info("【redis_执行命令_[set]】：resultB={}", resultB);
//        log.info("【redis_执行命令_[set]】：resultC={}", resultC);
//        log.info("【redis_执行命令_[set]】：resultD={}", resultD);

        // 批量设置值
//        String result = msetRaw(RS_STR_NS + "test5", "批量值1",
//                RS_STR_NS + "test6", "批量值2",
//                RS_STR_NS + "test7", "批量值3",
//                RS_STR_NS + "test8", "批量值4");
//        log.info("【redis_执行命令_[set]】：result={}", result);

        // 获取值
        String getResult = get(RS_STR_NS + "test5");
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
    public static String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(RS_STR_NS+key, value);
        } catch (Exception e) {
            throw new RuntimeException("向redis中存值失败");
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取redis中的值
     */
    public static String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(RS_STR_NS+key);
        } catch (Exception e) {
            throw new RuntimeException("获取redis值失败！");
        } finally {
            jedis.close();
        }
    }

    /**
     * 批量设置值 (未测试成功)
     */
    public static String msetRaw(String... keysValues) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.mset(keysValues); //
        } catch (Exception e) {
            throw new RuntimeException("批量向redis中存值失败！");
        } finally {
            jedis.close();
        }

    }

}
