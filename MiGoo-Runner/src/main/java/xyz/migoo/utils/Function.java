package xyz.migoo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;
import xyz.migoo.http.Response;

import java.lang.reflect.Method;

/**
 * @author xiaomi
 * @date 2018/7/28 10:50
 */
public class Function {

    public static Method[] functionLoader() {
        Method[] methods = Function.class.getDeclaredMethods();
        return methods;
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

    public static void status(Response response, JSONObject validate) {
        validate.put(Dict.VALIDATE_ACTUAL, response.statusCode());
        validate.put(Dict.VALIDATE_TYPE, Dict.VALIDATE_TYPE_EQUALS);
    }

    public static boolean equals(Object actual, Object expect) {
        return actual.equals(expect);
    }

    public static boolean contains(Object actual, Object expect) {
        return StringUtil.contains((String) actual, (String) expect);
    }


}