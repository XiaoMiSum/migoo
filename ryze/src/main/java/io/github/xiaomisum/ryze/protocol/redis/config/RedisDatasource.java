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

package io.github.xiaomisum.ryze.protocol.redis.config;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.TestSuiteResult;
import io.github.xiaomisum.ryze.core.testelement.configure.AbstractConfigureElement;
import io.github.xiaomisum.ryze.protocol.redis.RedisConstantsInterface;
import io.github.xiaomisum.ryze.support.Closeable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.JedisURIHelper;

import java.net.URI;
import java.util.Objects;

/**
 * @author xiaomi
 */
@KW(value = {"redis", "redis_datasource", "redis_data_source"})
public class RedisDatasource extends AbstractConfigureElement<RedisDatasource, RedisConfigureItem, TestSuiteResult>
        implements Closeable, RedisConstantsInterface {

    @JSONField(serialize = false)
    private JedisPool jedisPool;

    public RedisDatasource() {
        super();
    }

    public RedisDatasource(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void doProcess(ContextWrapper context) {
        var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(runtime.getConfig().getMaxTotal());
        poolConfig.setMaxIdle(runtime.getConfig().getMaxIdle());
        poolConfig.setMinIdle(runtime.getConfig().getMinIdle());
        var uri = URI.create(runtime.getConfig().url);
        var username = JedisURIHelper.getUser(uri);
        var password = JedisURIHelper.getPassword(uri);
        var database = JedisURIHelper.getDBIndex(uri);
        jedisPool = new JedisPool(poolConfig, uri.getHost(), uri.getPort(), runtime.getConfig().getTimeout(), username, password, database);
        context.getSessionRunner().getContext().getLocalVariablesWrapper().put(refName, this);
    }

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult("Redis数据源配置：" + refName);
    }

    @Override
    public void close() {
        if (Objects.isNull(jedisPool)) {
            return;
        }
        jedisPool.close();
    }

    public Jedis getConnection() {
        return jedisPool.getResource();
    }

    public String getUrl() {
        return runtime.getConfig().getUrl();
    }

    /**
     * Redis数据源 测试元件 构建类
     */
    public static class Builder extends AbstractConfigureElement.Builder<RedisDatasource, Builder, RedisConfigureItem, RedisConfigureItem.Builder, TestSuiteResult> {

        @Override
        public RedisDatasource build() {
            return new RedisDatasource(this);
        }

        @Override
        protected RedisConfigureItem.Builder getConfigureItemBuilder() {
            return RedisConfigureItem.builder();
        }
    }
}