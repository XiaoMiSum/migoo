/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.protocol.rabbit.config;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.TestSuiteResult;
import io.github.xiaomisum.ryze.core.testelement.configure.AbstractConfigureElement;
import io.github.xiaomisum.ryze.protocol.rabbit.RabbitConstantsInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2024/11/04 20:38
 */
@KW({"rabbitmq", "rabbit", "rabbit_mq", "rabbit_defaults"})
public class RabbitDefaults extends AbstractConfigureElement<RabbitDefaults, RabbitConfigureItem, TestSuiteResult> implements RabbitConstantsInterface {

    public RabbitDefaults(Builder builder) {
        super(builder);
    }

    public RabbitDefaults() {
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
        var config = (RabbitConfigureItem) context.getSessionRunner().getContext().getLocalVariablesWrapper().get(otherRefName);
        if (Objects.nonNull(config)) {
            runtime.setConfig(localConfig = localConfig.merge(config));
        }
        context.getSessionRunner().getContext().getLocalVariablesWrapper().put(refName, localConfig);
    }


    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult("Rabbit MQ 默认配置");
    }

    /**
     * Rabbit MQ 默认配置构建器
     */
    public static class Builder extends AbstractConfigureElement.Builder<RabbitDefaults, RabbitDefaults.Builder, RabbitConfigureItem, RabbitConfigureItem.Builder, TestSuiteResult> {

        @Override
        public RabbitDefaults build() {
            return new RabbitDefaults(this);
        }

        @Override
        protected RabbitConfigureItem.Builder getConfigureItemBuilder() {
            return RabbitConfigureItem.builder();
        }
    }
}