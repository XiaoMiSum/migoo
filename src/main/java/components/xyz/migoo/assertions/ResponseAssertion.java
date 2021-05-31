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

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.assertions.AbstractAssertion;
import core.xyz.migoo.assertions.AssertionResult;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.Alias;

import java.util.Arrays;
import java.util.List;

@Alias(aliasList = {"ResponseAssertion", "Response_Assertion"})
public class ResponseAssertion extends AbstractAssertion {

    private static final List<String> STATUS = Arrays.asList("line", "status", "code", "statuscode", "statusline", "status_code", "status_line");

    private static final String HEADER = "header.";
    private static final String CONTEXT = "context";

    @Override
    public AssertionResult getResult(SampleResult samplerResult) {
        AssertionResult result = new AssertionResult("ResponseAssertion");
        try {
            String field = get(FIELD) == null ? CONTEXT : getPropertyAsString(FIELD).toLowerCase();
            if (STATUS.contains(field)) {
                setActual(samplerResult.getResponseCode());
            } else if (field.startsWith(HEADER)) {
                JSONObject headers = JSONObject.parseObject(samplerResult.getResponseHeaders());
                String attribute = field.substring(field.indexOf(".") + 1).toLowerCase();
                for (String key : headers.keySet()) {
                    if (key.toLowerCase().equals(attribute)) {
                        setActual(headers.getString(attribute));
                        break;
                    }
                }
            } else {
                setActual(samplerResult.getResponseDataAsString());
            }
            super.assertThat(result);
        } catch (Exception e) {
            result.setError(true);
            result.setFailureMessage(e);
        }
        return result;
    }
}