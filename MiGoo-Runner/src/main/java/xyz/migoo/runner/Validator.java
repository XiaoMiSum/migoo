package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.Assert;
import xyz.migoo.exception.ValidatorException;
import xyz.migoo.http.Response;
import xyz.migoo.utils.Function;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import static xyz.migoo.config.Platform.CHECK_BODY;
import static xyz.migoo.config.Platform.CHECK_CODE;

/**
 * @author xiaomi
 * @date 2018/7/24 20:58
 */
public class Validator extends Assert {

    private static Log log = new Log(Validator.class);

    private Validator() {
    }

    public static void validation(Response response, JSON validate) throws ValidatorException {
        if (response.isError()){
            throw new ValidatorException("request error. please check network .\n"
                    + "\t" +  response.error());
        }

        if (response.isNotFound()){
            throw new ValidatorException("error 404. please check url: \n"
                    + "\t" + response.request().url());
        }
        if (validate instanceof JSONObject) {
            validation(response,(JSONObject) validate);
        }
        if (validate instanceof JSONArray) {
            validation(response,(JSONArray) validate);
        }

    }

    private static void validation(Response response, JSONArray validate) throws ValidatorException {
        for (int i = 0; i < validate.size(); i++) {
            validation(response, validate.getJSONObject(i));
        }
    }

    private static void validation(Response response, JSONObject validate) throws ValidatorException {
        try {
            String types = validate.getString("types").toLowerCase();
            evalValidate(response, validate);
            String actual = validate.getString("actual").toLowerCase();
            String expect = validate.getString("expect").toLowerCase();
            String check = validate.getString("check");
            validation(check, types, actual, expect);
        }catch (Exception e){
            throw new ValidatorException(StringUtil.getStackTrace(e));
        }

    }

    private static void evalValidate(Response response, JSONObject validate){
        String check = validate.getString("check");
        if (CHECK_BODY.contains(check)){
            validate.put("actual", response.body());
        }
        if (CHECK_CODE.contains(check)){
            validate.put("actual", response.statusCode());
            validate.put("types", "equals");
        }
    }

    private synchronized static void validation(String check, String types, String actual, String expect) throws ValidatorException {
        boolean result ;
        try {
            Function.functionLoader(types);
            result = Function.validation(actual,expect);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ValidatorException(e.getMessage());
        }
        if (!result){
            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("check result: " + false + "\n")
                    .append("\tcheck point: ").append(check).append("\n")
                    .append("\tcheck actual: ").append(actual).append("\n")
                    .append("\tcheck expect: ").append(expect).append("\n")
                    .append("\tcheck type: ").append(types).append("\n");
            throw new ValidatorException(errorMsg.toString());
        }
    }
}
