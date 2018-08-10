package xyz.migoo.utils;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.VariableException;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 绑定变量
 *
 * @author xiaomi
 * @date 2018/08/02 14:53
 */
public class Variable {

    private static  final Pattern PATTERN = Pattern.compile("^\\$\\{(\\w+)\\(([\\$\\w[-]* =,]*)\\)\\}");

    private static Method[] methods = null;

    private Variable() {
    }

    public static void bindVariable(JSONObject variables, JSONObject body) {
        if (variables == null || body == null) {
            return;
        }
        bindVariableByJSONObject(variables, body);
    }

    /**
     * 绑定变量，从 variables 中取出 key，再通过 key 从 body 中取出 value
     * 当 value = $key 时，则认为需要将 body 的 value 替换为变量的 value
     *
     * @param variables 变量列表 JSONObject
     * @param body      需要被替换的 JSONObject
     */
    private static void bindVariableByJSONObject(JSONObject variables, JSONObject body) {
        for (String key : variables.keySet()) {
            String object = body.getString(key);
            if (StringUtil.isNotBlank(object)) {
                String variablesValue = variables.getString(key);
                Matcher matcher = PATTERN.matcher(variablesValue);
                if (matcher.find()){
                    body.put(key, function(matcher.group(1), matcher.group(2)));
                }else {
                    if (object.equals("$" + key)) {
                        body.put(key, variables.getString(key));
                    }
                }
            }
        }
    }

    private static String function(String methodName, String params) {
        String value = "";
        try {
            if(methods == null){
                Class clazz = Class.forName("xyz.migoo.test.utils.Tester");
                methods = clazz.getDeclaredMethods();
            }
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    value = (String) method.invoke(null, params);
                }
            }
        }catch (Exception e){
            throw new VariableException(StringUtil.getStackTrace(e));
        }
        return value;
    }

}
