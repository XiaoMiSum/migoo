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

package io.github.xiaomisum.ryze.protocol.rabbit.sampler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.rabbitmq.client.ConnectionFactory;
import io.github.xiaomisum.ryze.core.builder.DefaultAssertionsBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;
import io.github.xiaomisum.ryze.protocol.rabbit.Rabbit;
import io.github.xiaomisum.ryze.protocol.rabbit.RabbitConstantsInterface;
import io.github.xiaomisum.ryze.protocol.rabbit.RealRabbitRequest;
import io.github.xiaomisum.ryze.protocol.rabbit.builder.RabbitConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.rabbit.builder.RabbitPostprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.rabbit.builder.RabbitPreprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.rabbit.config.RabbitConfigureItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2024/11/04 21:10
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@KW({"rabbitmq", "rabbit", "rabbit_mq", "rabbit_sampler"})
public class RabbitSampler extends AbstractSampler<RabbitSampler, RabbitConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, RabbitConstantsInterface {

    @JSONField(serialize = false)
    private ConnectionFactory factory;

    @JSONField(serialize = false)
    private String message;

    public RabbitSampler(Builder builder) {
        super(builder);
    }

    public RabbitSampler() {
        super();
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
        Rabbit.execute(factory, runtime.config, message, result);
    }


    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.config) ? new RabbitConfigureItem() : runtime.config;
        var ref = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (RabbitConfigureItem) context.getLocalVariablesWrapper().get(ref);
        runtime.setConfig(localConfig.merge(otherConfig));
        // 2. 创建Rabbit 连接池对象
        factory = Rabbit.handleRequest(runtime.getConfig());
        message = switch (config.getMessage()) {
            case Number number -> number.toString();
            case Boolean bool -> bool.toString();
            case null -> "";
            default -> JSON.toJSONString(config.getMessage());
        };
        result.setRequest(RealRabbitRequest.build(runtime.getConfig(), message));
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setResponse(SampleResult.DefaultReal.build(new byte[0]));
        factory = null;
    }

    public static class Builder extends AbstractSampler.Builder<RabbitSampler, Builder, RabbitConfigureItem,
            RabbitConfigureItem.Builder, RabbitConfigureElementsBuilder, RabbitPreprocessorsBuilder, RabbitPostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public RabbitSampler build() {
            return new RabbitSampler(this);
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
        protected RabbitConfigureElementsBuilder getConfiguresBuilder() {
            return RabbitConfigureElementsBuilder.builder();
        }

        @Override
        protected RabbitPreprocessorsBuilder getPreprocessorsBuilder() {
            return RabbitPreprocessorsBuilder.builder();
        }

        @Override
        protected RabbitPostprocessorsBuilder getPostprocessorsBuilder() {
            return RabbitPostprocessorsBuilder.builder();
        }

        @Override
        protected RabbitConfigureItem.Builder getConfigureItemBuilder() {
            return RabbitConfigureItem.builder();
        }
    }
}
