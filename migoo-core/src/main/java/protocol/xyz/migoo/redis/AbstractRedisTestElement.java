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

package protocol.xyz.migoo.redis;

import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.redis.config.RedisSourceElement;
import protocol.xyz.migoo.redis.util.RedisConstantsInterface;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import java.util.Collection;
import java.util.Locale;

/**
 * @author xiaomi
 */
public abstract class AbstractRedisTestElement extends AbstractTestElement implements RedisConstantsInterface {


    public static String listToString(Collection<?> list) {
        var sb = new StringBuilder("[");
        list.forEach(item -> {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(item instanceof String ? "\"" + item + "\"" : item);
        });
        return sb.append("]").toString();
    }

    protected SampleResult execute() {
        var result = new SampleResult(getPropertyAsString(TITLE));
        try {
            var dataSourceName = getPropertyAsString(DATASOURCE);
            var datasource = (RedisSourceElement) getVariables().get(dataSourceName);
            if (StringUtils.isBlank(dataSourceName)) {
                throw new IllegalArgumentException("datasource name is not specified or DataSource is null");
            }
            return execute(datasource, result);
        } catch (Exception e) {
            result.sampleEnd();
            result.setThrowable(e);
        }
        return result;
    }

    protected SampleResult execute(RedisSourceElement datasource, SampleResult sample) throws UnsupportedOperationException {
        sample.setTestClass(this.getClass());
        try (Jedis jedis = datasource.getConnection()) {
            sample.sampleStart();
            var command = getPropertyAsString(COMMAND);
            sample.setSamplerData("{\"command\": \"" + command + "\", \"send\": \"" + getPropertyAsString(SEND) + "\"}");
            if (StringUtils.isBlank(command)) {
                throw new UnsupportedOperationException("unsupported command: " + command);
            }
            var result = jedis.sendCommand(Protocol.Command.valueOf(command.toUpperCase(Locale.ROOT)), getPropertyAsString(SEND).split(","));
            sample.setResponseData(result == null ? "" : result instanceof byte[] bytes ? new String(bytes) :
                    result instanceof Collection ? listToString((Collection<?>) result) : result.toString());
        } finally {
            sample.sampleEnd();
        }
        return sample;
    }
}