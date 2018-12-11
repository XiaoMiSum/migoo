package xyz.migoo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import xyz.migoo.config.Dict;
import xyz.migoo.config.Platform;
import xyz.migoo.http.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2018/7/28 10:50
 */
public class Function {

    private static Map<String, Method> methods = null;
    private static final String BODY_ = "body";
    private static final String JSON_ = "json";

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
        Matcher matcher = Variable.FUNC_PATTERN.matcher(value);
        if (matcher.find()) {
            if (methods == null) {
                methods = InvokeUtil.functionLoader(Platform.EXTENDS_VALIDATOR);
            }
            Object result = InvokeUtil.invoke(methods, matcher.group(1), matcher.group(2));
            validate.put(Dict.VALIDATE_EXPECT, result);
        }
    }

    public static void body(Response response, JSONObject validate) {
        JSON json = response.json();
        if (json != null) {
            validate.put(Dict.VALIDATE_ACTUAL, json);
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
        String path = validate.getString(Dict.VALIDATE_CHECK);
        if (path.toLowerCase().startsWith(JSON_) || path.toLowerCase().startsWith(BODY_)){
            path = "$" + path.substring(BODY_.length());
        }
        Object value = JSONPath.read(response.body(), path);
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

    public static boolean regex(Object actual, Object expect) {
        String str = "";
        if (actual instanceof JSON){
            str = ((JSON) actual).toJSONString();
        }
        if (actual instanceof String){
            str = actual.toString();
        }
        if (actual instanceof Number){
            str = String.valueOf(actual);
        }
        Pattern pattern = Pattern.compile(expect.toString());
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

}