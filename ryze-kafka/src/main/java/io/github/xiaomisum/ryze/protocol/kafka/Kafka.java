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

package io.github.xiaomisum.ryze.protocol.kafka;

import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.protocol.kafka.config.KafkaConfigureItem;
import io.github.xiaomisum.ryze.support.Collections;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;

import static io.github.xiaomisum.ryze.protocol.kafka.KafkaConstantsInterface.*;

/**
 *
 * @author xiaomi
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Kafka {

    public static byte[] execute(KafkaConfigureItem config, String message, DefaultSampleResult result) {
        var props = Collections.of(BOOTSTRAP_SERVERS, config.getBootstrapServers(), ACKS, config.getAcks().toString(), RETRIES, config.getRetries(),
                LINGER_MS, config.getLingerMs(), KEY_SERIALIZER, config.getKeySerializer(), VALUE_SERIALIZER, config.getValueSerializer());
        try (var producer = new KafkaProducer<String, String>(props)) {
            result.sampleStart();
            var record = new ProducerRecord<>(config.getTopic(), config.getKey(), message);
            var future = producer.send(record);
            return ("offset: " + future.get().offset()).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            result.sampleEnd();
        }
    }

}
