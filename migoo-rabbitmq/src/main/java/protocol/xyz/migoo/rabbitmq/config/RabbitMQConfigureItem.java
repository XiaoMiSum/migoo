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

package protocol.xyz.migoo.rabbitmq.config;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.rabbitmq.RabbitMQConstantsInterface;

import java.util.Map;
import java.util.Objects;

/**
 * @author xiaomi
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RabbitMQConfigureItem implements ConfigureItem<RabbitMQConfigureItem>, RabbitMQConstantsInterface {

    @JSONField(name = REF)
    private String ref;

    @JSONField(name = VIRTUAL_HOST, ordinal = 1)
    private String virtualHost;

    @JSONField(name = HOST, ordinal = 2)
    private String host;

    @JSONField(name = PORT, ordinal = 3)
    private String port;

    @JSONField(name = USERNAME, ordinal = 4)
    private String username;

    @JSONField(name = PASSWORD, ordinal = 5)
    private String password;

    @JSONField(name = MESSAGE, ordinal = 6)
    private Object message;

    @JSONField(name = TIMEOUT, ordinal = 7)
    private Integer timeout;

    @JSONField(name = QUEUE_CONFIG, ordinal = 8)
    private Queue queue;

    @JSONField(name = EXCHANGE, ordinal = 9)
    private Exchange exchange;

    @JSONField(name = PROPS, ordinal = 10)
    private Props props;

    public RabbitMQConfigureItem() {
    }

    @Override
    public RabbitMQConfigureItem merge(RabbitMQConfigureItem other) {
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
    public RabbitMQConfigureItem calc(ContextWrapper context) {
        ref = (String) context.eval(ref);
        virtualHost = (String) context.eval(virtualHost);
        host = (String) context.eval(host);
        port = (String) context.eval(port);
        username = (String) context.eval(username);
        password = (String) context.eval(password);
        message = context.eval(message);
        queue = (Queue) context.eval(queue);
        exchange = (Exchange) context.eval(exchange);
        props = (Props) context.eval(props);
        return this;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getVirtualHost() {
        return virtualHost;
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
        private String name;

        @JSONField(name = QUEUE_DURABLE, ordinal = 1)
        private Boolean durable;

        @JSONField(name = QUEUE_EXCLUSIVE, ordinal = 2)
        private Boolean exclusive;

        @JSONField(name = QUEUE_AUTO_DELETE, ordinal = 3)
        private Boolean autoDelete;

        @JSONField(name = QUEUE_ARGUMENTS, ordinal = 4)
        private Map<String, Object> arguments;

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
        public Queue calc(ContextWrapper context) {
            name = (String) context.eval(name);
            durable = (Boolean) context.eval(durable);
            exclusive = (Boolean) context.eval(exclusive);
            autoDelete = (Boolean) context.eval(autoDelete);
            arguments = (Map<String, Object>) context.eval(arguments);
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
    }

    public static class Exchange implements ConfigureItem<Exchange> {

        @JSONField(name = EXCHANGE_NAME)
        private String name;
        @JSONField(name = EXCHANGE_TYPE, ordinal = 1)
        private String type;
        @JSONField(name = EXCHANGE_ROUTING_KEY, ordinal = 2)
        private String routingKey;

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
        public Exchange calc(ContextWrapper context) {
            name = (String) context.eval(name);
            type = (String) context.eval(type);
            routingKey = (String) context.eval(routingKey);
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
    }

    public static class Props implements ConfigureItem<Props> {

        @JSONField(name = PROPS_CONTENT_TYPE)
        private String contentType;
        @JSONField(name = PROPS_CONTENT_ENCODING, ordinal = 1)
        private String contentEncoding;
        @JSONField(name = PROPS_HEADERS, ordinal = 2)
        private Map<String, Object> headers;
        @JSONField(name = PROPS_DELIVERY_MODE, ordinal = 3)
        private Integer deliveryMode;
        @JSONField(name = PROPS_PRIORITY, ordinal = 4)
        private Integer priority;
        @JSONField(name = PROPS_CORRELATION_ID, ordinal = 5)
        private String correlationId;
        @JSONField(name = PROPS_REPLY_TO, ordinal = 6)
        private String replyTo;
        @JSONField(name = PROPS_EXPIRATION, ordinal = 7)
        private String expiration;
        @JSONField(name = PROPS_MESSAGE_ID, ordinal = 8)
        private String messageId;
        @JSONField(name = PROPS_TIMESTAMP, ordinal = 9)
        private String type;
        @JSONField(name = PROPS_USER_ID, ordinal = 11)
        private String userId;
        @JSONField(name = PROPS_APP_ID, ordinal = 12)
        private String appId;
        @JSONField(name = PROPS_CLUSTER_ID, ordinal = 13)
        private String clusterId;

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
        public Props calc(ContextWrapper context) {
            contentType = (String) context.eval(contentType);
            contentEncoding = (String) context.eval(contentEncoding);
            headers = (Map<String, Object>) context.eval(headers);
            deliveryMode = (Integer) context.eval(deliveryMode);
            priority = (Integer) context.eval(priority);
            correlationId = (String) context.eval(correlationId);
            replyTo = (String) context.eval(replyTo);
            expiration = (String) context.eval(expiration);
            messageId = (String) context.eval(messageId);
            type = (String) context.eval(type);
            userId = (String) context.eval(userId);
            appId = (String) context.eval(appId);
            clusterId = (String) context.eval(clusterId);
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
    }


}
