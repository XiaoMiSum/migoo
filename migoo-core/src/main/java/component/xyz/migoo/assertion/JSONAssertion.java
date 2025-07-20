/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package component.xyz.migoo.assertion;

import com.alibaba.fastjson2.JSONPath;
import core.xyz.migoo.assertion.AbstractAssertion;
import core.xyz.migoo.assertion.AssertionResult;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.ValidateResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 */
@Alias({"JSONAssertion", "json_assertion", "json"})
public class JSONAssertion extends AbstractAssertion {

    @Override
    protected AssertionResult initialized(SampleResult<? extends SampleResult<?>> result) {
        var res = new AssertionResult("JSON断言: " + field);
        var target = result.getResponseDataAsString();
        try {
            actualValue = JSONPath.extract(target, field);
        } catch (Exception e) {
            actualValue = null;
        }
        return res;
    }

    @Override
    public ValidateResult validate() {
        ValidateResult result = new ValidateResult();
        if (StringUtils.isBlank(field)) {
            result.append("\n提取表达式 %s 字段值缺失或为空，当前值：%s", field, toString());
        }
        return result;
    }


}