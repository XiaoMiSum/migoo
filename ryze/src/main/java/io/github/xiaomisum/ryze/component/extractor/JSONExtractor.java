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

package io.github.xiaomisum.ryze.component.extractor;

import com.alibaba.fastjson2.JSONPath;
import io.github.xiaomisum.ryze.core.TestStatus;
import io.github.xiaomisum.ryze.core.extractor.AbstractExtractor;
import io.github.xiaomisum.ryze.core.extractor.ExtractResult;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;

/**
 * @author xiaomi
 */
@KW({"JSONExtractor", "json_extractor", "json"})
public class JSONExtractor extends AbstractExtractor {

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected ExtractResult extract(SampleResult result) {
        var res = new ExtractResult("JSON 提取: " + field);
        var target = result.getResponse().bytesAsString();
        Object value = JSONPath.extract(target, field);
        res.setValue(value);
        if (value == null) {
            res.setStatus(TestStatus.failed);
            res.setMessage(String.format("目标字符串不存在 JsonPath %s，目标字符串：\n%s", field, target));
        }
        return res;
    }

    public static class Builder extends AbstractExtractor.Builder<Builder, JSONExtractor> {

        public Builder() {
            super(new JSONExtractor());
        }
    }
}