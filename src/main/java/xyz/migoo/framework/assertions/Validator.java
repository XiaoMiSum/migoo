/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


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
