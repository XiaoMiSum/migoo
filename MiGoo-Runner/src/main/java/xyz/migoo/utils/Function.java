package xyz.migoo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.config.Platform;
import xyz.migoo.http.Response;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.migoo.utils.Variable.FUNC_PATTERN;

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
        String value = validate.getString(CaseKeys.VALIDATE_EXPECT);
        if (StringUtil.isEmpty(value)){
            return;
        }
        Matcher matcher = FUNC_PATTERN.matcher(value);
        if (matcher.find()) {
            if (methods == null) {
                methods = InvokeUtil.functionLoader(Platform.EXTENDS_VALIDATOR);
            }
            Object result = InvokeUtil.invoke(methods, matcher.group(1), matcher.group(2));
            validate.put(CaseKeys.VALIDATE_EXPECT, result);
        }
    }

    /**
     * 1.将整个 response.body 放到 validate.actual
     * 2.如果 response.body 是个 json 对象，且 validate.expect 不是 json对象
     * 则 将 validate.expect 解析成 json 对象后重新放入 validate.expect
     *
     * @param response  响应信息
     * @param validate  检查点
     */
    public static void body(Response response, JSONObject validate) {
        JSON json = response.json();
        if (json != null) {
            validate.put(CaseKeys.VALIDATE_ACTUAL, json);
        } else {
            validate.put(CaseKeys.VALIDATE_ACTUAL, response.body());
        }
    }

    /**
     * 支持 body.code 、 body.data.id 方式获取  response.body 的 json 的 key
     * 将获取到的 value 放到 validate.actual
     *
     * @param response   响应信息
     * @param validate   检查点
     */
    public static void json(Response response, JSONObject validate) {
        String path = validate.getString(CaseKeys.VALIDATE_CHECK);
        if (path.startsWith(JSON_) || path.startsWith(BODY_)){
            path = "$" + path.substring(BODY_.length());
        }
        Object value = JSONPath.read(response.body(), path);
        validate.put(CaseKeys.VALIDATE_ACTUAL, value);
    }

    /**
     * 将 response.statusCode 放到 validate.actual
     *
     * @param response  响应信息
     * @param validate  检查点
     */
    public static void status(Response response, JSONObject validate) {
        validate.put(CaseKeys.VALIDATE_ACTUAL, response.statusCode());
    }

    /**
     * 验证  actual == expect
     *
     * @param actual  实际值
     * @param expect  期望值
     * @return  验证结果 true / false
     */
    public static boolean equals(Object actual, Object expect) {
        if(expect instanceof String || actual instanceof String){
            String s1 = String.valueOf(expect);
            String s2 = String.valueOf(actual);
            return s2.equalsIgnoreCase(s1);
        }
        if (expect instanceof JSON){
            return actual.equals(expect);
        }
        return actual == expect || actual.equals(expect);
    }

    public static boolean not(Object actual, Object expect){
        return !equals(actual, expect);
    }

    /**
     * 验证 actual.contains(expect)
     * 只支持 string \ json
     *
     * @param actual  实际值
     * @param expect  期望值
     * @return  验证结果 true / false
     */
    public static boolean contains(Object actual, Object expect) {
        if (actual instanceof String) {
            return StringUtil.contains((String) actual, (String) expect);
        }
        if (actual instanceof Map) {
            Map json = (Map) actual;
            return json.containsValue(expect) || json.containsKey(expect);
        }
        if (actual instanceof List) {
            return ((List) actual).contains(expect);
        }
        return false;
    }

    public static boolean notContains(Object actual, Object expect){
        return !contains(actual, expect);
    }

    /**
     * 验证 actual 是 空对象 或 空字符串
     *
     * @param actual  实际值
     * @param expect  期望值（未使用，可为任意对象）
     * @return  验证结果 true / false
     */
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

    /**
     * 验证 actual 不是 空对象 或 空字符串
     *
     * @param actual  实际值
     * @param expect  期望值（未使用，可为任意对象）
     * @return  验证结果 true / false
     */
    public static boolean isNotEmpty(Object actual, Object expect) {
        return !isEmpty(actual, expect);
    }

    /**
     * 正则表达式 验证器
     * @param actual   待验证的实际值
     * @param expect   正则表达式
     * @return 验证结果 true / false
     */
    public static boolean regex(Object actual, Object expect) {
        String str = "";
        if (actual instanceof JSON){
            str = ((JSON) actual).toJSONString();
        }
        if (actual instanceof Number){
            str = String.valueOf(actual);
        }
        if (actual instanceof String){
            str = actual.toString();
        }
        Pattern pattern = Pattern.compile(expect.toString());
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
