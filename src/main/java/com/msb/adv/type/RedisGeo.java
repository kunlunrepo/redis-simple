package com.msb.adv.type;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;
import redis.clients.jedis.params.GeoRadiusParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description :
 *
 * @author kunlunrepo
 * date :  2023-11-28 16:55
 */
@Slf4j
public class RedisGeo {

    private static final String REDIS_HOST = "192.168.10.150";

    private static final int REDIS_PORT = 6379;

    private static JedisPool jedisPool; // 连接池

    public final static String RS_GEO_NS = "rg:"; // 命名空间

    public static void main(String[] args) {
        createJedisPool();

//        Map<String, GeoCoordinate> memberCoordinateMap = new HashMap<>();
//        memberCoordinateMap.put("tianjin",new GeoCoordinate(117.12,39.08));
//        memberCoordinateMap.put("shijiazhuang",new GeoCoordinate(114.29,38.02));
//        memberCoordinateMap.put("tangshan",new GeoCoordinate(118.01,39.38));
//        memberCoordinateMap.put("baoding",new GeoCoordinate(115.29,38.51));
//
//        Long result = addLocations("cities", memberCoordinateMap);
//        log.info("【redis_执行命令_[geoadd]】：result={}", result);



        List<GeoRadiusResponse> responses = nearbyMore("cities","beijing",150,
                true,true);
        for(GeoRadiusResponse city:responses){
            System.out.println(city.getMemberByString());
            System.out.println(city.getDistance());
            System.out.println("-------------------------");
        }

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
     * 新增位置信息
     * @param longitude 经度
     * @param latitude 维度
     * @param member 成员
     */
    public static Long addLocation(String key, double longitude, double latitude, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.geoadd(RS_GEO_NS+key, longitude, latitude, member);
        } catch (Exception e) {
            throw new RuntimeException("向redis新增位置失败");
        } finally {
            jedis.close();
        }
    }

    /**
     * 新增多个位置信息
     * @param memberMap 坐标集合
     * @return
     */
    public static Long addLocations(String key, Map<String, GeoCoordinate> memberMap) { // memberMap 坐标
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.geoadd(RS_GEO_NS+key, memberMap);
        } catch (Exception e) {
            throw new RuntimeException("向redis新增多个位置失败");
        } finally {
            jedis.close();
        }
    }


    /**
     * (未测试成功)
     * @param key
     * @param member
     * @param radius
     * @param withDist 返回结果中包含离中心节点位置的距离
     * @param isASC
     * @return
     */
    public static List<GeoRadiusResponse> nearbyMore(
            String key,
            String member,
            double radius,
            boolean withDist,
            boolean isASC
    ) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            GeoRadiusParam geoRadiusParam = new GeoRadiusParam();
            if (withDist) geoRadiusParam.withDist();
            if (isASC) geoRadiusParam.sortAscending();
            else geoRadiusParam.sortDescending();
            return jedis.georadiusByMember(RS_GEO_NS+key, member, radius, GeoUnit.KM, geoRadiusParam);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("计算位置失败");
        } finally {
            jedis.close();
        }
    }
}
