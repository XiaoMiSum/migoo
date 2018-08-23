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

    public static void json(Response response, JSONObject validate) {
        // 1. 从 validate 获取 取值的 key
        String[] keys = validate.getString(Dict.VALIDATE_CHECK).split("\\.");
        Object value = "----";
        // 2. 通过 key 从 response 中 获取对应的值
        JSONObject json = response.json();
        for (int i = 0; i < keys.length; i++){
            if (keys.length >= 2 & i+2 < keys.length){
                json = json.getJSONObject(keys[i+1]);
            }else if (keys.length >= 2 & i+1 < keys.length){
                value = json.get(keys[i+1]);
            }
        }
        // 3. 设置 实际值到 validate 对象中
        validate.put(Dict.VALIDATE_ACTUAL, value);
    }

    public static void status(Response response, JSONObject validate) {
        validate.put(Dict.VALIDATE_ACTUAL, response.statusCode());
        validate.put(Dict.VALIDATE_TYPE, Dict.VALIDATE_TYPE_EQUALS);
    }

    public static boolean equals(Object actual, Object expect) {
        if(expect instanceof String && !(actual instanceof String)){
            actual = String.valueOf(actual);
        }
        return actual.equals(expect);
    }

    public static boolean contains(Object actual, Object expect) {
        return StringUtil.contains((String) actual, (String) expect);
    }

/*    public static void main(String[] args) {
        Response response = new Response();
        response.body("{" +
                "    \"status\": 200," +
                "    \"error_code\": 200," +
                "    \"data\": {" +
                "        \"withdrawalToken\": \"64fda979-5eb1-3943-11bd-b7bf14d00d4b13p5c36oeQIv9jzBjq12j0w2r9ClqcUK3dR7gttFpToG02TMLT1\"" +
                "   }" +
                "}");
        JSONObject json = JSONObject.parseObject("{\"check\": \"body.data.withdrawalToken\", \"expect\": 200, \"types\": \"eq\"}");

        json(response, json);
    }*/

}