/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package io.github.xiaomisum.ryze.protocol.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.protocol.rabbit.config.RabbitConfigureItem;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

import static io.github.xiaomisum.ryze.protocol.rabbit.RabbitConstantsInterface.EXCHANGE_TYPE_DIRECT;
import static io.github.xiaomisum.ryze.protocol.rabbit.RabbitConstantsInterface.EXCHANGE_TYPE_FANOUT;

/**
 * @author xiaomi
 * Created at 2025/7/26 22:24
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Rabbit {

    public static void execute(ConnectionFactory factory, RabbitConfigureItem config, String message, DefaultSampleResult result) {
        try (var connection = factory.newConnection(); var channel = connection.createChannel()) {
            var queue = config.getQueue();
            channel.queueDeclare(queue.getName(), queue.getDurable(), queue.getExclusive(), queue.getAutoDelete(), queue.getArguments());
            result.sampleStart();
            var properties = getBasicProperties(config);
            var exchange = config.getExchange();
            var exchangeName = Objects.isNull(exchange) ? "" : exchange.getName();
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
        }
    }

    private static AMQP.BasicProperties getBasicProperties(RabbitConfigureItem config) {
        AMQP.BasicProperties properties = null;
        if (Objects.nonNull(config.getProps())) {
            properties = new AMQP.BasicProperties(
                    config.getProps().getContentType(),
                    config.getProps().getContentEncoding(),
                    config.getProps().getHeaders(),
                    config.getProps().getDeliveryMode(),
                    config.getProps().getPriority(),
                    config.getProps().getCorrelationId(),
                    config.getProps().getReplyTo(),
                    config.getProps().getExpiration(),
                    config.getProps().getMessageId(),
                    new Date(),
                    config.getProps().getType(),
                    config.getProps().getUserId(),
                    config.getProps().getAppId(),
                    config.getProps().getClusterId()
            );
        }
        return properties;
    }

    public static ConnectionFactory handleRequest(RabbitConfigureItem config) {
        var factory = new ConnectionFactory();
        factory.setConnectionTimeout(config.getTimeout());
        factory.setVirtualHost(config.getVirtualHost());
        factory.setHost(config.getHost());
        factory.setPort(Integer.parseInt(config.getPort()));
        factory.setUsername(config.getUsername());
        factory.setPassword(config.getPassword());
        return factory;
    }
}
