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

package protocol.xyz.migoo.rabbitmq.sampler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.rabbitmq.client.*;
import core.xyz.migoo.builder.DefaultAssertionsBuilder;
import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import core.xyz.migoo.testelement.sampler.Sampler;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.rabbitmq.RabbitConstantsInterface;
import protocol.xyz.migoo.rabbitmq.RealRabbitRequest;
import protocol.xyz.migoo.rabbitmq.builder.RabbitConfigureElementBuilder;
import protocol.xyz.migoo.rabbitmq.builder.RabbitPostprocessorsBuilder;
import protocol.xyz.migoo.rabbitmq.builder.RabbitPreprocessorsBuilder;
import protocol.xyz.migoo.rabbitmq.config.RabbitConfigureItem;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2024/11/04 21:10
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Alias({"rabbitmq", "rabbit", "rabbit_mq", "rabbit_sampler"})
public class RabbitSampler extends AbstractSampler<RabbitSampler, RabbitConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, RabbitConstantsInterface {

    @JSONField(serialize = false)
    private RealRabbitRequest request;

    @JSONField(serialize = false)
    private ConnectionFactory factory;
    @JSONField(serialize = false)
    private Connection connection;
    @JSONField(serialize = false)
    private Channel channel;
    @JSONField(serialize = false)
    private byte[] response;

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
        return new DefaultSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        var message = switch (runtime.config.getMessage()) {
            case Map map -> JSON.toJSONString(map);
            case List list -> JSON.toJSONString(list);
            case null -> "";
            default -> runtime.config.getMessage().toString();
        };
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            var queue = runtime.config.getQueue();
            channel.queueDeclare(queue.getName(), queue.getDurable(), queue.getExclusive(), queue.getAutoDelete(), queue.getArguments());
            result.sampleStart();
            AMQP.BasicProperties properties = getBasicProperties();
            var exchange = runtime.config.getExchange();
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
            this.request = RealRabbitRequest.build(runtime.config, message);
        }
    }

    private AMQP.BasicProperties getBasicProperties() {
        AMQP.BasicProperties properties = null;
        if (Objects.nonNull(runtime.config.getProps())) {
            properties = new AMQP.BasicProperties(
                    runtime.config.getProps().getContentType(),
                    runtime.config.getProps().getContentEncoding(),
                    runtime.config.getProps().getHeaders(),
                    runtime.config.getProps().getDeliveryMode(),
                    runtime.config.getProps().getPriority(),
                    runtime.config.getProps().getCorrelationId(),
                    runtime.config.getProps().getReplyTo(),
                    runtime.config.getProps().getExpiration(),
                    runtime.config.getProps().getMessageId(),
                    new Date(),
                    runtime.config.getProps().getType(),
                    runtime.config.getProps().getUserId(),
                    runtime.config.getProps().getAppId(),
                    runtime.config.getProps().getClusterId()
            );
        }
        return properties;
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
        factory = new ConnectionFactory();
        factory.setConnectionTimeout(runtime.config.getTimeout());
        factory.setVirtualHost(runtime.config.getVirtualHost());
        factory.setHost(runtime.config.getHost());
        factory.setPort(Integer.parseInt(runtime.config.getPort()));
        factory.setUsername(runtime.config.getUsername());
        factory.setPassword(runtime.config.getPassword());

    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(request);
        result.setResponse(SampleResult.DefaultReal.build(response));
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

    public static class Builder extends AbstractSampler.Builder<RabbitSampler, Builder, RabbitConfigureItem,
            RabbitConfigureItem.Builder, RabbitConfigureElementBuilder, RabbitPreprocessorsBuilder, RabbitPostprocessorsBuilder,
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
        protected RabbitConfigureElementBuilder getConfiguresBuilder() {
            return RabbitConfigureElementBuilder.builder();
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
