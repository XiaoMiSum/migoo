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

package io.github.xiaomisum.ryze.protocol.kafka.config;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.protocol.kafka.KafkaConstantsInterface;
import io.github.xiaomisum.ryze.support.Customizer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaomi
 */
public class KafkaConfigureItem implements ConfigureItem<KafkaConfigureItem>, KafkaConstantsInterface {

    @JSONField(name = REF)
    protected String ref;

    @JSONField(name = BOOTSTRAP_SERVERS)
    protected String bootstrapServers;

    @JSONField(name = KAFKA_TOPIC, ordinal = 1)
    protected String topic;

    @JSONField(name = KAFKA_KEY, ordinal = 2)
    protected String key;

    @JSONField(name = KAFKA_MESSAGE, ordinal = 3)
    protected Object message;

    @JSONField(name = KEY_SERIALIZER, ordinal = 5)
    protected String keySerializer;

    @JSONField(name = VALUE_SERIALIZER, ordinal = 6)
    protected String valueSerializer;

    @JSONField(name = ACKS, ordinal = 7)
    protected Integer acks;

    @JSONField(name = RETRIES, ordinal = 8)
    protected Integer retries;

    @JSONField(name = LINGER_MS, ordinal = 9)
    protected Integer lingerMs;

    public KafkaConfigureItem() {
    }

    public static Builder builder() {
        return new Builder();
    }

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
    public KafkaConfigureItem evaluate(ContextWrapper context) {
        ref = (String) context.evaluate(ref);
        bootstrapServers = (String) context.evaluate(bootstrapServers);
        topic = (String) context.evaluate(topic);
        key = (String) context.evaluate(key);
        keySerializer = (String) context.evaluate(keySerializer);
        valueSerializer = (String) context.evaluate(valueSerializer);
        message = context.evaluate(message);
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

    /**
     * Kafka 配置属性 构建器
     */
    public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, KafkaConfigureItem> {

        private KafkaConfigureItem configure = new KafkaConfigureItem();

        public Builder() {
        }

        public Builder ref(String ref) {
            configure.ref = ref;
            return self;
        }

        public Builder address(String bootstrapServers) {
            configure.bootstrapServers = bootstrapServers;
            return self;
        }

        public Builder bootstrapServers(String bootstrapServers) {
            configure.bootstrapServers = bootstrapServers;
            return self;
        }

        public Builder topic(String topic) {
            configure.topic = topic;
            return self;
        }

        public Builder key(String key) {
            configure.key = key;
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


        public Builder keySerializer(String keySerializer) {
            configure.keySerializer = keySerializer;
            return self;
        }

        public Builder valueSerializer(String valueSerializer) {
            configure.valueSerializer = valueSerializer;
            return self;
        }

        public Builder acks(Integer acks) {
            configure.acks = acks;
            return self;
        }

        public Builder retries(Integer retries) {
            configure.retries = retries;
            return self;
        }

        public Builder lingerMs(Integer lingerMs) {
            configure.lingerMs = lingerMs;
            return self;
        }

        public Builder config(KafkaConfigureItem config) {
            configure = configure.merge(config);
            return self;
        }


        @Override
        public KafkaConfigureItem build() {
            return configure;
        }
    }

}
