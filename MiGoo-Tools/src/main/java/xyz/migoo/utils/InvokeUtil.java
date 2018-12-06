package xyz.migoo.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2018-12-06 13:51
 */
public class InvokeUtil {

    public static final String SEPARATOR = ",";

    public static Map<String, Method> functionLoader(String[] clazz) throws ClassNotFoundException {
        Map<String, Method> map = new HashMap<>(100);
        for (String clz : clazz) {
            Method[] methods = Class.forName(clz).getDeclaredMethods();
            for (Method method : methods) {
                map.put(method.getName(), method);
            }
        }
        return map;
    }

    public static Object invoke(Map<String, Method> methods, String name, String params) throws InvocationTargetException, IllegalAccessException {
        Object value;
        if (StringUtil.isBlank(params)){
            value = methods.get(name).invoke(null);
        }else if(params.contains(SEPARATOR)){
            value = methods.get(name).invoke(null, params.split(SEPARATOR));
        }else {
            value = methods.get(name).invoke(null, params);
        }
        return value;
    }
}
