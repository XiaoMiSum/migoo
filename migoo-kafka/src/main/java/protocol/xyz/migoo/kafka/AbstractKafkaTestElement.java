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

package protocol.xyz.migoo.kafka;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import protocol.xyz.migoo.kafka.config.KafkaDefaults;
import protocol.xyz.migoo.kafka.util.KafkaConstantsInterface;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author xiaomi
 * Created in 2021/11/11 11:08
 */
public abstract class AbstractKafkaTestElement extends AbstractTestElement implements KafkaConstantsInterface {

    private KafkaProducer<String, String> producer;

    private void buildKafkaProducer() {
        var props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, get(BOOTSTRAP_SERVERS_CONFIG));
        props.put(ACKS_CONFIG, getPropertyAsString(ACKS_CONFIG));
        props.put(RETRIES_CONFIG, get(RETRIES_CONFIG, 5));
        props.put(LINGER_MS_CONFIG, get(LINGER_MS_CONFIG, 20));
        props.put(KEY_SERIALIZER_CLASS_CONFIG, get(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer"));
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, get(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer"));
        this.producer = new KafkaProducer<>(props);
    }

    public void testStarted() {
        var other = (KafkaDefaults) getVariables().get(KAFKA_DEFAULT);
        if (Objects.nonNull(other)) {
            setProperty(BOOTSTRAP_SERVERS_CONFIG, other.get(BOOTSTRAP_SERVERS_CONFIG));
            setProperty(ACKS_CONFIG, other.getPropertyAsString(ACKS_CONFIG));
            setProperty(RETRIES_CONFIG, other.getPropertyAsInt(RETRIES_CONFIG));
            setProperty(LINGER_MS_CONFIG, other.getPropertyAsLong(LINGER_MS_CONFIG));
            setProperty(KEY_SERIALIZER_CLASS_CONFIG, other.get(KEY_SERIALIZER_CLASS_CONFIG));
            setProperty(VALUE_SERIALIZER_CLASS_CONFIG, other.get(VALUE_SERIALIZER_CLASS_CONFIG));
            setProperty(KAFKA_TOPIC, other.getPropertyAsString(KAFKA_TOPIC));
        }
        this.buildKafkaProducer();
    }

    protected SampleResult execute(SampleResult result) {
        result.setTestClass(this.getClass());
        result.sampleStart();
        result.setSamplerData(getSamplerData());
        try {
            var record = new ProducerRecord<>(getPropertyAsString(KAFKA_TOPIC), (String) get(KAFKA_KEY), getPropertyAsString(KAFKA_MESSAGE));
            Future<RecordMetadata> future = producer.send(record);
            result.setResponseData("offset: " + future.get().offset());
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    private String getSamplerData() {
        var json = new JSONObject(16);
        json.put(BOOTSTRAP_SERVERS_CONFIG, get(BOOTSTRAP_SERVERS_CONFIG));
        json.put(ACKS_CONFIG, get(ACKS_CONFIG));
        json.put(RETRIES_CONFIG, get(RETRIES_CONFIG));
        json.put(LINGER_MS_CONFIG, get(LINGER_MS_CONFIG));
        json.put(KEY_SERIALIZER_CLASS_CONFIG, get(KEY_SERIALIZER_CLASS_CONFIG));
        json.put(VALUE_SERIALIZER_CLASS_CONFIG, get(VALUE_SERIALIZER_CLASS_CONFIG));
        json.put(KAFKA_TOPIC, get(KAFKA_TOPIC));
        json.put(KAFKA_MESSAGE, get(KAFKA_MESSAGE));
        json.put(KAFKA_KEY, get(KAFKA_KEY));
        return json.toString();
    }

    public void testEnded() {
        producer.close();
    }
}
