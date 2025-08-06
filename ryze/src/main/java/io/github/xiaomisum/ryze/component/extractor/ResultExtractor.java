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

import io.github.xiaomisum.ryze.core.extractor.AbstractExtractor;
import io.github.xiaomisum.ryze.core.extractor.ExtractResult;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.support.ValidateResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 */
@KW({"ResultExtractor", "result_extractor", "result"})
public class ResultExtractor extends AbstractExtractor {

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected ExtractResult extract(SampleResult result) {
        var res = new ExtractResult("Result 提取");
        res.setValue(result.getResponse().bytesAsString());
        return res;
    }

    @Override
    public ValidateResult validate() {
        ValidateResult result = new ValidateResult();
        if (StringUtils.isBlank(refName)) {
            result.append("\n提取变量引用名称 %s 字段值缺失或为空，当前值：%s", REF_NAME, toString());
        }
        return result;
    }

    public static class Builder extends AbstractExtractor.Builder<ResultExtractor.Builder, ResultExtractor> {

        public Builder() {
            super(new ResultExtractor());
        }
    }
}
