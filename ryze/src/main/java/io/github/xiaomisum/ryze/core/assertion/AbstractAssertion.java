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

package io.github.xiaomisum.ryze.core.assertion;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.ApplicationConfig;
import io.github.xiaomisum.ryze.core.TestStatus;
import io.github.xiaomisum.ryze.core.builder.IBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Objects;

/**
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractAssertion implements Assertion, AssertionConstantsInterface {

    protected Object actualValue;

    @JSONField(name = EXPECTED)
    protected Object expected;

    @JSONField(name = RULE)
    protected String rule;

    @JSONField(name = FIELD)
    protected String field;

    @Override
    public void assertThat(ContextWrapper context) {
        if (!context.getTestResult().getStatus().isPassed()) {
            // 非 ryze-testng 框架下测试失败不会抛出异常，取样步骤结果失败，无需执行验证器
            return;
        }
        if (context.getTestResult() instanceof SampleResult result) {
            expected = context.evaluate(expected);
            var res = initialized(result);
            rule = StringUtils.isBlank(rule) ? "==" : rule;
            var checkRule = ApplicationConfig.getRuleKeyMap().get(rule.toLowerCase(Locale.ROOT));
            if (Objects.isNull(checkRule)) {
                res.setStatus(TestStatus.failed);
                res.setMessage(String.format("没有匹配的验证规则：%s", rule));
            } else {
                var bool = checkRule.assertThat(actualValue, expected);
                res.setStatus(bool ? TestStatus.passed : TestStatus.failed);
                res.setMessage(String.format("%s %s %s %s %s", field, rule, expected == null ? "" : expected, res.getStatus(),
                        res.getStatus() == TestStatus.passed ? "" : "实际值: " + actualValue));
            }
            if (res.getStatus().isBroken() || res.getStatus().isFailed()) {
                // 验证结果为 阻塞、失败，则设置取样器状态为失败
                result.setStatus(res.getStatus());
                throw new AssertionError(res.getMessage());
            }
        }
    }

    protected abstract AssertionResult initialized(SampleResult result);

    public Object getActualValue() {
        return actualValue;
    }

    public void setActualValue(Object actualValue) {
        this.actualValue = actualValue;
    }

    public Object getExpected() {
        return expected;
    }

    public void setExpected(Object expected) {
        this.expected = expected;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    /**
     * 验证器基础构建器
     *
     * @param <SELF> 构建类自身
     */
    public static abstract class Builder<SELF extends Builder<SELF, ASSERTION>, ASSERTION extends AbstractAssertion>
            implements IBuilder<ASSERTION> {
        private final ASSERTION assertion;
        protected SELF self;


        protected Builder(ASSERTION assertion) {
            self = (SELF) this;
            this.assertion = assertion;
        }

        public SELF field(String field) {
            this.assertion.field = field;
            return self;
        }

        public SELF rule(String rule) {
            this.assertion.rule = rule;
            return self;
        }

        public SELF expected(Object expected) {
            this.assertion.expected = expected;
            return self;
        }

        @Override
        public ASSERTION build() {
            return assertion;
        }
    }
}