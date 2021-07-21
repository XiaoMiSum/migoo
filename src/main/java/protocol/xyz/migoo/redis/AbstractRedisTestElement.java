/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package protocol.xyz.migoo.redis;

import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import java.util.Collection;
import java.util.Locale;

/**
 * @author xiaomi
 */
public abstract class AbstractRedisTestElement extends AbstractTestElement {

    private static final String COMMAND = "command";
    private static final String SEND = "send";

    protected SampleResult execute(Jedis conn) throws UnsupportedOperationException {
        return execute(conn, new SampleResult(getPropertyAsString(TITLE)));
    }

    protected SampleResult execute(Jedis conn, SampleResult sample) throws UnsupportedOperationException {
        sample.setTestClass(this.getClass());
        try {
            sample.sampleStart();
            String command = getPropertyAsString(COMMAND);
            if (StringUtils.isBlank(command)) {
                throw new UnsupportedOperationException("Unexpected command: " + command);
            }
            Object result = conn.sendCommand(Protocol.Command.valueOf(command.toUpperCase(Locale.ROOT)), getPropertyAsString(SEND).split(","));
            sample.setSamplerData("{\"command\": \"" + command + "\", \"send\": \"" + getPropertyAsString(SEND) + "\"}");
            sample.setResponseData(result == null ? "" : result instanceof Collection ? listToString((Collection) result) : result.toString());
        } finally {
            sample.sampleEnd();
       }
        return sample;
    }

    public static String listToString(Collection<?> list) {
        StringBuilder sb = new StringBuilder("[");
        list.forEach(item -> {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(item instanceof String ? "\"" + item + "\"" : item);
        });
        return sb.append("]").toString();
    }
}