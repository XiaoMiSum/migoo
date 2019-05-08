package xyz.migoo.utils.database;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2018/9/12 22:11
 */
public class Redis {

    private Redis(){

    }

    private Jedis jedis;

    public Redis(String host, int port, String password){
        jedis = new Jedis(host, port);
        jedis.auth(password);
    }

    public Redis(String host, int port){
        jedis = new Jedis(host, port);
    }

    public Redis(String host){
        jedis = new Jedis(host, 6379);
    }

    public String select(int index){
        return jedis.select(index);
    }

    public String get(String key){
        return jedis.get(key);
    }

    public String set(String key, String value){
        return jedis.set(key, value);
    }

    public Map<String, String> hgetAll(String key){
        return jedis.hgetAll(key);
    }

    public String hget(String key, String field){
        return jedis.hget(key, field);
    }

    public List<String> hmget(String key, String... fields) {
        return jedis.hmget(key, fields);
    }

    public Long del(String key){
        return jedis.del(key);
    }

    public Long del(String... keys){
        return jedis.del(keys);
    }

    public Long hdel(String key, String... fields){
        return jedis.hdel(key, fields);
    }

}
