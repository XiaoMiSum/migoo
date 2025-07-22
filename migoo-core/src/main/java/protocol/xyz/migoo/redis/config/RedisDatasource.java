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

package protocol.xyz.migoo.redis.config;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.Closeable;
import core.xyz.migoo.testelement.TestSuiteResult;
import core.xyz.migoo.testelement.configure.AbstractConfigureElement;
import protocol.xyz.migoo.redis.RedisConstantsInterface;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.JedisURIHelper;

import java.net.URI;
import java.util.Objects;

/**
 * @author xiaomi
 */
@Alias(value = {"redis", "redis_datasource", "redis_data_source"})
public class RedisDatasource extends AbstractConfigureElement<RedisConfigureItem, RedisDatasource, TestSuiteResult>
        implements Closeable, RedisConstantsInterface {

    @JSONField(serialize = false)
    private JedisPool jedisPool;

    @Override
    protected void doProcess(ContextWrapper context) {
        var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(runtime.getConfig().getMaxTotal());
        poolConfig.setMaxIdle(runtime.getConfig().getMaxIdle());
        poolConfig.setMinIdle(runtime.getConfig().getMinIdle());
        var uri = URI.create(runtime.getConfig().getUrl());
        var username = JedisURIHelper.getUser(uri);
        var password = JedisURIHelper.getPassword(uri);
        var database = JedisURIHelper.getDBIndex(uri);
        jedisPool = new JedisPool(poolConfig, uri.getHost(), uri.getPort(), runtime.getConfig().getTimeout(), username, password, database);
        context.getSessionRunner().getContextWrapper().getLocalVariablesWrapper().put(refName, this);
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
}