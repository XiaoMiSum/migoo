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

package io.github.xiaomisum.ryze.protocol.rabbit.config;

import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.protocol.rabbit.RabbitConstantsInterface;
import io.github.xiaomisum.ryze.support.Customizer;
import io.github.xiaomisum.ryze.support.groovy.Groovy;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author xiaomi
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RabbitConfigureItem implements ConfigureItem<RabbitConfigureItem>, RabbitConstantsInterface {

    @JSONField(name = REF)
    protected String ref;

    @JSONField(name = VIRTUAL_HOST, ordinal = 1)
    protected String virtualHost;

    @JSONField(name = HOST, ordinal = 2)
    protected String host;

    @JSONField(name = PORT, ordinal = 3)
    protected String port;

    @JSONField(name = USERNAME, ordinal = 4)
    protected String username;

    @JSONField(name = PASSWORD, ordinal = 5)
    protected String password;

    @JSONField(name = MESSAGE, ordinal = 6)
    protected Object message;

    @JSONField(name = TIMEOUT, ordinal = 7)
    protected Integer timeout;

    @JSONField(name = QUEUE_CONFIG, ordinal = 8)
    protected Queue queue;

    @JSONField(name = EXCHANGE, ordinal = 9)
    protected Exchange exchange;

    @JSONField(name = PROPS, ordinal = 10)
    protected Props props;

    public RabbitConfigureItem() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public RabbitConfigureItem merge(RabbitConfigureItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.ref = StringUtils.isBlank(self.ref) ? localOther.ref : self.ref;
        self.virtualHost = StringUtils.isBlank(self.virtualHost) ? localOther.virtualHost : self.virtualHost;
        self.host = StringUtils.isBlank(self.host) ? localOther.host : self.host;
        self.port = StringUtils.isBlank(self.port) ? localOther.port : self.port;
        self.message = Objects.isNull(message) ? localOther.message : self.message;
        self.username = StringUtils.isBlank(self.username) ? localOther.username : self.username;
        self.password = StringUtils.isBlank(self.password) ? localOther.password : self.password;
        self.timeout = Objects.isNull(timeout) ? localOther.timeout : self.timeout;
        self.queue = Objects.isNull(queue) ? localOther.queue : self.queue;
        self.exchange = Objects.isNull(exchange) ? localOther.exchange : self.exchange;
        self.props = Objects.isNull(props) ? localOther.props : self.props;
        return self;
    }

    @Override
    public RabbitConfigureItem evaluate(ContextWrapper context) {
        ref = (String) context.evaluate(ref);
        virtualHost = (String) context.evaluate(virtualHost);
        host = (String) context.evaluate(host);
        port = (String) context.evaluate(port);
        username = (String) context.evaluate(username);
        password = (String) context.evaluate(password);
        message = context.evaluate(message);
        queue = (Queue) context.evaluate(queue);
        exchange = (Exchange) context.evaluate(exchange);
        props = (Props) context.evaluate(props);
        return this;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getVirtualHost() {
        return StringUtils.isBlank(virtualHost) ? "/" : virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return StringUtils.isBlank(port) ? "5672" : port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return StringUtils.isBlank(username) ? "guest" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return StringUtils.isBlank(password) ? "guest" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Integer getTimeout() {
        return Objects.isNull(timeout) ? 60000 : timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Props getProps() {
        return props;
    }

    public void setProps(Props props) {
        this.props = props;
    }

    public static class Queue implements ConfigureItem<Queue> {

        @JSONField(name = QUEUE_NAME)
        protected String name;

        @JSONField(name = QUEUE_DURABLE, ordinal = 1)
        protected Boolean durable;

        @JSONField(name = QUEUE_EXCLUSIVE, ordinal = 2)
        protected Boolean exclusive;

        @JSONField(name = QUEUE_AUTO_DELETE, ordinal = 3)
        protected Boolean autoDelete;

        @JSONField(name = QUEUE_ARGUMENTS, ordinal = 4)
        protected Map<String, Object> arguments;

        public Queue() {
        }

        @Override
        public Queue merge(Queue other) {
            if (other == null) {
                return copy();
            }
            var localOther = other.copy();
            var self = copy();
            self.name = StringUtils.isBlank(name) ? localOther.name : self.name;
            self.durable = Objects.isNull(durable) ? localOther.durable : self.durable;
            self.exclusive = Objects.isNull(exclusive) ? localOther.exclusive : self.exclusive;
            self.autoDelete = Objects.isNull(autoDelete) ? localOther.autoDelete : self.autoDelete;
            self.arguments = Objects.isNull(arguments) ? localOther.arguments : self.arguments;
            return self;
        }

        @Override
        public Queue evaluate(ContextWrapper context) {
            name = (String) context.evaluate(name);
            durable = (Boolean) context.evaluate(durable);
            exclusive = (Boolean) context.evaluate(exclusive);
            autoDelete = (Boolean) context.evaluate(autoDelete);
            arguments = (Map<String, Object>) context.evaluate(arguments);
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getDurable() {
            return Objects.nonNull(durable) && durable;
        }

        public void setDurable(Boolean durable) {
            this.durable = durable;
        }

        public Boolean getExclusive() {
            return Objects.nonNull(exclusive) && exclusive;
        }

        public void setExclusive(Boolean exclusive) {
            this.exclusive = exclusive;
        }

        public Boolean getAutoDelete() {
            return Objects.nonNull(autoDelete) && autoDelete;
        }

        public void setAutoDelete(Boolean autoDelete) {
            this.autoDelete = autoDelete;
        }

        public Map<String, Object> getArguments() {
            return arguments;
        }

        public void setArguments(Map<String, Object> arguments) {
            this.arguments = arguments;
        }

        public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, Queue> {

            private Queue queue = new Queue();

            public static Builder builder() {
                return new Builder();
            }

            public Builder name(String name) {
                queue.name = name;
                return self;
            }

            public Builder durable() {
                queue.durable = true;
                return self;
            }

            public Builder exclusive() {
                queue.exclusive = true;
                return self;
            }

            public Builder autoDelete() {
                queue.autoDelete = true;
                return self;
            }

            public Builder arguments(Map<String, Object> arguments) {
                queue.arguments = arguments;
                return self;
            }

            public Builder argument(String name, Object value) {
                synchronized (this) {
                    if (queue.arguments == null) {
                        synchronized (this) {
                            this.queue.arguments = new HashMap<>();
                        }
                    }
                }
                queue.arguments.put(name, value);
                return self;
            }

            public Builder config(Queue queue) {
                this.queue = this.queue.merge(queue);
                return self;
            }

            @Override
            public Queue build() {
                return queue;
            }
        }
    }

    public static class Exchange implements ConfigureItem<Exchange> {

        @JSONField(name = EXCHANGE_NAME)
        protected String name;
        @JSONField(name = EXCHANGE_TYPE, ordinal = 1)
        protected String type;
        @JSONField(name = EXCHANGE_ROUTING_KEY, ordinal = 2)
        protected String routingKey;

        public Exchange() {
        }

        @Override
        public Exchange merge(Exchange other) {
            if (other == null) {
                return copy();
            }
            var localOther = other.copy();
            var self = copy();
            self.name = StringUtils.isBlank(name) ? localOther.name : self.name;
            self.type = StringUtils.isBlank(type) ? localOther.type : self.type;
            self.routingKey = StringUtils.isBlank(routingKey) ? localOther.routingKey : self.routingKey;
            return self;
        }

        @Override
        public Exchange evaluate(ContextWrapper context) {
            name = (String) context.evaluate(name);
            type = (String) context.evaluate(type);
            routingKey = (String) context.evaluate(routingKey);
            return this;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return StringUtils.isBlank(type) ? EXCHANGE_TYPE_TOPIC : type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRoutingKey() {
            return routingKey;
        }

        public void setRoutingKey(String routingKey) {
            this.routingKey = routingKey;
        }

        public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, Exchange> {

            private Exchange exchange = new Exchange();

            public static Builder builder() {
                return new Builder();
            }

            public Builder name(String name) {
                exchange.name = name;
                return self;
            }

            public Builder type(String type) {
                exchange.type = type;
                return self;
            }

            public Builder routingKey(String routingKey) {
                exchange.routingKey = routingKey;
                return self;
            }

            public Builder config(Exchange queue) {
                this.exchange = this.exchange.merge(queue);
                return self;
            }

            @Override
            public Exchange build() {
                return exchange;
            }
        }
    }

    public static class Props implements ConfigureItem<Props> {

        @JSONField(name = PROPS_CONTENT_TYPE)
        protected String contentType;
        @JSONField(name = PROPS_CONTENT_ENCODING, ordinal = 1)
        protected String contentEncoding;
        @JSONField(name = PROPS_HEADERS, ordinal = 2)
        protected Map<String, Object> headers;
        @JSONField(name = PROPS_DELIVERY_MODE, ordinal = 3)
        protected Integer deliveryMode;
        @JSONField(name = PROPS_PRIORITY, ordinal = 4)
        protected Integer priority;
        @JSONField(name = PROPS_CORRELATION_ID, ordinal = 5)
        protected String correlationId;
        @JSONField(name = PROPS_REPLY_TO, ordinal = 6)
        protected String replyTo;
        @JSONField(name = PROPS_EXPIRATION, ordinal = 7)
        protected String expiration;
        @JSONField(name = PROPS_MESSAGE_ID, ordinal = 8)
        protected String messageId;
        @JSONField(name = PROPS_TIMESTAMP, ordinal = 9)
        protected String type;
        @JSONField(name = PROPS_USER_ID, ordinal = 11)
        protected String userId;
        @JSONField(name = PROPS_APP_ID, ordinal = 12)
        protected String appId;
        @JSONField(name = PROPS_CLUSTER_ID, ordinal = 13)
        protected String clusterId;

        public Props() {
        }

        @Override
        public Props merge(Props other) {
            if (other == null) {
                return copy();
            }
            var localOther = other.copy();
            var self = copy();
            self.contentType = StringUtils.isBlank(contentType) ? localOther.contentType : self.contentType;
            self.contentEncoding = StringUtils.isBlank(contentEncoding) ? localOther.contentEncoding : self.contentEncoding;
            self.headers = Objects.isNull(headers) || headers.isEmpty() ? localOther.headers : self.headers;
            self.deliveryMode = self.deliveryMode == null ? localOther.deliveryMode : self.deliveryMode;
            self.priority = self.priority == null ? localOther.priority : self.priority;
            self.correlationId = StringUtils.isBlank(correlationId) ? localOther.correlationId : self.correlationId;
            self.replyTo = StringUtils.isBlank(replyTo) ? localOther.replyTo : self.replyTo;
            self.expiration = StringUtils.isBlank(expiration) ? localOther.expiration : self.expiration;
            self.messageId = StringUtils.isBlank(messageId) ? localOther.messageId : self.messageId;
            self.type = StringUtils.isBlank(type) ? localOther.type : self.type;
            self.userId = StringUtils.isBlank(userId) ? localOther.userId : self.userId;
            self.appId = StringUtils.isBlank(appId) ? localOther.appId : self.appId;
            self.clusterId = StringUtils.isBlank(clusterId) ? localOther.clusterId : self.clusterId;
            return self;
        }

        @Override
        public Props evaluate(ContextWrapper context) {
            contentType = (String) context.evaluate(contentType);
            contentEncoding = (String) context.evaluate(contentEncoding);
            headers = (Map<String, Object>) context.evaluate(headers);
            deliveryMode = (Integer) context.evaluate(deliveryMode);
            priority = (Integer) context.evaluate(priority);
            correlationId = (String) context.evaluate(correlationId);
            replyTo = (String) context.evaluate(replyTo);
            expiration = (String) context.evaluate(expiration);
            messageId = (String) context.evaluate(messageId);
            type = (String) context.evaluate(type);
            userId = (String) context.evaluate(userId);
            appId = (String) context.evaluate(appId);
            clusterId = (String) context.evaluate(clusterId);
            return this;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentEncoding() {
            return contentEncoding;
        }

        public void setContentEncoding(String contentEncoding) {
            this.contentEncoding = contentEncoding;
        }

        public Map<String, Object> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, Object> headers) {
            this.headers = headers;
        }

        public Integer getDeliveryMode() {
            return deliveryMode;
        }

        public void setDeliveryMode(Integer deliveryMode) {
            this.deliveryMode = deliveryMode;
        }

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public String getCorrelationId() {
            return correlationId;
        }

        public void setCorrelationId(String correlationId) {
            this.correlationId = correlationId;
        }

        public String getReplyTo() {
            return replyTo;
        }

        public void setReplyTo(String replyTo) {
            this.replyTo = replyTo;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getClusterId() {
            return clusterId;
        }

        public void setClusterId(String clusterId) {
            this.clusterId = clusterId;
        }

        public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, Props> {

            private Props props = new Props();

            public static Builder builder() {
                return new Builder();
            }

            public Builder contentType(String contentType) {
                this.props.contentType = contentType;
                return self;
            }

            public Builder contentEncoding(String contentEncoding) {
                this.props.contentEncoding = contentEncoding;
                return self;
            }

            public Builder headers(Map<String, Object> headers) {
                this.props.headers = headers;
                return self;
            }

            public Builder deliveryMode(Integer deliveryMode) {
                this.props.deliveryMode = deliveryMode;
                return self;
            }

            public Builder priority(Integer priority) {
                this.props.priority = priority;
                return self;
            }

            public Builder correlationId(String correlationId) {
                this.props.correlationId = correlationId;
                return self;
            }

            public Builder replyTo(String replyTo) {
                this.props.replyTo = replyTo;
                return self;
            }

            public Builder expiration(String expiration) {
                this.props.expiration = expiration;
                return self;
            }

            public Builder messageId(String messageId) {
                this.props.messageId = messageId;
                return self;
            }

            public Builder type(String type) {
                this.props.type = type;
                return self;
            }

            public Builder userId(String userId) {
                this.props.userId = userId;
                return self;
            }

            public Builder appId(String appId) {
                this.props.appId = appId;
                return self;
            }

            public Builder clusterId(String clusterId) {
                this.props.clusterId = clusterId;
                return self;
            }

            public Builder config(Props props) {
                this.props = this.props.merge(props);
                return self;
            }

            @Override
            public Props build() {
                return props;
            }
        }
    }

    /**
     * Rabbit 配置属性 构建器
     */
    public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, RabbitConfigureItem> {

        private RabbitConfigureItem configure = new RabbitConfigureItem();

        public Builder ref(String ref) {
            this.configure.ref = ref;
            return self;
        }

        public Builder virtualHost(String virtualHost) {
            this.configure.virtualHost = virtualHost;
            return self;
        }

        public Builder host(String host) {
            this.configure.host = host;
            return self;
        }

        public Builder port(String port) {
            this.configure.port = port;
            return self;
        }

        public Builder username(String username) {
            this.configure.username = username;
            return self;
        }

        public Builder password(String password) {
            this.configure.password = password;
            return self;
        }

        public <T> Builder message(Class<T> type, Customizer<T> customizer) {
            try {
                T message = type.getConstructor().newInstance();
                customizer.customize(message);
                configure.setMessage(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return self;
        }

        public Builder message(Object message) {
            this.configure.message = message;
            return self;
        }

        public Builder message(List<?> message) {
            this.configure.message = message;
            return self;
        }

        public Builder message(Map<?, ?> message) {
            this.configure.message = message;
            return self;
        }

        public Builder timeout(Integer timeout) {
            this.configure.timeout = timeout;
            return self;
        }

        public Builder queue(Queue queue) {
            this.configure.queue = queue;
            return self;
        }

        public Builder queue(Queue.Builder builder) {
            this.configure.queue = builder.build();
            return self;
        }

        public Builder queue(Consumer<Queue.Builder> consumer) {
            var builder = Queue.Builder.builder();
            consumer.accept(builder);
            this.configure.queue = builder.build();
            return self;
        }

        public Builder queue(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = Queue.Builder.class) Closure<?> closure) {
            var builder = Queue.Builder.builder();
            Groovy.call(closure, builder);
            this.configure.queue = builder.build();
            return self;
        }

        public Builder exchange(Exchange exchange) {
            this.configure.exchange = exchange;
            return self;
        }

        public Builder exchange(Exchange.Builder builder) {
            this.configure.exchange = builder.build();
            return self;
        }

        public Builder exchange(Consumer<Exchange.Builder> consumer) {
            var builder = Exchange.Builder.builder();
            consumer.accept(builder);
            this.configure.exchange = builder.build();
            return self;
        }

        public Builder exchange(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = Exchange.Builder.class) Closure<?> closure) {
            var builder = Exchange.Builder.builder();
            Groovy.call(closure, builder);
            this.configure.exchange = builder.build();
            return self;
        }

        public Builder props(Props props) {
            this.configure.props = props;
            return self;
        }

        public Builder props(Props.Builder builder) {
            this.configure.props = builder.build();
            return self;
        }

        public Builder props(Consumer<Props.Builder> consumer) {
            var builder = Props.Builder.builder();
            consumer.accept(builder);
            this.configure.props = builder.build();
            return self;
        }

        public Builder props(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = Props.Builder.class) Closure<?> closure) {
            var builder = Props.Builder.builder();
            Groovy.call(closure, builder);
            this.configure.props = builder.build();
            return self;
        }

        public Builder config(RabbitConfigureItem config) {
            this.configure = configure.merge(config);
            return self;
        }

        @Override
        public RabbitConfigureItem build() {
            return configure;
        }
    }

}
