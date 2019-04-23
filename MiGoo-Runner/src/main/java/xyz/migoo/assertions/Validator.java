package xyz.migoo.assertions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.Assert;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.AssertionException;
import xyz.migoo.http.Response;
import xyz.migoo.utils.InvokeUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

import static xyz.migoo.utils.Variable.FUNC_PATTERN;

/**
 * @author xiaomi
 * @date 2018/7/24 20:58
 */
public class Validator extends Assert {

    private static final Log LOG = new Log(Validator.class);
    static Map<String, Method> methods = null;

    private Validator() {
    }

    public static void validation(Response response, JSON validate) throws AssertionException {
        if (validate instanceof JSONObject) {
            validation(response, (JSONObject) validate);
        }
        if (validate instanceof JSONArray) {
            validation(response, (JSONArray) validate);
        }
    }

    private static void validation(Response response, JSONArray validate) throws AssertionException {
        for (int i = 0; i < validate.size(); i++) {
            validation(response, validate.getJSONObject(i));
        }
    }

    /**
     * 执行 validate.expect 指定的方法，将值put到 validate 对象中
     * @param validate  检查点
     * @throws AssertionException 检查异常
     */
    private static void evalValidate(JSONObject validate) throws AssertionException {
        try {
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
        } catch (Exception e) {
            throw new AssertionException(e.getMessage());
        }
    }

    private synchronized static void validation(Response response, JSONObject validate) throws AssertionException {
        evalValidate(validate);
        LOG.info("check point  : " + validate.toJSONString());
        Boolean result = false;
        AbstractAssertion assertion = AssertionFactory.getAssertion(validate.getString(CaseKeys.VALIDATE_CHECK));
        assertion.setActual(response);
        try {
            result = Objects.requireNonNull(assertion.assertThat(validate));
            LOG.info("check result : " + result);
        } catch (Exception e) {
            LOG.error("check result : " + result, e);
            throw new AssertionException(e.getMessage());
        }
        if (!result) {
            String check = validate.getString(CaseKeys.VALIDATE_CHECK);
            String expected = validate.getString(CaseKeys.VALIDATE_EXPECT);
            String actual = String.valueOf(assertion.getActual());
            String clazz = assertion.getClass().getName();
            String method = validate.getString(CaseKeys.VALIDATE_TYPE);
            String msg="Value expected(%s) to be '%s', but found '%s' \n" +
                    "Assertion class is '%s', assert method is %s";
            throw new AssertionException(String.format(msg, check
                    , expected, actual, clazz, method));
        }
    }
}
