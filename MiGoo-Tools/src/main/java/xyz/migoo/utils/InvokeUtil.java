package xyz.migoo.utils;

import xyz.migoo.exception.InvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 */
public class InvokeUtil {

    private static Log log = new Log(InvokeUtil.class);
    private static final String SEPARATOR = ",";
    //private static final Pattern JSON_PATTERN = Pattern.compile("「(.+)」$");

    /**
     * 从指定 扩展类中 获取扩展函数
     */
    public static Map<String, Method> functionLoader(String[] classes) throws InvokeException {
        try {
            Map<String, Method> map = new HashMap<>(100);
            for (String clazz : classes) {
                Class clz = Class.forName(clazz);
                for (; clz != null && clz != Object.class; clz = clz.getSuperclass()){
                    Method[] methods = clz.getDeclaredMethods();
                    for (Method method : methods) {
                        String key = method.getName() + "(" + method.getParameterCount() + ")";
                        map.put(key, method);
                    }
                }
            }
            return map;
        }catch (ClassNotFoundException e){
            throw new InvokeException(e.getMessage());
        }
    }

    /**
     * 执行指定扩展函数
     *
     * @param name   方法名
     * @param params 参数
     */
    public static Object invoke(Map<String, Method> methods, String name, String params) throws InvokeException {
        Object result;
        String method = name;
        try {
            if (StringUtil.isBlank(params)) {
                method = method + "(0)";
                result = methods.get(method).invoke(null);
            } else if (params.contains(SEPARATOR)) {
                Object[] parameters = params.split(SEPARATOR);
                int i = 0;
                for (Object obj : parameters){
                    // 将被替换的 json字符串 替换为 json格式
                    obj = obj.toString().replaceAll("'", "\\\"").replaceAll("=", ",").replaceAll("「","{").replaceAll("」", "}");
                    parameters[i] = obj;
                    i = i + 1;
                }
                method = method + "(" + parameters.length + ")";
                result = methods.get(method).invoke(null, parameters);
            } else {
                method = method + "(1)";
                params = params.replaceAll("'", "\\\"").replaceAll("=", ",").replaceAll("「","{").replaceAll("」", "}");
                result = methods.get(method).invoke(null, params);
            }
            log.info(String.format("method invoke, [%s] -> [%s] -> [%s]", method, params, result));
            return result;
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
            throw new InvokeException("invoke error, method name: " + method + "  params: " + params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvokeException("invoke error, method name: " + method + "  params: " + params);
        }
    }
}
