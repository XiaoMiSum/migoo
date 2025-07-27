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
import protocol.xyz.migoo.kafka.KafkaConstantsInterface;

import java.util.Objects;

/**
 * @author xiaomi
 */
public class RabbitMQConfigureItem implements ConfigureItem<RabbitMQConfigureItem>, KafkaConstantsInterface {

    @JSONField(name = REF)
    private String ref;

    @JSONField(name = BOOTSTRAP_SERVERS)
    private String bootstrapServers;

    @JSONField(name = KAFKA_TOPIC, ordinal = 1)
    private String topic;

    @JSONField(name = KAFKA_KEY, ordinal = 2)
    private String key;

    @JSONField(name = KAFKA_MESSAGE, ordinal = 3)
    private Object message;

    @JSONField(name = KEY_SERIALIZER, ordinal = 5)
    private String keySerializer;

    @JSONField(name = VALUE_SERIALIZER, ordinal = 6)
    private String valueSerializer;

    @JSONField(name = ACKS, ordinal = 7)
    private Integer acks;

    @JSONField(name = RETRIES, ordinal = 8)
    private Integer retries;

    @JSONField(name = LINGER_MS, ordinal = 9)
    private Integer lingerMs;

    @Override
    public KafkaConfigureItem merge(KafkaConfigureItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.ref = StringUtils.isBlank(self.ref) ? localOther.ref : self.ref;
        self.bootstrapServers = StringUtils.isBlank(self.bootstrapServers) ? localOther.bootstrapServers : self.bootstrapServers;
        self.topic = StringUtils.isBlank(self.topic) ? localOther.topic : self.topic;
        self.key = StringUtils.isBlank(self.key) ? localOther.key : self.key;
        self.message = Objects.isNull(message) ? localOther.message : self.message;
        self.keySerializer = StringUtils.isBlank(self.keySerializer) ? localOther.keySerializer : self.keySerializer;
        self.valueSerializer = StringUtils.isBlank(self.valueSerializer) ? localOther.valueSerializer : self.valueSerializer;
        self.acks = Objects.isNull(acks) ? localOther.acks : self.acks;
        self.retries = Objects.isNull(retries) ? localOther.retries : self.retries;
        self.lingerMs = Objects.isNull(lingerMs) ? localOther.lingerMs : self.lingerMs;
        return self;
    }

    @Override
    public KafkaConfigureItem calc(ContextWrapper context) {
        ref = (String) context.eval(ref);
        bootstrapServers = (String) context.eval(bootstrapServers);
        topic = (String) context.eval(topic);
        key = (String) context.eval(key);
        keySerializer = (String) context.eval(keySerializer);
        valueSerializer = (String) context.eval(valueSerializer);
        message = context.eval(message);
        return this;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getKeySerializer() {
        return StringUtils.isBlank(keySerializer) ? "org.apache.kafka.common.serialization.StringSerializer" : valueSerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return StringUtils.isBlank(valueSerializer) ? "org.apache.kafka.common.serialization.StringSerializer" : valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public Integer getAcks() {
        return Objects.isNull(acks) ? 1 : acks;
    }

    public void setAcks(Integer acks) {
        this.acks = acks;
    }

    public Integer getRetries() {
        return Objects.isNull(retries) ? 5 : retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getLingerMs() {
        return Objects.isNull(lingerMs) ? 20 : lingerMs;
    }

    public void setLingerMs(Integer lingerMs) {
        this.lingerMs = lingerMs;
    }

}
