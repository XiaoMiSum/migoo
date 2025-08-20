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

package io.github.xiaomisum.ryze.protocol.mongo.processsor;

import com.alibaba.fastjson2.annotation.JSONField;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.processor.AbstractProcessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Postprocessor;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.mongo.Mongo;
import io.github.xiaomisum.ryze.protocol.mongo.MongoConstantsInterface;
import io.github.xiaomisum.ryze.protocol.mongo.MongoRealRequest;
import io.github.xiaomisum.ryze.protocol.mongo.config.MongoConfigItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * MongoDB 后置处理器类
 * <p>
 * 该类用于在测试步骤执行后处理 MongoDB 操作，继承自 AbstractProcessor 并实现 Postprocessor 接口。
 * 它可以在测试步骤完成后执行 MongoDB 操作，例如清理数据或执行其他操作。
 * </p>
 * <p>
 * 主要功能：
 * <ul>
 *   <li>在测试步骤执行后执行 MongoDB 操作</li>
 *   <li>处理 MongoDB 连接和配置</li>
 *   <li>执行 MongoDB 增删改查操作</li>
 *   <li>生成和处理请求/响应信息</li>
 *   <li>使用 @KW 注解支持多种关键字识别</li>
 * </ul>
 * </p>
 *
 * @author xiaomi
 */
@KW(value = {"mongo_postprocessor", "mongo_post_processor", "mongo", "mongodb"})
public class MongoPostprocessor extends AbstractProcessor<MongoPostprocessor, MongoConfigItem, DefaultSampleResult> implements Postprocessor, MongoConstantsInterface {

    /**
     * MongoDB 客户端设置，不会被序列化
     */
    @JSONField(serialize = false, deserialize = false)
    private MongoClientSettings settings;
    
    /**
     * 响应数据，不会被序列化
     */
    @JSONField(serialize = false, deserialize = false)
    private byte[] response;

    /**
     * 使用 Builder 构造方法创建 MongoPostprocessor 实例
     *
     * @param builder Builder 实例
     */
    public MongoPostprocessor(Builder builder) {
        super(builder);
    }

    /**
     * 无参构造方法
     */
    public MongoPostprocessor() {
        super();
    }

    /**
     * 创建 Builder 实例
     * <p>
     * 静态工厂方法，用于创建 MongoPostprocessor.Builder 实例。
     * </p>
     *
     * @return MongoPostprocessor.Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 获取测试结果
     * <p>
     * 创建并返回 MongoDB 后置处理器的采样结果实例。
     * </p>
     *
     * @return DefaultSampleResult 实例
     */
    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.getId(), StringUtils.isBlank(runtime.getTitle()) ? "MONGO 后置处理器" : runtime.getTitle());
    }

    /**
     * 执行采样
     * <p>
     * 执行实际的 MongoDB 操作，包括创建客户端、执行操作和处理结果。
     * </p>
     *
     * @param context 上下文包装器
     * @param result  采样结果
     */
    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        response = Mongo.execute(settings, runtime.getConfig(), result);
    }

    /**
     * 处理请求
     * <p>
     * 在执行 MongoDB 操作前处理请求，包括合并配置项、设置连接参数和构建请求信息。
     * </p>
     *
     * @param context 上下文包装器
     * @param result  采样结果
     */
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

    /**
     * 处理响应
     * <p>
     * 在执行 MongoDB 操作后处理响应，包括设置响应数据。
     * </p>
     *
     * @param context 上下文包装器
     * @param result  采样结果
     */
    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setResponse(SampleResult.DefaultReal.build(response));
    }

    /**
     * MongoDB 后置处理器构建器类
     * <p>
     * 该类用于通过 Builder 模式构建 MongoPostprocessor 实例。
     * </p>
     */
    public static class Builder extends PostprocessorBuilder<MongoPostprocessor, Builder, MongoConfigItem,
            MongoConfigItem.Builder, DefaultExtractorsBuilder, DefaultSampleResult> {
        /**
         * 构建 MongoPostprocessor 实例
         *
         * @return MongoPostprocessor 实例
         */
        @Override
        public MongoPostprocessor build() {
            return new MongoPostprocessor(this);
        }

        /**
         * 获取提取器构建器
         *
         * @return DefaultExtractorsBuilder 实例
         */
        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        /**
         * 获取配置项构建器
         *
         * @return MongoConfigItem.Builder 实例
         */
        @Override
        protected MongoConfigItem.Builder getConfigureItemBuilder() {
            return MongoConfigItem.builder();
        }
    }
}