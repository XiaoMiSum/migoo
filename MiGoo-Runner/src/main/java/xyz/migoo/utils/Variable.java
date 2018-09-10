package xyz.migoo.utils;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.VariableException;

import java.lang.reflect.Method;
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

    public static final Pattern PATTERN = Pattern.compile("^\\$\\{(\\w+)\\(([\\$\\w[-]*, ]*)\\)\\}");
    private static Map<String, Method> methodMap = null;

    private Variable() {
    }

    public static void bindVariable(JSONObject variables, JSONObject body) {
        if (variables == null || body == null || variables.isEmpty() || body.isEmpty()) {
            return;
        }
        for (String variable : variables.keySet()) {
            String value = variables.getString(variable);
            String regex = "$" + variable;
            for (String key : body.keySet()){
                String object = body.getString(key);
                if (StringUtil.contains(object, regex)){
                    body.put(key, object.replace(regex, value));
                }
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
                variables.put(key, function(matcher.group(1), matcher.group(2)));
            }
        }
    }

    private static String function(String methodName, String params) {
        String value;
        try {
            if (methodName == null){
                Class clazz = Class.forName(Platform.EXTENDS_VARIABLE);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    methodMap.put(method.getName(), method);
                }
            }
            value = (String) methodMap.get(methodName).invoke(null, params);
        }catch (Exception e){
            throw new VariableException(StringUtil.getStackTrace(e));
        }
        return value;
    }

}