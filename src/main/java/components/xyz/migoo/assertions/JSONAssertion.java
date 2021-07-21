/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package components.xyz.migoo.assertions;

import com.alibaba.fastjson.JSONPath;
import core.xyz.migoo.assertions.AbstractAssertion;
import core.xyz.migoo.assertions.AssertionResult;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.Alias;

/**
 * @author xiaomi
 */
@Alias(aliasList = {"JSONAssertion", "json_assertion"})
public class JSONAssertion extends AbstractAssertion {

    @Override
    public AssertionResult getResult(SampleResult samplerResult) {
        AssertionResult result = new AssertionResult("JSONAssertion");
        try {
            String jsonStr = samplerResult.getResponseDataAsString();
            setActual(JSONPath.read(jsonStr, getPropertyAsString(FIELD)));
            super.assertThat(result);
        } catch (Exception e) {
            result.setFailureMessage(e);
        }
        return result;
    }
}