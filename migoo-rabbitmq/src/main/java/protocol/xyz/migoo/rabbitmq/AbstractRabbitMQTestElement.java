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

package protocol.xyz.migoo.rabbitmq;

import com.alibaba.fastjson2.JSONObject;
import com.rabbitmq.client.*;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import protocol.xyz.migoo.rabbitmq.config.RabbitMQDefaults;
import protocol.xyz.migoo.rabbitmq.util.RabbitMQConstantsInterface;

import java.util.Date;
import java.util.Objects;

/**
 * @author xiaomi
 * Created in 2024/11/04 11:08
 */
public abstract class AbstractRabbitMQTestElement extends AbstractTestElement implements RabbitMQConstantsInterface {

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    public void testStarted() {
        var other = (RabbitMQDefaults) getVariables().get(RABBIT_MQ_DEFAULT);
        if (Objects.nonNull(other)) {
            setProperty(VIRTUAL_HOST, other.get(VIRTUAL_HOST));
            setProperty(HOST, other.get(HOST));
            setProperty(PORT, other.get(PORT));
            setProperty(USERNAME, other.get(USERNAME));
            setProperty(PASSWORD, other.get(PASSWORD));
            setProperty(QUEUE_CONFIG, other.get(QUEUE_CONFIG));
            setProperty(EXCHANGE, other.get(EXCHANGE));
        }
        this.buildConnectionFactory();
    }

    protected SampleResult execute(SampleResult result) throws Exception {
        result.setTestClass(this.getClass());
        result.sampleStart();
        result.setSamplerData(getRequestString());
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            var queue = getPropertyAsJSONObject(QUEUE_CONFIG);
            channel.queueDeclare(queue.getString(QUEUE_NAME), queue.getBooleanValue(QUEUE_DURABLE), queue.getBooleanValue(QUEUE_EXCLUSIVE),
                    queue.getBooleanValue(QUEUE_AUTO_DELETE), queue.getJSONObject(QUEUE_ARGUMENTS));
            result.setSamplerData(getRequestString());
            var exchange = getPropertyAsJSONObject(EXCHANGE);
            AMQP.BasicProperties properties = null;
            var props = getPropertyAsJSONObject(PROPS);
            if (Objects.nonNull(props) && !props.isEmpty()) {
                properties = new AMQP.BasicProperties(
                        getPropertyAsString(PROPS_CONTENT_TYPE),
                        getPropertyAsString(PROPS_CONTENT_ENCODING),
                        getPropertyAsJSONObject(PROPS_HEADERS),
                        getPropertyAsInt(PROPS_DELIVERY_MODE),
                        getPropertyAsInt(PROPS_PRIORITY),
                        getPropertyAsString(PROPS_CORRELATION_ID),
                        getPropertyAsString(PROPS_REPLY_TO),
                        getPropertyAsString(PROPS_EXPIRATION),
                        getPropertyAsString(PROPS_MESSAGE_ID),
                        new Date(getPropertyAsLong(PROPS_TIMESTAMP)),
                        getPropertyAsString(PROPS_TYPE),
                        getPropertyAsString(PROPS_USER_ID),
                        getPropertyAsString(PROPS_APP_ID),
                        getPropertyAsString(PROPS_CLUSTER_ID)
                );
            }
            var exchangeName = Objects.nonNull(exchange) && !exchange.isEmpty() ? queue.getString(EXCHANGE_NAME) : null;
            var routingKey = Objects.isNull(exchange) || exchange.isEmpty() ? queue.getString(QUEUE_NAME) : null;
            if (Objects.nonNull(exchange) && !exchange.isEmpty()) {
                var type = Objects.nonNull(exchange.get(EXCHANGE_TYPE)) ? exchange.getString(EXCHANGE_TYPE).toLowerCase()
                        : EXCHANGE_TYPE_TOPIC;
                channel.exchangeDeclare(exchange.getString(EXCHANGE_NAME),
                        switch (type) {
                            case EXCHANGE_TYPE_FANOUT:
                                yield BuiltinExchangeType.FANOUT;
                            case EXCHANGE_TYPE_DIRECT:
                                yield BuiltinExchangeType.DIRECT;
                            case EXCHANGE_TYPE_TOPIC:
                            default:
                                yield BuiltinExchangeType.TOPIC;
                        });
                routingKey = EXCHANGE_TYPE_FANOUT.equals(type) ? null : exchange.getString(EXCHANGE_ROUTING_KEY);
                channel.queueBind(queue.getString(QUEUE_NAME), queue.getString(EXCHANGE_NAME), routingKey);
            }
            channel.basicPublish(exchangeName, queue.getString(QUEUE_NAME), properties, getPropertyAsByteArray(MESSAGE));
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    private void buildConnectionFactory() {
        factory = new ConnectionFactory();
        factory.setConnectionTimeout((Integer) get(TIMEOUT, ConnectionFactory.DEFAULT_CONNECTION_TIMEOUT));
        factory.setVirtualHost((String) get(VIRTUAL_HOST, ConnectionFactory.DEFAULT_VHOST));
        factory.setHost(getPropertyAsString(HOST));
        factory.setPort((Integer) get(PORT, ConnectionFactory.DEFAULT_AMQP_PORT));
        factory.setUsername((String) get(USERNAME, ConnectionFactory.DEFAULT_USER));
        factory.setPassword((String) get(PASSWORD, ConnectionFactory.DEFAULT_PASS));
    }

    private String getRequestString() {
        JSONObject host = new JSONObject(10);
        host.put(HOST, getPropertyAsString(HOST));
        host.put(PORT, get(PORT, ConnectionFactory.DEFAULT_AMQP_PORT));
        host.put(USERNAME, get(USERNAME, ConnectionFactory.DEFAULT_USER));
        host.put(PASSWORD, get(PASSWORD, ConnectionFactory.DEFAULT_PASS));
        host.put(VIRTUAL_HOST, get(VIRTUAL_HOST, ConnectionFactory.DEFAULT_VHOST));
        host.put(TIMEOUT, get(TIMEOUT, ConnectionFactory.DEFAULT_CONNECTION_TIMEOUT));

        var json = new JSONObject(10);
        json.put(HOST, host);
        json.put(QUEUE_CONFIG, get(QUEUE_CONFIG));
        json.put(PROPS, get(PROPS));
        json.put(EXCHANGE, get(EXCHANGE));
        json.put(MESSAGE, get(MESSAGE));
        return json.toString();
    }

    public void testEnded() {
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
}
