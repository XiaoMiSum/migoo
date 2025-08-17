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

package io.github.xiaomisum.ryze.protocol.kafka;

import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.kafka.config.KafkaConfigureItem;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 * Created at 2025/7/26 22:24
 */
public class RealKafkaRequest extends SampleResult.Real {

    private String address;

    private String topic;

    private String key;

    public RealKafkaRequest(byte[] bytes) {
        super(bytes);
    }

    public static RealKafkaRequest build(KafkaConfigureItem config, String message) {
        var result = new RealKafkaRequest(message.getBytes());
        result.address = config.getBootstrapServers();
        result.topic = config.getTopic();
        result.key = config.getKey();
        return result;
    }

    @Override
    public String format() {
        var buf = new StringBuilder();
        buf.append("kafka server address: ").append(address).append("\n").append("topic: ").append(topic);
        if (StringUtils.isNotBlank(key)) {
            buf.append("\n").append("key: ").append(key);
        }
        if (StringUtils.isNotBlank(bytesAsString())) {
            buf.append("\n").append("message: ").append(bytesAsString());
        }
        return buf.toString();
    }
}
