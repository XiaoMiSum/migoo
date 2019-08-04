package xyz.migoo.assertions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.Assert;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.http.Response;
import xyz.migoo.utils.InvokeUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;

import static xyz.migoo.parser.BindVariable.FUNC_PATTERN;

/**
 * @author xiaomi
 * @date 2018/7/24 20:58
 */
public class Validator extends Assert {

    private static final Log LOG = new Log(Validator.class);
    static Map<String, Method> methods = null;

    private Validator() {
    }

    public static void validation(Response response, JSON validate, JSONObject variables) throws AssertionFailure, ExecuteError {
        if (validate instanceof JSONObject) {
            validation(response, (JSONObject) validate, variables);
        }
        if (validate instanceof JSONArray) {
            validation(response, (JSONArray) validate, variables);
        }
    }

    private static void validation(Response response, JSONArray validate, JSONObject variables) throws AssertionFailure, ExecuteError {
        for (int i = 0; i < validate.size(); i++) {
            validation(response, validate.getJSONObject(i), variables);
        }
    }

    /**
     * 执行 validate.expect 指定的方法，将值put到 validate 对象中
     *
     * @param validate 检查点
     * @throws AssertionFailure 检查异常
     */
    private static void evalValidate(JSONObject validate, JSONObject variables) throws ExecuteError {
        try {
            String value = validate.getString(CaseKeys.VALIDATE_EXPECT);
            if (StringUtil.isEmpty(value)) {
                return;
            }
            Matcher func = FUNC_PATTERN.matcher(value);
            if (func.find()) {
                if (methods == null) {
                    methods = InvokeUtil.functionLoader(Platform.EXTENDS_VALIDATOR);
                }
                Object result = InvokeUtil.invoke(methods, func.group(1), func.group(2), variables);
                validate.put(CaseKeys.VALIDATE_EXPECT, result);
            }
        } catch (Exception e) {
            throw new ExecuteError(e.getMessage(), e);
        }
    }

    private synchronized static void validation(Response response, JSONObject validate, JSONObject variables) throws AssertionFailure, ExecuteError {
        evalValidate(validate, variables);
        LOG.info(String.format("check point  : %s", validate.toJSONString()));
        AbstractAssertion assertion = AssertionFactory.getAssertion(validate.getString(CaseKeys.VALIDATE_CHECK));
        assertion.setActual(response);
        boolean result = assertion.assertThat(validate);
        LOG.info(String.format("check result : %s", result));
        if (!result) {
            String check = validate.getString(CaseKeys.VALIDATE_CHECK);
            String expected = validate.getString(CaseKeys.VALIDATE_EXPECT);
            String actual = String.valueOf(assertion.getActual());
            String clazz = assertion.getClass().getName();
            String method = validate.getString(CaseKeys.VALIDATE_TYPE);
            String msg = "Value expected(%s) to be '%s', but found '%s' \n" +
                    "Assertion class is '%s', assert method is '%s'";
            throw new AssertionFailure(String.format(msg, check, expected, actual, clazz, method));
        }
    }
}
