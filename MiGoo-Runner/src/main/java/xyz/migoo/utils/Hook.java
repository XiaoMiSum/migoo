package xyz.migoo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

import static xyz.migoo.utils.Variable.PATTERN;

/**
 *
 * @author xiaomi
 * @date 2018/08/02 14:53
 */
public class Hook {

    private static Method[] methods = null;

    private Hook() {
    }

    /**
     *
     * @param variables  config  或 case
     * @param key      需要被替换的 JSONObject
     */
    public static void hook(JSONObject variables, String key) {
        JSONArray json = variables.getJSONArray(key);
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
            if (methods == null){
                Class clazz = Class.forName("xyz.migoo.test.utils.Function");
                methods = clazz.getDeclaredMethods();
            }
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    method.invoke(null, params);
                }
            }
        }catch (Exception e){
            // 不处理异常
        }
    }

}