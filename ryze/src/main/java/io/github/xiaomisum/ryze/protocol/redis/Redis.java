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

package io.github.xiaomisum.ryze.protocol.redis;

import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.protocol.redis.config.RedisDatasource;
import redis.clients.jedis.Protocol;

import java.util.List;

/**
 * @author xiaomi
 */
public class Redis {

    public static byte[] execute(RedisDatasource datasource, String command, String args, DefaultSampleResult result) {
        result.sampleStart();
        try (var jedis = datasource.getConnection()) {
            var response = jedis.sendCommand(Protocol.Command.valueOf(command), args.split(","));
            return toBytes(response);
        } finally {
            result.sampleEnd();
        }
    }

    private static byte[] toBytes(Object result) {
        return switch (result) {
            case null -> new byte[0];
            case List<?> results -> {
                var sb = new StringBuilder("[");
                results.forEach(item -> sb.append(sb.length() > 1 ? "," : "").append(item instanceof String ? "\"" + item + "\"" : item));
                yield sb.append("]").toString().getBytes();
            }
            case byte[] bytes -> bytes;
            default -> result.toString().getBytes();
        };
    }
}
