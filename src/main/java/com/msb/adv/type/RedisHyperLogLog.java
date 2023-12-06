package com.msb.adv.type;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * description : 一种基数算法，可以利用极小的内存空间完成独立总数的统计
 *
 * @author kunlunrepo
 * date :  2023-11-28 14:43
 */
@Slf4j
public class RedisHyperLogLog {

    private static final String REDIS_HOST = "192.168.10.150";

    private static final int REDIS_PORT = 6379;

    private static JedisPool jedisPool; // 连接池

    public final static String RS_HLL_NS = "rhll:"; // 命名空间

    public static void main(String[] args) {
        createJedisPool();

        count();

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
     * 统计个数 (大约数)
     */
    public static void count() {
        Jedis jedis = null;
        int size = 10000;
        try {
            jedis = jedisPool.getResource();
            for (int i = 0; i < size; i++) {
                jedis.pfadd(RS_HLL_NS+"countTest", "user"+i);
            }
            long total = jedis.pfcount(RS_HLL_NS + "countTest");
            log.info("实际次数：{}", size);
            log.info("HyperLogLog：{}", total);
        } catch (Exception e) {
            throw new RuntimeException("统计redis中个数失败");
        } finally {
            jedis.close();
        }

    }


}
