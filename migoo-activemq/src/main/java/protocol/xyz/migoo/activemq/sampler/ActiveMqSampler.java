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

package protocol.xyz.migoo.activemq.sampler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.builder.DefaultAssertionsBuilder;
import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import core.xyz.migoo.testelement.sampler.Sampler;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.activemq.ActiveMqConstantsInterface;
import protocol.xyz.migoo.activemq.RealActiveRequest;
import protocol.xyz.migoo.activemq.builder.ActivePostprocessorsBuilder;
import protocol.xyz.migoo.activemq.builder.ActivePreprocessorsBuilder;
import protocol.xyz.migoo.activemq.config.ActiveConfigureItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/10 21:10
 */
@Alias({"activemq", "activemq_sampler", "active_mq_sampler"})
@SuppressWarnings({"unchecked", "rawtypes"})
public class ActiveMqSampler extends AbstractSampler<ActiveMqSampler, ActiveConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, ActiveMqConstantsInterface {

    @JSONField(serialize = false)
    private RealActiveRequest request;
    @JSONField(serialize = false)
    private ConnectionFactory factory;
    @JSONField(serialize = false)
    private Connection connection;
    @JSONField(serialize = false)
    private Session session;
    @JSONField(serialize = false)
    private MessageProducer producer;

    public ActiveMqSampler() {
        super();
    }

    public ActiveMqSampler(Builder builder) {
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
        var message = switch (runtime.config.getMessage()) {
            case Map map -> JSON.toJSONString(map);
            case List list -> JSON.toJSONString(list);
            case null -> "";
            default -> runtime.config.getMessage().toString();
        };
        try {
            result.sampleStart();
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            var destination = StringUtils.isNotBlank(runtime.config.getQueue()) ? session.createQueue(runtime.config.getQueue())
                    : session.createTopic(runtime.config.getTopic());
            producer = session.createProducer(destination);
            var textMessage = session.createTextMessage(message);
            producer.send(textMessage);
        } catch (Exception e) {
            result.setTrack(e);
        } finally {
            result.sampleEnd();
            this.request = RealActiveRequest.build(runtime.config, message);
        }
    }


    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.getConfig()) ? new ActiveConfigureItem() : runtime.getConfig();
        var ref = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (ActiveConfigureItem) context.getLocalVariablesWrapper().get(ref);
        runtime.setConfig(localConfig.merge(otherConfig));
        // 2. 创建ActiveMQ 连接池;
        factory = new ActiveMQConnectionFactory(runtime.config.getUsername(), runtime.config.getPassword(), runtime.config.getBrokerUrl());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(request);
        result.setResponse(SampleResult.DefaultReal.build(new byte[0]));
        if (producer != null) {
            try {
                producer.close();
            } catch (Exception ignored) {
            }
        }
        if (session != null) {
            try {
                session.close();
            } catch (Exception ignored) {
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static class Builder extends AbstractSampler.Builder<ActiveMqSampler, Builder, ActiveConfigureItem,
            ActiveConfigureItem.Builder, ActivePreprocessorsBuilder, ActivePostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public ActiveMqSampler build() {
            return new ActiveMqSampler(this);
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
        protected ActivePreprocessorsBuilder getPreprocessorsBuilder() {
            return ActivePreprocessorsBuilder.builder();
        }

        @Override
        protected ActivePostprocessorsBuilder getPostprocessorsBuilder() {
            return ActivePostprocessorsBuilder.builder();
        }

        @Override
        protected ActiveConfigureItem.Builder getConfigureItemBuilder() {
            return ActiveConfigureItem.builder();
        }
    }
}
