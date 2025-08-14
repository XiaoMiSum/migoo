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

package io.github.xiaomisum.ryze.protocol.active.sampler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.builder.DefaultAssertionsBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;
import io.github.xiaomisum.ryze.protocol.active.Active;
import io.github.xiaomisum.ryze.protocol.active.ActiveConstantsInterface;
import io.github.xiaomisum.ryze.protocol.active.RealActiveRequest;
import io.github.xiaomisum.ryze.protocol.active.builder.ActiveConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.active.builder.ActivePostprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.active.builder.ActivePreprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.active.config.ActiveConfigureItem;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/10 21:10
 */
@KW({"active_mq", "activemq", "active"})
@SuppressWarnings({"unchecked", "rawtypes"})
public class ActiveSampler extends AbstractSampler<ActiveSampler, ActiveConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, ActiveConstantsInterface {

    @JSONField(serialize = false)
    private ConnectionFactory factory;

    @JSONField(serialize = false)
    private String message;

    public ActiveSampler() {
        super();
    }

    public ActiveSampler(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.id, runtime.title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        Active.execute(runtime.config, factory, message, result);
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.getConfig()) ? new ActiveConfigureItem() : runtime.getConfig();
        var ref = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (ActiveConfigureItem) context.getLocalVariablesWrapper().get(ref);
        runtime.setConfig(localConfig.merge(otherConfig));
        // 2. 创建ActiveMQ 连接池;
        factory = new ActiveMQConnectionFactory(runtime.config.getUsername(), runtime.config.getPassword(), runtime.config.getBrokerUrl());
        message = switch (config.getMessage()) {
            case Number number -> number.toString();
            case Boolean bool -> bool.toString();
            case null -> "";
            default -> JSON.toJSONString(config.getMessage());
        };
        result.setRequest(RealActiveRequest.build(runtime.getConfig(), message));
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setResponse(SampleResult.DefaultReal.build(new byte[0]));
        factory = null;
    }

    public static class Builder extends AbstractSampler.Builder<ActiveSampler, Builder, ActiveConfigureItem,
            ActiveConfigureItem.Builder, ActiveConfigureElementsBuilder, ActivePreprocessorsBuilder, ActivePostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public ActiveSampler build() {
            return new ActiveSampler(this);
        }

        @Override
        protected DefaultAssertionsBuilder getAssertionsBuilder() {
            return DefaultAssertionsBuilder.builder();
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected ActiveConfigureElementsBuilder getConfiguresBuilder() {
            return ActiveConfigureElementsBuilder.builder();
        }

        @Override
        protected ActivePreprocessorsBuilder getPreprocessorsBuilder() {
            return ActivePreprocessorsBuilder.builder();
        }

        @Override
        protected ActivePostprocessorsBuilder getPostprocessorsBuilder() {
            return ActivePostprocessorsBuilder.builder();
        }

        @Override
        protected ActiveConfigureItem.Builder getConfigureItemBuilder() {
            return ActiveConfigureItem.builder();
        }
    }
}
