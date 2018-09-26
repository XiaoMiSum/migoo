package xyz.migoo.database;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2018/9/12 22:11
 */
public class Redis {

    private static Jedis jedis;

    public static Jedis conn(String host, int port, String password){
        if (jedis == null){
            jedis = new Jedis(host, port);
            jedis.auth(password);
        }
        return jedis;
    }

    public static String get(String key){
        return jedis.get(key);
    }

    public static Map<String, String> hgetAll(String key){
        return jedis.hgetAll(key);
    }

    public static String hget(String key, String field){
        return jedis.hget(key, field);
    }

    public List<String> hmget(String key, String... fields) {
        return jedis.hmget(key, fields);
    }

    public static long del(String key){
        return jedis.del(key);
    }

    public static long del(String... keys){
        return jedis.del(keys);
    }

    public static long hdel(String key, String... fields){
        return jedis.hdel(key, fields);
    }

}
