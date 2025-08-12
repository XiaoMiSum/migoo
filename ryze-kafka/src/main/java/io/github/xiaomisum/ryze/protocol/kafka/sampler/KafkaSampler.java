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

package io.github.xiaomisum.ryze.protocol.kafka.sampler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.builder.DefaultAssertionsBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;
import io.github.xiaomisum.ryze.protocol.kafka.KafkaConstantsInterface;
import io.github.xiaomisum.ryze.protocol.kafka.RealKafkaRequest;
import io.github.xiaomisum.ryze.protocol.kafka.builder.KafkaConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.kafka.builder.KafkaPostprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.kafka.builder.KafkaPreprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.kafka.config.KafkaConfigureItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author mi.xiao
 * @date 2021/4/10 21:10
 */
@KW({"kafka_sampler", "kafka"})
@SuppressWarnings({"unchecked", "rawtypes"})
public class KafkaSampler extends AbstractSampler<KafkaSampler, KafkaConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, KafkaConstantsInterface {

    @JSONField(serialize = false)
    private RealKafkaRequest request;

    @JSONField(serialize = false)
    private KafkaProducer<String, String> producer;
    @JSONField(serialize = false)
    private byte[] response;

    public KafkaSampler(Builder builder) {
        super(builder);
    }

    public KafkaSampler() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.id, runtime.title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        var message = switch (runtime.config.getMessage()) {
            case Map map -> JSON.toJSONString(map);
            case List list -> JSON.toJSONString(list);
            case null -> "";
            default -> runtime.config.getMessage().toString();
        };
        try {
            result.sampleStart();
            var record = new ProducerRecord<>(runtime.config.getTopic(), runtime.config.getKey(), message);
            Future<RecordMetadata> future = producer.send(record);
            response = ("offset: " + future.get().offset()).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            result.sampleEnd();
            this.request = RealKafkaRequest.build(runtime.config, message);
        }
    }


    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.getConfig()) ? new KafkaConfigureItem() : runtime.getConfig();
        var ref = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (KafkaConfigureItem) context.getLocalVariablesWrapper().get(ref);
        runtime.setConfig(localConfig.merge(otherConfig));
        // 2. 创建Kafka对象
        var props = new Properties();
        props.put(BOOTSTRAP_SERVERS, runtime.config.getBootstrapServers());
        props.put(ACKS, runtime.getConfig().getAcks().toString());
        props.put(RETRIES, runtime.config.getRetries());
        props.put(LINGER_MS, runtime.config.getLingerMs());
        props.put(KEY_SERIALIZER, runtime.config.getKeySerializer());
        props.put(VALUE_SERIALIZER, runtime.config.getValueSerializer());
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(request);
        result.setResponse(SampleResult.DefaultReal.build(response));
    }

    public static class Builder extends AbstractSampler.Builder<KafkaSampler, Builder, KafkaConfigureItem,
            KafkaConfigureItem.Builder, KafkaConfigureElementsBuilder, KafkaPreprocessorsBuilder, KafkaPostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public KafkaSampler build() {
            return new KafkaSampler(this);
        }

        @Override
        protected DefaultAssertionsBuilder getAssertionsBuilder() {
            return DefaultAssertionsBuilder.builder();
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected KafkaConfigureElementsBuilder getConfiguresBuilder() {
            return KafkaConfigureElementsBuilder.builder();
        }

        @Override
        protected KafkaPreprocessorsBuilder getPreprocessorsBuilder() {
            return KafkaPreprocessorsBuilder.builder();
        }

        @Override
        protected KafkaPostprocessorsBuilder getPostprocessorsBuilder() {
            return KafkaPostprocessorsBuilder.builder();
        }

        @Override
        protected KafkaConfigureItem.Builder getConfigureItemBuilder() {
            return KafkaConfigureItem.builder();
        }
    }
}
