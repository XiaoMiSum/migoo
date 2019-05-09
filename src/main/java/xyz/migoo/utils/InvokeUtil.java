package xyz.migoo.utils;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.InvokeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import static xyz.migoo.parser.BindVariable.FUNC_PATTERN;
import static xyz.migoo.parser.BindVariable.PARAM_PATTERN;

/**
 * @author xiaomi
 */
public class InvokeUtil {

    private static Log log = new Log(InvokeUtil.class);
    private static final String SEPARATOR = ",";

    /**
     * 从指定 扩展类中 获取扩展函数
     */
    public static Map<String, Method> functionLoader(Object[] classes) throws InvokeException {
        try {
            Map<String, Method> map = new HashMap<>(100);
            for (Object clazz : classes) {
                Class clz = Class.forName((String) clazz);
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
     * 生成 参数数组
     *
     * @param params 参数字符串，处理时使用,分割
     * @param variables 变量，参数可能使用变量，需要从此对象取出
     */
    public static Object[] parameter(String params, JSONObject variables) throws RuntimeException {
        if (StringUtil.isBlank(params)) {
            return null;
        }
        Object[] parameters = params.split(SEPARATOR);
        Object[] parameter = new Object[parameters.length];
        int i = 0;
        for (Object obj : parameters){
            Matcher param = PARAM_PATTERN.matcher(obj.toString());
            if (variables == null || variables.isEmpty()){
                parameter[i] = obj;
            }else {
                Object object = variables.get(param.group(1));
                if (param.find() && object != null) {
                    if (FUNC_PATTERN.matcher(String.valueOf(obj)).find()
                            || PARAM_PATTERN.matcher(String.valueOf(obj)).find()){
                        throw new RuntimeException(param.group(1) + " need eval!");
                    }
                    parameter[i] = object;
                }else{
                    parameter[i] = obj;
                }
            }
            i += 1;
        }
        return parameter;
    }

    /**
     * 获取 Method 对象
     *
     * @param methods 保存了 Method 对象的 Map
     * @param name method 名称
     * @param params 参数字符串，处理时使用,分割
     */
    public static Method method(Map<String, Method> methods, String name, Object[] params){
        if (params == null){
            return methods.get(String.format("%s(0)", name));
        }else {
            return methods.get(String.format("%s(%s)", name, params.length));
        }
    }

    /**
     * 执行 Method
     *
     * @param method 待执行的 Method 对象
     * @param parameter 参数数组
     */
    public static Object invoke(Method method, Object[] parameter) throws InvokeException {
        Object result;
        String params = parameterToString(parameter);
        try {
            result =  method.invoke(null, parameter);
            log.info(String.format("method invoke, [%s] -> [%s] -> [%s]", method, params, result));
        }catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
            throw new InvokeException("invoke error, method name: " + method + "  parameter: " + params);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvokeException("invoke error, method name: " + method + "  parameter: " + params);
        }
        return result;
    }

    private static String parameterToString(Object[] parameter){
        StringBuilder sb = new StringBuilder();
        for (Object o : parameter){
            if (sb.length() > 0){
                sb.append(SEPARATOR);
            }
            sb.append(o);
        }
        return sb.toString();
    }
}