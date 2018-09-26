package xyz.migoo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;
import xyz.migoo.config.Platform;
import xyz.migoo.http.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author xiaomi
 * @date 2018/7/28 10:50
 */
public class Function {

    private static Map<String, Method> methodMap = null;

    public static Map<String, Method> functionLoader() {
        Method[] methods = Function.class.getDeclaredMethods();
        Map<String, Method> map = new HashMap<>(methods.length);
        for (Method method : methods){
            map.put(method.getName(), method);
        }
        return map;
    }

    public static void evalExpect(JSONObject validate) throws Exception{
        String value = validate.getString(Dict.VALIDATE_EXPECT);
        if (StringUtil.isEmpty(value)){
            return;
        }
        Matcher matcher = Variable.PATTERN.matcher(value);
        if (matcher.find()) {
            if (methodMap == null) {
                Class clazz = Class.forName(Platform.EXTENDS_VALIDATOR);

                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    methodMap.put(method.getName(), method);
                }
            }
            methodMap.get(matcher.group(1)).invoke(null, validate, matcher.group(2));
        }
    }

    public static void body(Response response, JSONObject validate) {
        if (response.json() != null) {
            validate.put(Dict.VALIDATE_ACTUAL, response.json());
            Object expect = validate.get(Dict.VALIDATE_EXPECT);
            // 如果不是 json 对象 则解析成 json 对象
            if (!(expect instanceof JSON)) {
                try {
                    validate.put(Dict.VALIDATE_EXPECT, JSON.parseObject((String) expect));
                } catch (Exception e) {
                    validate.put(Dict.VALIDATE_EXPECT, JSON.parseArray((String) expect));
                }
            }
        } else {
            validate.put(Dict.VALIDATE_ACTUAL, response.body());
        }
    }

    public static void json(Response response, JSONObject validate) {
        // 1. 从 validate 获取 取值的 key
        String[] keys = validate.getString(Dict.VALIDATE_CHECK).split("\\.");
        Object value = "----";
        // 2. 通过 key 从 response 中 获取对应的值
        JSONObject json = response.json();
        for (int i = 0; i < keys.length; i++) {
            // keys.length >= 2 代表是 多层嵌套 json
            // 当 i + 2 > keys.length 时 说明 是多层嵌套 json 的最后一层 json 对象
            if (keys.length >= 2 && i + 2 < keys.length) {
                json = json.getJSONObject(keys[i + 1]);
            // 当 i + 1 > keys.length 时 从 最后一层 json 对象中 获取 key值
            } else if (keys.length >= 2 && i + 1 < keys.length) {
                value = json.get(keys[i + 1]);
            }
        }
        // 3. 设置 实际值到 validate 对象中
        validate.put(Dict.VALIDATE_ACTUAL, value);
    }

    public static void html(Response response, JSONObject validate) {
        // 1. 从 validate 获取 取值的 key
        String[] keys = validate.getString(Dict.VALIDATE_CHECK).split("\\.");
        String[] select = new String[keys.length - 1];
        for (int i = 0; i < select.length; i++) {
            if (select.length >= 2 & i + 1 <= select.length) {
                select[i] = keys[i + 1];
            }
        }
        // 2. 通过 key 从 response.body (html) 中 获取对应的值
        String value = HtmlUtil.parse(response.body(), select);
        // 3. 设置 实际值到 validate 对象中
        validate.put(Dict.VALIDATE_ACTUAL, value);
    }

    public static void status(Response response, JSONObject validate) {
        validate.put(Dict.VALIDATE_ACTUAL, response.statusCode());
        validate.put(Dict.VALIDATE_TYPE, Dict.VALIDATE_TYPE_IS_EMPTY);
    }

    public static boolean equals(Object actual, Object expect) {
        if (expect instanceof String && !(actual instanceof String)) {
            actual = String.valueOf(actual);
        }
        return actual.equals(expect);
    }

    public static boolean contains(Object actual, Object expect) {
        if (actual instanceof String) {
            return StringUtil.contains((String) actual, (String) expect);
        }
        if (actual instanceof Map) {
            Map map = (Map) actual;
            return map.containsKey(expect) || map.containsValue(expect);
        }
        if (actual instanceof List) {
            return ((List)actual).contains(expect);
        }
        return false;
    }

    public static boolean isEmpty(Object actual, Object expect) {
        if (actual instanceof JSONObject) {
            return ((JSONObject) actual).isEmpty();
        }
        if (actual instanceof JSONArray) {
            return ((JSONArray) actual).isEmpty();
        }
        if (actual instanceof String) {
            return StringUtil.isBlank((String) actual);
        }
        return actual == null;
    }

    public static boolean isNotEmpty(Object actual, Object expect) {
        return !isEmpty(actual, expect);
    }

}