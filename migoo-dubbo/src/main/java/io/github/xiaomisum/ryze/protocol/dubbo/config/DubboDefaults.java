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

package io.github.xiaomisum.ryze.protocol.dubbo.config;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.TestSuiteResult;
import io.github.xiaomisum.ryze.core.testelement.configure.AbstractConfigureElement;
import io.github.xiaomisum.ryze.protocol.dubbo.DubboConstantsInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/10 20:38
 */
@KW(value = {"Dubbo_Default", "DubboDefault", "Dubbo_Defaults", "dubbo"})
public class DubboDefaults extends AbstractConfigureElement<DubboDefaults, DubboConfigureItem, TestSuiteResult> implements DubboConstantsInterface {

    public DubboDefaults(Builder builder) {
        super(builder);
    }

    public DubboDefaults() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void doProcess(ContextWrapper context) {
        refName = StringUtils.isBlank(refName) ? DEF_REF_NAME_KEY : refName;
        var localConfig = runtime.getConfig();
        var otherRefName = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var config = (DubboConfigureItem) context.getSessionRunner().getContext().getLocalVariablesWrapper().get(otherRefName);
        if (Objects.nonNull(config)) {
            runtime.setConfig(localConfig = localConfig.merge(config));
        }
        context.getSessionRunner().getContext().getLocalVariablesWrapper().put(refName, localConfig);
    }

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult("Dubbo 默认配置");
    }

    /**
     * Dubbo默认配置 测试元件 构建类
     */
    public static class Builder extends AbstractConfigureElement.Builder<DubboDefaults, Builder, DubboConfigureItem, DubboConfigureItem.Builder, TestSuiteResult> {

        @Override
        public DubboDefaults build() {
            return new DubboDefaults(this);
        }

        @Override
        protected DubboConfigureItem.Builder getConfigureItemBuilder() {
            return DubboConfigureItem.builder();
        }
    }
}
