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

package io.github.xiaomisum.ryze.protocol.rabbit.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.rabbitmq.client.*;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.processor.AbstractProcessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Postprocessor;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.rabbit.RabbitConstantsInterface;
import io.github.xiaomisum.ryze.protocol.rabbit.RealRabbitRequest;
import io.github.xiaomisum.ryze.protocol.rabbit.config.RabbitConfigureItem;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2024/11/04 20:09
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@KW({"rabbitmq", "rabbit", "rabbit_mq", "rabbit_postprocessor"})
public class RabbitPostprocessor extends AbstractProcessor<RabbitPostprocessor, RabbitConfigureItem, DefaultSampleResult> implements Postprocessor, RabbitConstantsInterface {

    @JSONField(serialize = false)
    private RealRabbitRequest request;

    @JSONField(serialize = false)
    private ConnectionFactory factory;
    @JSONField(serialize = false)
    private Connection connection;
    @JSONField(serialize = false)
    private Channel channel;

    public RabbitPostprocessor() {
        super();
    }

    public RabbitPostprocessor(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        var message = switch (runtime.getConfig().getMessage()) {
            case Map map -> JSON.toJSONString(map);
            case List list -> JSON.toJSONString(list);
            case null -> "";
            default -> runtime.getConfig().getMessage().toString();
        };
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            var queue = runtime.getConfig().getQueue();
            channel.queueDeclare(queue.getName(), queue.getDurable(), queue.getExclusive(), queue.getAutoDelete(), queue.getArguments());
            result.sampleStart();
            AMQP.BasicProperties properties = getBasicProperties();
            var exchange = runtime.getConfig().getExchange();
            var exchangeName = Objects.isNull(exchange) ? null : exchange.getName();
            var routingKey = Objects.isNull(exchange) ? null : exchange.getRoutingKey();

            if (Objects.nonNull(exchange)) {
                channel.exchangeDeclare(exchangeName,
                        switch (exchange.getType()) {
                            case EXCHANGE_TYPE_FANOUT -> BuiltinExchangeType.FANOUT;
                            case EXCHANGE_TYPE_DIRECT -> BuiltinExchangeType.DIRECT;
                            default -> BuiltinExchangeType.TOPIC;
                        });
                routingKey = EXCHANGE_TYPE_FANOUT.equals(exchange.getType()) ? null : exchange.getRoutingKey();
                channel.queueBind(exchange.getName(), exchange.getType(), routingKey);
            }
            channel.basicPublish(exchangeName, queue.getName(), properties, message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            result.sampleEnd();
            this.request = RealRabbitRequest.build(runtime.getConfig(), message);
        }
    }

    private AMQP.BasicProperties getBasicProperties() {
        AMQP.BasicProperties properties = null;
        if (Objects.nonNull(runtime.getConfig().getProps())) {
            properties = new AMQP.BasicProperties(
                    runtime.getConfig().getProps().getContentType(),
                    runtime.getConfig().getProps().getContentEncoding(),
                    runtime.getConfig().getProps().getHeaders(),
                    runtime.getConfig().getProps().getDeliveryMode(),
                    runtime.getConfig().getProps().getPriority(),
                    runtime.getConfig().getProps().getCorrelationId(),
                    runtime.getConfig().getProps().getReplyTo(),
                    runtime.getConfig().getProps().getExpiration(),
                    runtime.getConfig().getProps().getMessageId(),
                    new Date(),
                    runtime.getConfig().getProps().getType(),
                    runtime.getConfig().getProps().getUserId(),
                    runtime.getConfig().getProps().getAppId(),
                    runtime.getConfig().getProps().getClusterId()
            );
        }
        return properties;
    }


    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.getConfig()) ? new RabbitConfigureItem() : runtime.getConfig();
        var ref = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (RabbitConfigureItem) context.getLocalVariablesWrapper().get(ref);
        runtime.setConfig(localConfig.merge(otherConfig));
        // 2. 创建Rabbit 连接池对象
        factory = new ConnectionFactory();
        factory.setConnectionTimeout(runtime.getConfig().getTimeout());
        factory.setVirtualHost(runtime.getConfig().getVirtualHost());
        factory.setHost(runtime.getConfig().getHost());
        factory.setPort(Integer.parseInt(runtime.getConfig().getPort()));
        factory.setUsername(runtime.getConfig().getUsername());
        factory.setPassword(runtime.getConfig().getPassword());

    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(request);
        result.setResponse(SampleResult.DefaultReal.build(new byte[0]));
        if (channel != null) {
            try {
                channel.close();
            } catch (Exception ignored) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ignored) {
            }
        }
        factory = null;
    }

    public static class Builder extends AbstractProcessor.PostprocessorBuilder<RabbitPostprocessor, Builder, RabbitConfigureItem,
            RabbitConfigureItem.Builder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public RabbitPostprocessor build() {
            return new RabbitPostprocessor(this);
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected RabbitConfigureItem.Builder getConfigureItemBuilder() {
            return RabbitConfigureItem.builder();
        }
    }
}