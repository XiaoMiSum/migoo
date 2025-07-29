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

package protocol.xyz.migoo.redis.sampler;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import core.xyz.migoo.testelement.sampler.Sampler;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.redis.RealRedisRequest;
import protocol.xyz.migoo.redis.Redis;
import protocol.xyz.migoo.redis.config.RedisConfigureItem;
import protocol.xyz.migoo.redis.config.RedisDatasource;
import redis.clients.jedis.Protocol;

import java.util.Locale;

/**
 * @author xiaomi
 */
@Alias(value = {"redis", "redis_sampler"})
public class RedisSampler extends AbstractSampler<RedisSampler, RedisConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private RedisDatasource datasource;

    private byte[] bytes;

    public RedisSampler(Builder builder) {
        super(builder);
    }

    public RedisSampler() {
        super();
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
        result.sampleStart();
        try (var jedis = datasource.getConnection()) {
            var command = runtime.config.getCommand();
            var result2 = jedis.sendCommand(Protocol.Command.valueOf(command.toUpperCase(Locale.ROOT)), runtime.config.getSend().split(","));
            bytes = Redis.toBytes(result2);
        } catch (Exception e) {
            result.setTrack(e);
        } finally {
            result.sampleEnd();
        }
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        datasource = (RedisDatasource) context.getLocalVariablesWrapper().get(runtime.config.getDatasource());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealRedisRequest(datasource.getUrl(), runtime.config.getCommand(), runtime.config.getSend()));
        result.setResponse(SampleResult.DefaultReal.build(bytes));
    }

    /**
     * Redis 取样器构建器
     */
    public static class Builder extends AbstractSampler.Builder<RedisSampler, Builder, RedisConfigureItem, RedisConfigureItem.Builder, DefaultSampleResult> {
        @Override
        public RedisSampler build() {
            return new RedisSampler(this);
        }
    }
}
