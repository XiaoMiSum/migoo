package xyz.migoo.framework;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-11-22 23:48
 */
public class Vars {

    private static final Map<String, JSONObject> VARS = new HashMap<>(16);

    static void add(String key, JSONObject vars){
        if (vars != null){
            VARS.put(key, vars);
        }
    }

    public static JSONObject get(String key){
        return VARS.get(key);
    }

    private Vars(){}
}
