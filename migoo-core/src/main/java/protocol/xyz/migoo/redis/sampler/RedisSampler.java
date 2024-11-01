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

import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.TestStateListener;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.redis.AbstractRedisTestElement;
import protocol.xyz.migoo.redis.config.RedisSourceElement;
import redis.clients.jedis.Jedis;

/**
 * @author xiaomi
 */
public class RedisSampler extends AbstractRedisTestElement implements Sampler, TestStateListener {

    @Override
    public SampleResult sample() {
        var result = new SampleResult(getPropertyAsString(TITLE));
        var dataSourceName = getPropertyAsString("datasource");
        var dataSource = (RedisSourceElement) getVariables().get(dataSourceName);
        if (StringUtils.isBlank(dataSourceName) || dataSource == null) {
            result.setSuccessful(false);
            result.setResponseData("DataSourceName is not specified or DataSource is null");
        } else {
            result.setUrl(dataSource.getUrl());
            try (Jedis conn = dataSource.getConnection()) {
                return execute(conn, result);
            } catch (Exception e) {
                result.setThrowable(e);
            }
        }
        return result;
    }

    @Override
    public void testStarted() {
        
    }

    @Override
    public void testEnded() {

    }
}