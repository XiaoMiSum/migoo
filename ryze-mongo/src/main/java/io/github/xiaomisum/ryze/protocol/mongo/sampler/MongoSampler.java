/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package io.github.xiaomisum.ryze.protocol.mongo.sampler;

import com.alibaba.fastjson2.annotation.JSONField;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import io.github.xiaomisum.ryze.core.builder.DefaultAssertionsBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.mongo.Mongo;
import io.github.xiaomisum.ryze.protocol.mongo.MongoConstantsInterface;
import io.github.xiaomisum.ryze.protocol.mongo.MongoRealRequest;
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoPostprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.mongo.builder.MongoPreprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.mongo.config.MongoConfigItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author xiaomi
 */
@KW(value = {"mongo_sampler", "mongo", "mongodb"})
public class MongoSampler extends AbstractSampler<MongoSampler, MongoConfigItem, DefaultSampleResult> implements MongoConstantsInterface {

    @JSONField(serialize = false, deserialize = false)
    private MongoClientSettings settings;
    @JSONField(serialize = false, deserialize = false)
    private byte[] response;

    public MongoSampler(Builder builder) {
        super(builder);
    }

    public MongoSampler() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.getId(), StringUtils.isBlank(runtime.getTitle()) ? "MONGO 后置处理器" : runtime.getTitle());
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        response = Mongo.execute(settings, runtime.getConfig(), result);
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.getConfig()) ? new MongoConfigItem() : runtime.getConfig();
        var datasource = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (MongoConfigItem) context.getLocalVariablesWrapper().get(datasource);
        runtime.setConfig(localConfig.merge(otherConfig));
        settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(runtime.getConfig().getUrl())).retryWrites(true).build();
        result.setRequest(MongoRealRequest.build(runtime.getConfig()));
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setResponse(SampleResult.DefaultReal.build(response));
    }

    /**
     * Mongo 取样器构建器
     */
    public static class Builder extends AbstractSampler.Builder<MongoSampler, MongoSampler.Builder, MongoConfigItem,
            MongoConfigItem.Builder, MongoConfigureElementsBuilder, MongoPreprocessorsBuilder, MongoPostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {

        public Builder() {
        }

        @Override
        public MongoSampler build() {
            return new MongoSampler(this);
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
        protected MongoConfigureElementsBuilder getConfiguresBuilder() {
            return MongoConfigureElementsBuilder.builder();
        }

        @Override
        protected MongoPreprocessorsBuilder getPreprocessorsBuilder() {
            return MongoPreprocessorsBuilder.builder();
        }

        @Override
        protected MongoPostprocessorsBuilder getPostprocessorsBuilder() {
            return MongoPostprocessorsBuilder.builder();
        }

        @Override
        protected MongoConfigItem.Builder getConfigureItemBuilder() {
            return MongoConfigItem.builder();
        }
    }
}
