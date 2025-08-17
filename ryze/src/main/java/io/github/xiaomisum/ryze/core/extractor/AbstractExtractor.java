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

package io.github.xiaomisum.ryze.core.extractor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.TestStatus;
import io.github.xiaomisum.ryze.core.builder.IBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.support.ValidateResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractExtractor implements Extractor, ExtractorConstantsInterface {

    @JSONField(name = FIELD)
    protected String field;

    @JSONField(name = DEFAULT_VALUE)
    protected Object defaultValue;

    @JSONField(name = REF_NAME)
    protected String refName;


    public AbstractExtractor() {
    }

    @Override
    public ValidateResult validate() {
        ValidateResult result = new ValidateResult();
        if (StringUtils.isBlank(refName)) {
            result.append("\n提取引用名称 %s 字段值缺失或为空，当前值：%s", REF_NAME, toString());
        }
        if (StringUtils.isBlank(field)) {
            result.append("\n提取表达式 %s 字段值缺失或为空，当前值：%s", field, toString());
        }
        return result;
    }

    @Override
    public void process(ContextWrapper context) {
        if (!context.getTestResult().getStatus().isPassed()) {
            // 非 ryze-testng 框架下测试失败不会抛出异常，取样步骤结果失败，无需执行验证器
            return;
        }
        if (context.getTestResult() instanceof SampleResult result) {
            try {
                var res = StringUtils.isBlank(result.getResponse().bytesAsString()) ? broken() : extract(result);
                if (TestStatus.passed.equals(res.getStatus())) {
                    context.getLocalVariablesWrapper().put(refName, res.getValue());
                    return;
                }
                if (TestStatus.failed.equals(res.getStatus()) && defaultValue != null) {
                    res.setStatus(TestStatus.passed);
                    context.getLocalVariablesWrapper().put(refName, defaultValue);
                }
                if (res.getStatus().isBroken() || res.getStatus().isFailed()) {
                    throw new RuntimeException(res.getMessage(), res.getException());
                }
                return;
            } catch (Exception e) {
                if (defaultValue != null) {
                    // 提取发生异常，且设置了默认值，则使用默认值
                    context.getLocalVariablesWrapper().put(refName, defaultValue);
                    return;
                }
                throw new RuntimeException(e);
            }

        }
        throw new RuntimeException("不支持提取的测试组件: " + context.getTestElement().getClass());
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    private ExtractResult broken() {
        var res = new ExtractResult(getClass().getSimpleName());
        res.setStatus(TestStatus.broken);
        res.setMessage("待提取的字符串为 null 或空白");
        return res;
    }

    protected abstract ExtractResult extract(SampleResult context);

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }


    /**
     * 验证器基础构建器
     *
     * @param <SELF> 构建类自身
     */
    public static abstract class Builder<SELF extends Builder<SELF, EXTRACTOR>, EXTRACTOR extends AbstractExtractor>
            implements IBuilder<EXTRACTOR> {
        protected final EXTRACTOR extractor;
        protected SELF self;


        protected Builder(EXTRACTOR extractor) {
            self = (SELF) this;
            this.extractor = extractor;
        }

        public SELF refName(String refName) {
            extractor.refName = refName;
            return self;
        }

        public SELF field(String field) {
            extractor.field = field;
            return self;
        }

        public SELF defaultValue(Object defaultValue) {
            extractor.defaultValue = defaultValue;
            return self;
        }

        @Override
        public EXTRACTOR build() {
            return extractor;
        }
    }
}