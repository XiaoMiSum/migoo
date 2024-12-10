package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

public class ActiveMQ extends El {

    public ActiveMQ username(String username) {
        p("username", username);
        return this;
    }

    public ActiveMQ password(String password) {
        p("password", password);
        return this;
    }

    public ActiveMQ brokerUrl(String brokerUrl) {
        p("brokerUrl", brokerUrl);
        return this;
    }

    public ActiveMQ queue(String queue) {
        p("queue", queue);
        return this;
    }

    public ActiveMQ topic(String topic) {
        p("topic", topic);
        return this;
    }

    public ActiveMQ message(Object message) {
        p("message", message);
        return this;
    }

}
