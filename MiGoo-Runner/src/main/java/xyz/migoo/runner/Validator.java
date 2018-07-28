package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.Assert;
import xyz.migoo.exception.ValidatorException;
import xyz.migoo.http.Response;
import xyz.migoo.utils.StringUtil;

/**
 * @author xiaomi
 * @date 2018/7/24 20:58
 */
public class Validator extends Assert {

    private Validator() {
    }

    public static void validation(Response response, JSON validate) throws ValidatorException {
        if (validate instanceof JSONObject) {
            validation(response,(JSONObject) validate);
        }
        if (validate instanceof JSONArray) {
            validation(response,(JSONArray) validate);
        }

    }

    private static void validation(Response response, JSONObject validate) throws ValidatorException {
        evalValidate(response, validate);
        String types = validate.getString("types").toLowerCase();
        String actual = validate.getString("actual");
        String expect = validate.getString("expect");
        String check = validate.getString("check");
        validation(check, types, actual, expect);
    }

    private static void validation(Response response, JSONArray validate) throws ValidatorException {
        for (int i = 0; i < validate.size(); i++) {
            validation(response, validate.getJSONObject(i));
        }
    }

    private synchronized static void validation(String check, String types, String actual, String expect) throws ValidatorException {
        boolean equals = "==".equals(types) || "eq".equals(types) || "equal".equals(types)
                || "equals".equals(types) || "is".equals(types);
        boolean contain = "contain".equals(types) || "contains".equals(types) || "ct".equals(types);
        boolean result = false;
        if (equals){
            result = equals(actual, expect);
        }
        if (contain){
            result = contain(actual, expect);
        }
        if (!result){
            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("check result: " + result + "\n")
                    .append("\tcheck point: ").append(check).append("\n")
                    .append("\tcheck actual: ").append(actual).append("\n")
                    .append("\tcheck expect: ").append(expect).append("\n")
                    .append("\tcheck type: ").append(types).append("\n");
            throw new ValidatorException(errorMsg.toString());
        }
    }

    private static boolean equals(String actual, String expect){
        return actual.equals(expect);
    }

    private static boolean contain(String actual, String expect){
        return StringUtil.contains(actual, expect);
    }

    private static void evalValidate(Response response, JSONObject validate){
        String check = validate.getString("check");
        boolean body = "body".equals(check) || "content".equals(check);
        if (body){
            validate.put("actual", response.body());
        }
        boolean code = "statusCode".equals(check) || "status".equals(check) || "code".equals(check);
        if (code){
            validate.put("actual", response.statusCode());
            validate.put("types", "equals");
        }
    }
}
