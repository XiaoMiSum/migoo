package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.TestCase;
import xyz.migoo.framework.entity.Validate;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.simplehttp.Response;
import xyz.migoo.report.MiGooLog;

import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/24 20:58
 */
public class Validator {

    private Validator() {
    }

    public synchronized static void validation(TestCase testCase, List<Validate> validates) throws AssertionFailure, ExecuteError {
        validates.forEach(validate -> {
            VariableHelper.evalValidate(validate, testCase.variables());
            MiGooLog.log(String.format("check point  : %s, func: %s, expect: %s", validate.getCheck(),
                    validate.getFunc(), validate.getExpect()));
            AbstractAssertion assertion = AssertionFactory.getAssertion(validate.getCheck());
            assertion.setActual(testCase.response());
            boolean result = assertion.assertThat(JSONObject.parseObject(validate.toString()));
            validate.setActual(assertion.getActual());
            MiGooLog.log(String.format("check result : %s", result));
            if (!result) {
                validate.setResult("failure");
                testCase.hasFailure();
            } else {
                validate.setResult("success");
            }
        });
    }
}
