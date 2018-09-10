package xyz.migoo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Platform;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;

import static xyz.migoo.utils.Variable.PATTERN;

/**
 *
 * @author xiaomi
 * @date 2018/08/02 14:53
 */
public class Hook {

    private static Map<String, Method> methodMap = null;

    private Hook() {
    }

    /**
     *
     * @param variables  config  或 case
     * @param key      需要被替换的 JSONObject
     */
    public static void hook(JSONObject variables, String key) {
        if (variables ==null || variables.isEmpty()){
            return;
        }
        JSONArray json = variables.getJSONArray(key);
        if (json.isEmpty()){
            return;
        }
        for (int i = 0; i< json.size(); i++){
            String value = json.getString(i);
            Matcher matcher = PATTERN.matcher(value);
            if (matcher.find()){
                function(matcher.group(1), matcher.group(2));
            }
        }
    }

    private static void function(String methodName, String params) {
        try {
            if (methodMap == null){
                Class clazz = Class.forName(Platform.EXTENDS_HOOK);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    methodMap.put(method.getName(), method);
                }
            }
            methodMap.get(methodName).invoke(null, params);
        }catch (Exception e){
            // 不处理异常
        }
    }

}