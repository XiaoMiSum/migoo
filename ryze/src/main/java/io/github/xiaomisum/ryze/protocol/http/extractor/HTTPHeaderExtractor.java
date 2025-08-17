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

package io.github.xiaomisum.ryze.protocol.http.extractor;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.component.extractor.JSONExtractor;
import io.github.xiaomisum.ryze.component.extractor.RegexExtractor;
import io.github.xiaomisum.ryze.core.TestStatus;
import io.github.xiaomisum.ryze.core.extractor.AbstractExtractor;
import io.github.xiaomisum.ryze.core.extractor.ExtractResult;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.http.RealHTTPRealResultResponse;

/**
 * HTTP 响应提取器，仅支持提取请求头中的数据，请求头存在多个时，默认返回第一个匹配的
 * <p>
 * 如需提取响应体中的JSON数据，请使用 {@link JSONExtractor}
 * 如需提取响应体中的HTML数据，请使用 {@link RegexExtractor}
 *
 * @author xiaomi
 */
@KW({"http_header_extractor", "http_header", "HTTPHeader", "http", "https"})
public class HTTPHeaderExtractor extends AbstractExtractor {

    @JSONField(name = MATCH_NUM)
    protected int matchNum;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected ExtractResult extract(SampleResult result) {
        var res = new ExtractResult("HTTP响应 提取: " + field);
        var response = (RealHTTPRealResultResponse) result.getResponse();
        Object value = null;
        var headers = response.headers().stream().filter(header -> header.getName().equalsIgnoreCase(field)).toList();
        if (!headers.isEmpty()) {
            matchNum = (headers.size() < matchNum + 1) ? headers.size() - 1 : matchNum;
            value = headers.get(matchNum).getValue();
            res.setValue(value);
        }
        if (value == null) {
            res.setStatus(TestStatus.failed);
            res.setMessage("响应头中不存在：" + field);
        }
        return res;
    }


    public static class Builder extends AbstractExtractor.Builder<Builder, HTTPHeaderExtractor> {

        public Builder() {
            super(new HTTPHeaderExtractor());
        }

        public Builder matchNum(int matchNum) {
            extractor.matchNum = matchNum;
            return self;
        }
    }
}