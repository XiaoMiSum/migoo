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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import core.xyz.migoo.assertion.AbstractAssertion;
import core.xyz.migoo.assertion.VerifyResult;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.Alias;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
@Alias({"HTTPAssertion", "HTTP_Assertion"})
public class HTTPResponseAssertion extends AbstractAssertion {

    private static final List<String> STATUS = Arrays.asList("line", "status", "code", "statuscode", "statusline", "status_code", "status_line");

    private static final String CONTEXT = "context";
    private static final Pattern PATTERN = Pattern.compile("^header(\\[\\d+])?(\\.\\w+.?)?");


    @Override
    public VerifyResult getResult(SampleResult samplerResult) {
        var result = new VerifyResult(this.getClass());
        if (samplerResult instanceof HTTPSampleResult httpResult) {
            var field = get(FIELD) == null ? CONTEXT : getPropertyAsString(FIELD).toLowerCase();
            getProperty().put(FIELD, field);
            var matcher = PATTERN.matcher(field);
            if (matcher.find()) {
                var path = "$" + (matcher.group(1) == null ? "[0]" : matcher.group(1)) + matcher.group(2);
                setActual(JSONPath.extract(JSON.toJSONString(httpResult.getResponseHeaders()), path));
            } else {
                setActual(STATUS.contains(field) ? httpResult.getResponseCode() : httpResult.getResponseDataAsString());
            }
            super.assertThat(result);
        } else {
            result.setSuccessful(true);
            setContent(result, String.format("HTTPResponseAssertion unsupported %s, assert default true", samplerResult.getClass().getSimpleName()));
        }
        return result;
    }
}