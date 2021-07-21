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
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
@Alias(aliasList = {"HTTPAssertion", "HTTP_Assertion"})
public class HTTPResponseAssertion extends AbstractAssertion {

    private static final List<String> STATUS = Arrays.asList("line", "status", "code", "statuscode", "statusline", "status_code", "status_line");

    private static final String CONTEXT = "context";
    private static final Pattern PATTERN = Pattern.compile("^header(\\[[\\d]+])?(\\.\\w+)?");

    @Override
    public AssertionResult getResult(SampleResult samplerResult) {
        AssertionResult result = new AssertionResult("HTTPAssertion");
        if (samplerResult instanceof HTTPSampleResult) {
            String field = get(FIELD) == null ? CONTEXT : getPropertyAsString(FIELD).toLowerCase();
            HTTPSampleResult httpResult = (HTTPSampleResult) samplerResult;
            Matcher matcher = PATTERN.matcher(field);
            if (matcher.find()) {
                String path = "$" + (matcher.group(1) == null ? "[0]" : matcher.group(1)) + matcher.group(2);
                setActual(JSONPath.read(((HTTPSampleResult) samplerResult).getResponseHeaders(), path));
            } else {
                setActual(STATUS.contains(field) ? httpResult.getResponseCode() : samplerResult.getResponseDataAsString());
            }
            super.assertThat(result);
        } else {
            result.setSuccessful(true);
            result.setContext(String.format("UnSupport %s, assert default true", samplerResult.getClass().getSimpleName()));
        }
        return result;
    }
}