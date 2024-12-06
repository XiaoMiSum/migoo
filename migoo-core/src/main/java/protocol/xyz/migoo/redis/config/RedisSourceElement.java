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
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.TestStateListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author xiaomi
 */
@Alias({"RedisDataSource", "Redis_DataSource", "RedisSource", "Redis_Source"})
public class RedisSourceElement extends AbstractTestElement implements TestStateListener {

    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String DATABASE_KEY = "database";
    private static final String PASSWORD_KEY = "password";
    private static final String TIME_OUT_KEY = "time_out";
    private static final String MAX_TOTAL_KEY = "max_total";
    private static final String MAX_IDLE_KEY = "max_idle";
    private static final String MIN_IDLE_KEY = "min_idle";
    private static final String VARIABLE_NAME_KEY = "variable_name";

    @JSONField(serialize = false)
    private JedisPool jedisPool;

    private String url;

    @Override
    public void testStarted() {
        var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(getPropertyAsInt(MAX_TOTAL_KEY) > 0 ? getPropertyAsInt(MAX_TOTAL_KEY) : 10);
        poolConfig.setMaxIdle(getPropertyAsInt(MAX_IDLE_KEY) > 0 ? getPropertyAsInt(MAX_IDLE_KEY) : 5);
        poolConfig.setMinIdle(getPropertyAsInt(MIN_IDLE_KEY) > 0 ? getPropertyAsInt(MIN_IDLE_KEY) : 1);
        var timeout = getPropertyAsInt(TIME_OUT_KEY) > 0 ? getPropertyAsInt(TIME_OUT_KEY) : 10000;
        var host = getPropertyAsString(HOST_KEY);
        var port = get(PORT_KEY) == null ? 6379 : getPropertyAsInt(PORT_KEY);
        var database = get(DATABASE_KEY) == null ? 0 : getPropertyAsInt(DATABASE_KEY);
        var password = getPropertyAsString(PASSWORD_KEY);
        jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database);
        getVariables().put(getPropertyAsString(VARIABLE_NAME_KEY), this);
        url = String.format("redis://%s:%s/%s", host, port, database);
    }

    @Override
    public void testEnded() {
        if (!jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

    public Jedis getConnection() {
        return jedisPool.getResource();
    }

    public String getUrl() {
        return url;
    }
}