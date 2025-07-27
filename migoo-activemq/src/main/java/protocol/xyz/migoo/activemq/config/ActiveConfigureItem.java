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

package protocol.xyz.migoo.activemq.config;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import org.apache.activemq.ActiveMQConnection;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.activemq.ActiveMqConstantsInterface;

import java.util.Objects;

/**
 * @author xiaomi
 */
public class ActiveConfigureItem implements ConfigureItem<ActiveConfigureItem>, ActiveMqConstantsInterface {

    @JSONField(name = REF)
    private String ref;

    @JSONField(name = BROKER_URL)
    private String brokerUrl;

    @JSONField(name = ACTIVEMQ_TOPIC, ordinal = 1)
    private String topic;

    @JSONField(name = ACTIVEMQ_QUEUE, ordinal = 2)
    private String queue;

    @JSONField(name = ACTIVEMQ_MESSAGE, ordinal = 3)
    private Object message;

    @JSONField(name = ACTIVEMQ_USERNAME, ordinal = 5)
    private String username;

    @JSONField(name = ACTIVEMQ_PASSWORD, ordinal = 6)
    private String password;


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
    public ActiveConfigureItem calc(ContextWrapper context) {
        ref = (String) context.eval(ref);
        brokerUrl = (String) context.eval(brokerUrl);
        topic = (String) context.eval(topic);
        queue = (String) context.eval(queue);
        username = (String) context.eval(username);
        password = (String) context.eval(password);
        message = context.eval(message);
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
}
