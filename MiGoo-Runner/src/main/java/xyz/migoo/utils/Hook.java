package xyz.migoo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Platform;

import java.lang.reflect.Method;
import java.util.HashMap;
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
     * @param hook 待执行的方法列表
     */
    public static void hook(JSONArray hook) {
        if (hook ==null || hook.isEmpty()){
            return;
        }
        for (int i = 0; i < hook.size(); i++){
            String value = hook.getString(i);
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
                methodMap = new HashMap<>(methods.length);
                for (Method method : methods) {
                    methodMap.put(method.getName(), method);
                }
            }
            methodMap.get(methodName).invoke(null, params);
        }catch (Exception e){
            // 不处理异常
            e.printStackTrace();
        }
    }

}