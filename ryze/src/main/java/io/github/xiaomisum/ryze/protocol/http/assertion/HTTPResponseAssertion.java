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

package io.github.xiaomisum.ryze.protocol.http.assertion;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import io.github.xiaomisum.ryze.core.assertion.AbstractAssertion;
import io.github.xiaomisum.ryze.core.assertion.AssertionResult;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.http.RealHTTPRealResultResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
@KW({"HTTPAssertion", "HTTP_Assertion", "http", "https"})
public class HTTPResponseAssertion extends AbstractAssertion {

    private static final List<String> STATUS = Arrays.asList("line", "status", "code", "statuscode", "statusline", "status_code", "status_line");
    private static final String BODY = "body";
    private static final Pattern PATTERN = Pattern.compile("^header(\\[\\d+])?(\\.\\w+.?)?");

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected AssertionResult initialized(SampleResult result) {
        var res = new AssertionResult("HTTP响应断言: ");
        var response = (RealHTTPRealResultResponse) result.getResponse();
        field = StringUtils.isBlank(field) ? BODY : field;
        var matcher = PATTERN.matcher(field);
        if (matcher.find()) {
            var path = "$" + (matcher.group(1) == null ? "[0]" : matcher.group(1)) + matcher.group(2);
            actualValue = JSONPath.extract(JSON.toJSONString(response.headers()), path);
        } else {
            actualValue = STATUS.contains(field.toLowerCase()) ? response.statusCode() : response.bytesAsString();
        }
        return res;
    }


    public static class Builder extends AbstractAssertion.Builder<Builder, HTTPResponseAssertion> {

        public Builder() {
            super(new HTTPResponseAssertion());
        }
    }
}