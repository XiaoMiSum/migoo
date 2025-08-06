/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.protocol.active.config;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.TestSuiteResult;
import io.github.xiaomisum.ryze.core.testelement.configure.AbstractConfigureElement;
import io.github.xiaomisum.ryze.protocol.active.ActiveConstantsInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/10 20:38
 */
@KW({"active_mq", "activemq", "active"})
public class ActiveDefaults extends AbstractConfigureElement<ActiveDefaults, ActiveConfigureItem, TestSuiteResult> implements ActiveConstantsInterface {

    public ActiveDefaults(Builder builder) {
        super(builder);
    }

    public ActiveDefaults() {
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
        var config = (ActiveConfigureItem) context.getSessionRunner().getContext().getLocalVariablesWrapper().get(otherRefName);
        if (Objects.nonNull(config)) {
            runtime.setConfig(localConfig = localConfig.merge(config));
        }
        context.getSessionRunner().getContext().getLocalVariablesWrapper().put(refName, localConfig);
    }


    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult("Active MQ 默认配置");
    }

    /**
     * ActiveMQ默认配置 测试元件 构建类
     */
    public static class Builder extends AbstractConfigureElement.Builder<ActiveDefaults, Builder, ActiveConfigureItem, ActiveConfigureItem.Builder, TestSuiteResult> {

        @Override
        public ActiveDefaults build() {
            return new ActiveDefaults(this);
        }

        @Override
        protected ActiveConfigureItem.Builder getConfigureItemBuilder() {
            return ActiveConfigureItem.builder();
        }
    }
}
