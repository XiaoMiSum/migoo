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

package protocol.xyz.migoo.redis.processor;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.processor.AbstractProcessor;
import core.xyz.migoo.testelement.processor.Preprocessor;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.redis.RealRedisRequest;
import protocol.xyz.migoo.redis.Utils;
import protocol.xyz.migoo.redis.config.RedisConfigureItem;
import protocol.xyz.migoo.redis.config.RedisDatasource;
import redis.clients.jedis.Protocol;

/**
 * @author xiaomi
 */
@Alias(value = {"redis_preprocessor", "redis_pre_processor", "redis"})
@SuppressWarnings({"unchecked", "rawtypes"})
public class RedisPreprocessor extends AbstractProcessor<RedisPreprocessor, RedisConfigureItem, DefaultSampleResult> implements Preprocessor, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private RedisDatasource datasource;
    @JSONField(serialize = false)
    private byte[] bytes;

    public RedisPreprocessor(Builder builder) {
        super(builder);
    }

    public RedisPreprocessor() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, StringUtils.isBlank(title) ? "Redis 前置处理器" : title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        result.sampleStart();
        try (var jedis = datasource.getConnection()) {
            var command = runtime.getConfig().getCommand();
            var response = jedis.sendCommand(Protocol.Command.valueOf(command), runtime.getConfig().getSend().split(","));
            bytes = Utils.toBytes(response);
        } finally {
            result.sampleEnd();
        }
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        datasource = (RedisDatasource) context.getLocalVariablesWrapper().get(runtime.getConfig().getDatasource());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealRedisRequest(datasource.getUrl(), runtime.getConfig().getCommand(), runtime.getConfig().getSend()));
        result.setResponse(SampleResult.DefaultReal.build(bytes));
    }

    /**
     * Redis 前置处理器构建器
     */
    public static class Builder extends AbstractProcessor.PreprocessorBuilder<RedisPreprocessor, Builder, RedisConfigureItem,
            RedisConfigureItem.Builder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public RedisPreprocessor build() {
            return new RedisPreprocessor(this);
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected RedisConfigureItem.Builder getConfigureItemBuilder() {
            return RedisConfigureItem.builder();
        }
    }
}
