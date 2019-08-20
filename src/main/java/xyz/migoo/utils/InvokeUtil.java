package xyz.migoo.utils;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.InvokeException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.migoo.parser.BindVariable.FUNC_PATTERN;
import static xyz.migoo.parser.BindVariable.PARAM_PATTERN;

/**
 * @author xiaomi
 */
public class InvokeUtil {

    private static final String SEPARATOR = ",";
    private static final Pattern REGEX_LONG = Pattern.compile("^[-\\+]?[0-9]+$");
    private static final Pattern REGEX_FLOAT = Pattern.compile("^[-\\+]?[0-9]+\\.[0-9]+$");
    private static final Pattern REGEX_BOOLEAN = Pattern.compile("^[true\\false]$");

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
                        String key = String.format("%s(%s)", method.getName(), method.getParameterCount());
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
    private static Object[] parameter(String params, JSONObject variables) throws RuntimeException {
        if (StringUtil.isBlank(params)) {
            return null;
        }
        String[] strParams = params.split(SEPARATOR);
        Object[] parameters = new Object[strParams.length];
        for (int i = 0; i < strParams.length; i++) {
            String str= strParams[i];
            if (REGEX_LONG.matcher(str).find()){
                parameters[i] = Long.valueOf(str);
            }
            if (REGEX_FLOAT.matcher(str).find()){
                parameters[i] = Double.valueOf(str);
            }
            if (REGEX_BOOLEAN.matcher(str).find()){
                parameters[i] = Boolean.valueOf(str);
            }
            Matcher param = PARAM_PATTERN.matcher(str);
            if (param.find()) {
                if (variables != null && !variables.isEmpty()){
                    Object object = variables.get(param.group(1));
                    if (FUNC_PATTERN.matcher(String.valueOf(object)).find()
                            || PARAM_PATTERN.matcher(String.valueOf(object)).find()){
                        throw new RuntimeException(String.format("%s need eval!", param.group(1)));
                    }
                    parameters[i] = object;
                }
            }
            parameters[i] = str;
        }
        return parameters;
    }

    /**
     * 获取 Method 对象
     *
     * @param methods 保存了 Method 对象的 Map
     * @param name method 名称
     * @param params 参数字符串，处理时使用,分割
     */
    private static Method method(Map<String, Method> methods, String name, Object[] params){
        if (params == null){
            return methods.get(String.format("%s(0)", name));
        }else {
            return methods.get(String.format("%s(%s)", name, params.length));
        }
    }

    /**
     * 执行 Method
     *
     * @param methods 保存了 Method 对象的 Map
     * @param name method 名称
     * @param parameter 参数数组
     */
    public static Object invoke(Map<String, Method> methods, String name, String parameter, JSONObject variables)
            throws InvokeException {
        Object result;
        try {
            Object[] parameters = parameter(parameter, variables);
            result = method(methods, name, parameters).invoke(null, parameters);
            MiGooLog.log(String.format("invoke success, method [%s] -> parameter [%s] -> return [%s]", name, parameter, result));
        } catch (NullPointerException e){
            throw new InvokeException(String.format("method '%s(%s)' not found !", name, parameter));
        } catch (Exception e) {
            throw new InvokeException(String.format("invoke error, method name '%s', parameter '%s'", name, parameter), e);
        }
        return result;
    }
}
