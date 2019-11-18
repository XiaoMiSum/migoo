package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.simplehttp.Response;
import xyz.migoo.report.MiGooLog;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2018/7/24 20:58
 */
public class Validator {

    private static Map<String, Method> methods = null;

    private Validator() {
    }

    public synchronized static void validation(Response response, JSONArray validate, JSONObject variables) throws AssertionFailure, ExecuteError {
        for (int i = 0; i < validate.size(); i++) {
            VariableHelper.evalValidate(validate.getJSONObject(i), variables);
            MiGooLog.log(String.format("check point  : %s", validate.getJSONObject(i).toJSONString()));
            AbstractAssertion assertion = AssertionFactory.getAssertion(validate.getJSONObject(i).getString(CaseKeys.VALIDATE_CHECK));
            assertion.setActual(response);
            boolean result = assertion.assertThat(validate.getJSONObject(i));
            MiGooLog.log(String.format("check result : %s", result));
            if (!result) {
                String check = validate.getJSONObject(i).getString(CaseKeys.VALIDATE_CHECK);
                String expected = validate.getJSONObject(i).getString(CaseKeys.VALIDATE_EXPECT);
                String actual = String.valueOf(assertion.getActual());
                String clazz = assertion.getClass().getSimpleName();
                String method = validate.getJSONObject(i).getString(CaseKeys.VALIDATE_TYPE);
                String msg = "Value expected(%s) to be '%s', but found '%s' \n" +
                        "Assertion class is '%s', assert method is '%s'";
                throw new AssertionFailure(String.format(msg, check, expected, actual, clazz, method));
            }
        }
    }
}
