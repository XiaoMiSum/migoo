package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import junit.framework.Assert;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.ValidatorException;
import xyz.migoo.http.Response;
import xyz.migoo.utils.Function;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;

import static xyz.migoo.config.Platform.*;

/**
 * @author xiaomi
 * @date 2018/7/24 20:58
 */
public class Validator extends Assert {

    private static final Log LOG = new Log(Validator.class);
    private static Map<String, Method> methodMap = null;
    private static String function;

    private Validator() {
    }

    public static void validation(Response response, JSON validate) throws ValidatorException {
        if (validate instanceof JSONObject) {
            validation(response, (JSONObject) validate);
        }
        if (validate instanceof JSONArray) {
            validation(response, (JSONArray) validate);
        }
    }

    private static void validation(Response response, JSONArray validate) throws ValidatorException {
        for (int i = 0; i < validate.size(); i++) {
            validation(response, validate.getJSONObject(i));
        }
    }

    private static void validation(Response response, JSONObject validate) throws ValidatorException {
        if (methodMap == null) {
            methodMap = Function.functionLoader();
        }
        evalValidate(response, validate);
        String types = validate.getString(CaseKeys.VALIDATE_TYPE);
        String check = validate.getString(CaseKeys.VALIDATE_CHECK);
        Object expect = validate.get(CaseKeys.VALIDATE_EXPECT);
        Object actual = validate.get(CaseKeys.VALIDATE_ACTUAL);
        LOG.info("check point  : " + validate.toJSONString());
        validation(check, types, actual, expect);
    }

    /**
     * 重新运算 求出参数 validate 的内容
     * 将 response 的 body 或 status 放到 validate.actual 中
     * @param response  响应信息
     * @param validate  检查点
     * @throws ValidatorException 检查异常
     */
    private static void evalValidate(Response response, JSONObject validate) throws ValidatorException {
        try {
            String check = validate.getString(CaseKeys.VALIDATE_CHECK);
            function(check);
            methodMap.get(function).invoke(null, response, validate);
            Function.evalExpect(validate);
        } catch (Exception e) {
            throw new ValidatorException(e.getMessage());
        }
    }

    private synchronized static void validation(String check, String types, Object actual, Object expect) throws ValidatorException {
        function(types);
        boolean result = false;
        try {
            result = (boolean) methodMap.get(function).invoke(null, actual, expect);
            LOG.info("check result : " + result);
        } catch (Exception e) {
            LOG.info("check result : " + result);
            throw new ValidatorException(StringUtil.getStackTrace(e));
        }
        if (!result) {
            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("check result: " + false + "\n")
                    .append("\tcheck point  : ").append(check).append("\n")
                    .append("\tcheck actual : ").append(actual).append("\n")
                    .append("\tcheck expect : ").append(expect).append("\n")
                    .append("\tcheck type   : ").append(types).append("\n");
            throw new ValidatorException(errorMsg.toString());
        }
    }

    private static void function(String searchChar){
        if (CHECK_BODY.contains(searchChar)) {
            function = CaseKeys.EVAL_ACTUAL_BY_BODY;
            return;
        }
        if (CHECK_CODE.contains(searchChar)) {
            function = CaseKeys.EVAL_ACTUAL_BY_STATUS;
            return;
        }
        if (isJson(searchChar)) {
            function = CaseKeys.EVAL_ACTUAL_BY_JSON;
            return;
        }
        if (FUNCTION_EQUALS.contains(searchChar)) {
            function = CaseKeys.VALIDATE_TYPE_EQUALS;
            return;
        }
        if (FUNCTION_NOT_EQUALS.contains(searchChar)) {
            function = CaseKeys.VALIDATE_TYPE_NOT_EQUALS;
            return;
        }
        if (FUNCTION_CONTAINS.contains(searchChar)) {
            function = CaseKeys.VALIDATE_TYPE_CONTAINS;
            return;
        }
        if (FUNCTION_NOT_CONTAINS.contains(searchChar)) {
            function = CaseKeys.VALIDATE_TYPE_NOT_CONTAINS;
            return;
        }
        if (FUNCTION_IS_EMPTY.contains(searchChar)) {
            function = CaseKeys.VALIDATE_TYPE_IS_EMPTY;
            return;
        }
        if (FUNCTION_IS_NOT_EMPTY.contains(searchChar)) {
            function = CaseKeys.VALIDATE_TYPE_IS_NOT_EMPTY;
            return;
        }
        if (FUNCTION_REGEX.contains(searchChar)) {
            function = CaseKeys.VALIDATE_TYPE_REGEX;
            return;
        }
        if (function == null){
            throw new ValidatorException("'function' is null");
        }
    }
}
