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

package protocol.xyz.migoo.kafka.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.processor.AbstractProcessor;
import core.xyz.migoo.testelement.processor.Preprocessor;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import protocol.xyz.migoo.kafka.KafkaConstantsInterface;
import protocol.xyz.migoo.kafka.RealKafkaRequest;
import protocol.xyz.migoo.kafka.config.KafkaConfigureItem;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author mi.xiao
 * @date 2021/4/13 20:08
 */
@Alias({"kafka_preprocessor", "kafka_pre_processor", "kafka"})
@SuppressWarnings({"unchecked", "rawtypes"})
public class KafkaPreprocessor extends AbstractProcessor<KafkaPreprocessor, KafkaConfigureItem, DefaultSampleResult> implements Preprocessor, KafkaConstantsInterface {

    @JSONField(serialize = false)
    private RealKafkaRequest request;

    @JSONField(serialize = false)
    private KafkaProducer<String, String> producer;
    @JSONField(serialize = false)
    private byte[] response;

    public KafkaPreprocessor() {
        super(new Builder());
    }

    public KafkaPreprocessor(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        var message = switch (runtime.getConfig().getMessage()) {
            case Map map -> JSON.toJSONString(map);
            case List list -> JSON.toJSONString(list);
            case null -> "";
            default -> runtime.getConfig().getMessage().toString();
        };
        try {
            result.sampleStart();
            var record = new ProducerRecord<>(runtime.getConfig().getTopic(), runtime.getConfig().getKey(), message);
            Future<RecordMetadata> future = producer.send(record);
            response = ("offset: " + future.get().offset()).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            result.setTrack(e);
        } finally {
            result.sampleEnd();
            this.request = RealKafkaRequest.build(runtime.getConfig(), message);
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
        props.put(BOOTSTRAP_SERVERS, runtime.getConfig().getBootstrapServers());
        props.put(ACKS, runtime.getConfig().getAcks());
        props.put(RETRIES, runtime.getConfig().getRetries());
        props.put(LINGER_MS, runtime.getConfig().getLingerMs());
        props.put(KEY_SERIALIZER, runtime.getConfig().getKeySerializer());
        props.put(VALUE_SERIALIZER, runtime.getConfig().getValueSerializer());
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(request);
        result.setResponse(SampleResult.DefaultReal.build(response));
    }

    public static class Builder extends AbstractProcessor.PreprocessorBuilder<KafkaPreprocessor, Builder, KafkaConfigureItem,
            AbstractTestElement.ConfigureBuilder<?, KafkaConfigureItem>, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public KafkaPreprocessor build() {
            return new KafkaPreprocessor(this);
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }
    }
}
