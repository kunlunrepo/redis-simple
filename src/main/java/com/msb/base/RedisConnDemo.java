package com.msb.base;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;


/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-11-25 11:23
 */
@Slf4j
public class RedisConnDemo {

    private static final String REDIS_HOST = "192.168.10.150";

    private static final int REDIS_PORT = 6379;

    public static void main(String[] args) {

//        standalone();

//        jedisPool();

//        jedisCluster();

    }

    /**
     * 单机版
     */
    public static void standalone() {
        Jedis jedis = new Jedis(REDIS_HOST, REDIS_PORT);
        // 执行set命令
//        String result = jedis.set("name", "redis-standalone");
//        log.info("【redis_执行命令_[set]】：result={}", result);
        // 执行get命令
        String result = jedis.get("name");
        log.info("【redis_执行命令_[get]】：result={}", result);

    }

    /**
     * 连接池版
     */
    public static void jedisPool() {
        // 连接池配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20); // 最大连接数
        config.setMaxIdle(5); // 最大空闲连接数
        config.setMinIdle(3); // 最小空闲连接数

        // 创建jedisPool
        JedisPool jedisPool = new JedisPool(config, REDIS_HOST, REDIS_PORT);

        // 创建jedis对象
        Jedis jedis = jedisPool.getResource();

        // 执行set命令
//        String result =jedis.set("name", "redis-pool");
//        log.info("【redis_执行命令_[set]】：result={}", result);

        // 执行get命令
        String result = jedis.get("name");
        log.info("【redis_执行命令_[get]】：result={}", result);

    }

    public static void jedisCluster() {
        // 集群地址 (暂未部署)
        Set<HostAndPort> addrSet = new HashSet<>();
        addrSet.add(new HostAndPort(REDIS_HOST, REDIS_PORT));

        // 创建集群
        JedisCluster jedisCluster = new JedisCluster(addrSet);

        // 执行set命令
        String result = jedisCluster.set("name", "redis-cluster");
        log.info("【redis_执行命令_[set]】：result={}", result);

    }

}
