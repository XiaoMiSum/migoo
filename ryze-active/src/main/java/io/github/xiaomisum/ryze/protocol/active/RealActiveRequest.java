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

package io.github.xiaomisum.ryze.protocol.active;

import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.active.config.ActiveConfigureItem;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 * Created at 2025/7/26 22:24
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RealActiveRequest extends SampleResult.Real {

    private String address;

    private String topic;

    private String queue;

    private String username;

    private String password;

    public RealActiveRequest(byte[] bytes) {
        super(bytes);
    }

    public static RealActiveRequest build(ActiveConfigureItem config, String message) {
        var result = new RealActiveRequest(message.getBytes());
        result.address = config.getBrokerUrl();
        result.topic = config.getTopic();
        result.queue = config.getQueue();
        result.username = config.getUsername();
        result.password = config.getPassword();
        return result;
    }

    @Override
    public String format() {
        var buf = new StringBuilder();
        buf.append("kafka server address: ").append(address).append("\n")
                .append("username: ").append(username).append("\n")
                .append("password: ").append(password).append("\n");
        if (StringUtils.isNotBlank(topic)) {
            buf.append("\n").append("topic: ").append(topic);
        }
        if (StringUtils.isNotBlank(queue)) {
            buf.append("\n").append("queue: ").append(queue);
        }
        if (StringUtils.isNotBlank(bytesAsString())) {
            buf.append("\n").append("message: ").append(bytesAsString());
        }
        return buf.toString();
    }
}
