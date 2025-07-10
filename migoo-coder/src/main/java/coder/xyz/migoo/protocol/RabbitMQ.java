package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

import java.util.HashMap;
import java.util.Map;

public class RabbitMQ extends El {

    public RabbitMQ host(String host) {
        p("host", host);
        return this;
    }

    public RabbitMQ port(String port) {
        p("port", port);
        return this;
    }

    public RabbitMQ port(int port) {
        p("port", port);
        return this;
    }

    public RabbitMQ username(String username) {
        p("username", username);
        return this;
    }

    public RabbitMQ password(String password) {
        p("password", password);
        return this;
    }

    public RabbitMQ timeout(int timeout) {
        p("timeout", timeout);
        return this;
    }

    public Queue queue() {
        var queue = new Queue(this);
        p("queue", queue);
        return queue;
    }

    public Exchange exchange() {
        var exchange = new Exchange(this);
        p("exchange", exchange);
        return exchange;
    }

    public Props props() {
        var props = new Props(this);
        p("props", props);
        return props;
    }

    public RabbitMQ message(Object message) {
        p("message", message);
        return this;
    }

    public static class Queue extends HashMap<String, Object> {

        private final RabbitMQ rabbit;

        private Queue(RabbitMQ rabbit) {
            this.rabbit = rabbit;
        }

        public Queue name(String name) {
            put("name", name);
            return this;
        }

        public Queue durable(boolean durable) {
            put("durable", durable);
            return this;
        }

        public Queue exclusive(boolean exclusive) {
            put("exclusive", exclusive);
            return this;
        }

        public Queue autoDelete(boolean autoDelete) {
            put("auto_delete", autoDelete);
            return this;
        }

        public Queue arguments(Map<String, Object> arguments) {
            put("arguments", arguments);
            return this;
        }

        public RabbitMQ and() {
            return rabbit;
        }
    }

    public static class Exchange extends HashMap<String, Object> {
        private final RabbitMQ rabbit;

        private Exchange(RabbitMQ rabbit) {
            this.rabbit = rabbit;
        }

        public Exchange name(String name) {
            put("name", name);
            return this;
        }

        public Exchange type(String type) {
            put("type", type);
            return this;
        }

        public Exchange routingKey(String routingKey) {
            put("routing_key", routingKey);
            return this;
        }

        public RabbitMQ and() {
            return rabbit;
        }
    }

    public static class Props extends HashMap<String, Object> {

        private final RabbitMQ rabbit;

        private Props(RabbitMQ rabbit) {
            this.rabbit = rabbit;
        }

        public Props contentType(String contentType) {
            put("content_type", contentType);
            return this;
        }

        public Props contentEncoding(String contentEncoding) {
            put("content_encoding", contentEncoding);
            return this;
        }

        public Props headers(Map<String, Object> headers) {
            put("headers", headers);
            return this;
        }

        public Props deliveryMode(String deliveryMode) {
            put("delivery_mode", deliveryMode);
            return this;
        }

        public Props priority(String priority) {
            put("priority", priority);
            return this;
        }

        public Props correlationId(String correlationId) {
            put("correlation_id", correlationId);
            return this;
        }

        public Props replyTo(String replyTo) {
            put("reply_to", replyTo);
            return this;
        }

        public Props expiration(String expiration) {
            put("expiration", expiration);
            return this;
        }

        public Props messageId(String messageId) {
            put("message_id", messageId);
            return this;
        }

        public Props timestamp(String timestamp) {
            put("timestamp", timestamp);
            return this;
        }

        public Props type(String type) {
            put("type", type);
            return this;
        }

        public Props userId(String userId) {
            put("user_id", userId);
            return this;
        }

        public Props appId(String appId) {
            put("app_id", appId);
            return this;
        }

        public Props clusterId(String clusterId) {
            put("cluster_id", clusterId);
            return this;
        }

        public RabbitMQ and() {
            return rabbit;
        }
    }
}
