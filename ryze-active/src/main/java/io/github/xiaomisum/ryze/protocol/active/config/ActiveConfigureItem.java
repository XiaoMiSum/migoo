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

package io.github.xiaomisum.ryze.protocol.active.config;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.protocol.active.ActiveConstantsInterface;
import io.github.xiaomisum.ryze.support.Customizer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaomi
 */
public class ActiveConfigureItem implements ConfigureItem<ActiveConfigureItem>, ActiveConstantsInterface {

    @JSONField(name = BROKER_URL)
    protected String brokerUrl;
    @JSONField(name = ACTIVEMQ_TOPIC, ordinal = 1)
    protected String topic;
    @JSONField(name = ACTIVEMQ_QUEUE, ordinal = 2)
    protected String queue;
    @JSONField(name = ACTIVEMQ_MESSAGE, ordinal = 3)
    protected Object message;
    @JSONField(name = ACTIVEMQ_USERNAME, ordinal = 5)
    protected String username;
    @JSONField(name = ACTIVEMQ_PASSWORD, ordinal = 6)
    protected String password;
    @JSONField(name = REF)
    protected String ref;

    public ActiveConfigureItem() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ActiveConfigureItem merge(ActiveConfigureItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.ref = StringUtils.isBlank(self.ref) ? localOther.ref : self.ref;
        self.brokerUrl = StringUtils.isBlank(self.brokerUrl) ? localOther.brokerUrl : self.brokerUrl;
        self.topic = StringUtils.isBlank(self.topic) ? localOther.topic : self.topic;
        self.queue = StringUtils.isBlank(self.queue) ? localOther.queue : self.queue;
        self.message = Objects.isNull(message) ? localOther.message : self.message;
        self.username = StringUtils.isBlank(self.username) ? localOther.username : self.username;
        self.password = StringUtils.isBlank(self.password) ? localOther.password : self.password;
        return self;
    }

    @Override
    public ActiveConfigureItem evaluate(ContextWrapper context) {
        ref = (String) context.evaluate(ref);
        brokerUrl = (String) context.evaluate(brokerUrl);
        topic = (String) context.evaluate(topic);
        queue = (String) context.evaluate(queue);
        username = (String) context.evaluate(username);
        password = (String) context.evaluate(password);
        message = context.evaluate(message);
        return this;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getBrokerUrl() {
        return StringUtils.isBlank(brokerUrl) ? ActiveMQConnection.DEFAULT_BROKER_URL : brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getUsername() {
        return StringUtils.isBlank(username) ? ActiveMQConnection.DEFAULT_USER : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return StringUtils.isBlank(password) ? ActiveMQConnection.DEFAULT_PASSWORD : password;

    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Active 配置属性 构建器
     */
    public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, ActiveConfigureItem> {

        private ActiveConfigureItem configure = new ActiveConfigureItem();

        public Builder() {
        }

        public Builder ref(String ref) {
            configure.setRef(ref);
            return self;
        }

        public Builder brokerUrl(String brokerUrl) {
            configure.setBrokerUrl(brokerUrl);
            return self;
        }

        public Builder topic(String topic) {
            configure.setTopic(topic);
            return self;
        }

        public Builder queue(String queue) {
            configure.setQueue(queue);
            return self;
        }

        public Builder message(Object message) {
            configure.setMessage(message);
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

        public Builder username(String username) {
            configure.setUsername(username);
            return self;
        }

        public Builder password(String password) {
            configure.setPassword(password);
            return self;
        }

        public Builder message(String message) {
            configure.setMessage(message);
            return self;
        }

        public Builder message(List<?> message) {
            configure.setMessage(message);
            return self;
        }

        public Builder message(Map<?, ?> message) {
            configure.setMessage(message);
            return self;
        }

        public Builder config(ActiveConfigureItem config) {
            configure = configure.merge(config);
            return self;
        }

        @Override
        public ActiveConfigureItem build() {
            return configure;
        }
    }
}
