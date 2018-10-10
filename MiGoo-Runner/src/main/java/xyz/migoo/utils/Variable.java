package xyz.migoo.utils;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.VariableException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 绑定变量
 *
 * @author xiaomi
 * @date 2018/08/02 14:53
 */
public class Variable {

    public static final Pattern PATTERN = Pattern.compile("^\\$\\{(\\w+)\\((.*)\\)\\}");
    private static final String SEPARATOR = ",";
    private static Map<String, Method> methodMap = null;

    private Variable() {
    }

    public static void bindVariable(JSONObject variables, JSONObject body) {
        if (variables == null || body == null || variables.isEmpty() || body.isEmpty()) {
            return;
        }
        for (String variable : variables.keySet()) {
            String value = variables.getString(variable);
            if (PATTERN.matcher(value).find()){
                continue;
            }
            String regex = "$" + variable + "$";
            for (String key : body.keySet()){
                String object = body.getString(key);
                if (StringUtil.contains(object, regex) && StringUtil.isNotBlank(value)){
                    body.put(key, object.replace(regex, value));
                }
            }
        }
    }

    public static void bindVariable(JSONObject variables, String key) {
        if (variables == null || StringUtil.isBlank(key) || variables.isEmpty()) {
            return;
        }
        String body = variables.getString(key);
        if (StringUtil.isBlank(body)){
            return;
        }
        for (String variable : variables.keySet()) {
            String value = variables.getString(variable);
            if (PATTERN.matcher(value).find()){
                continue;
            }
            String regex = "$" + variable + "$";
            if (StringUtil.contains(body, regex)){
                body.replace(regex, value);
            }
        }
    }

    public static void evalVariable(JSONObject variables){
        if (variables == null || variables.isEmpty()){
            return;
        }
        for (String key : variables.keySet()){
            Object object = variables.get(key);
            if (!(object instanceof String)){
                continue;
            }
            Matcher matcher = PATTERN.matcher((String)object);
            if (matcher.find()){
                String params = matcher.group(2);
                if (params.contains("$")){
                    continue;
                }
                functionLoader();
                String result = invoke(matcher.group(1), params);
                if (StringUtil.isNotEmpty(result)) {
                    variables.put(key, result);
                }
            }
        }
    }

    private static void functionLoader() {
        try {
            if (methodMap == null){
                Class clazz = Class.forName(Platform.EXTENDS_VARIABLE);
                Method[] methods = clazz.getDeclaredMethods();
                methodMap = new HashMap<>(methods.length);
                for (Method method : methods) {
                    methodMap.put(method.getName(), method);
                }
            }
        }catch (Exception e){
            throw new VariableException(StringUtil.getStackTrace(e));
        }
    }

    private static String invoke(String methodName, String params) {
        String value;
        try {
            if (StringUtil.isBlank(params)){
                value = (String) methodMap.get(methodName).invoke(null);
            }else if(params.contains(SEPARATOR)){
                Object[] o = new Object[]{params.split(SEPARATOR)};
                value = (String) methodMap.get(methodName).invoke(null, o);
            }else {
                value = (String) methodMap.get(methodName).invoke(null, params);
            }
        }catch (Exception e){
            throw new VariableException(StringUtil.getStackTrace(e));
        }
        return value;
    }

}