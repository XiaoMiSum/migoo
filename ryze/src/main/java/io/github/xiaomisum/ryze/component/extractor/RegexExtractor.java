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

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.TestStatus;
import io.github.xiaomisum.ryze.core.extractor.AbstractExtractor;
import io.github.xiaomisum.ryze.core.extractor.ExtractResult;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
@KW({"RegexExtractor", "regex_extractor", "regex"})
public class RegexExtractor extends AbstractExtractor {

    @JSONField(name = MATCH_NUM)
    protected int matchNum;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected ExtractResult extract(SampleResult result) {
        var res = new ExtractResult("正则表达式 提取: %s ，匹配第 %s 个".formatted(field, matchNum + 1));
        var target = result.getResponse().bytesAsString();
        var value = "";

        var pattern = Pattern.compile(field);
        var matcher = pattern.matcher(target);
        matchNum = Math.max(0, matchNum);
        int state = 0;
        while (state > -1 && matcher.find()) {
            state = matcher.groupCount() > 0 ? matchNum : state;
            value = matcher.group(1);
            state--;
        }

        if (StringUtils.isBlank(value)) {
            res.setStatus(TestStatus.failed);
            res.setMessage(String.format("目标字符串没有匹配的数据 %s，目标字符串：\n%s", field, target));
        }
        return res;
    }

    public int getMatchNum() {
        return matchNum;
    }

    public void setMatchNum(int matchNum) {
        this.matchNum = matchNum;
    }

    public static class Builder extends AbstractExtractor.Builder<Builder, RegexExtractor> {

        public Builder() {
            super(new RegexExtractor());
        }

        public Builder matchNum(int matchNum) {
            extractor.matchNum = matchNum;
            return self;
        }
    }
}