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

package io.github.xiaomisum.ryze.protocol.rabbit;

import com.alibaba.fastjson2.JSON;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.rabbit.config.RabbitConfigureItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author xiaomi
 * Created at 2025/7/26 22:24
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class RealRabbitRequest extends SampleResult.Real {

    private String address;

    private String username;
    private String password;
    private String virtualHost;

    private RabbitConfigureItem.Queue queue;

    private RabbitConfigureItem.Exchange exchange;

    public RealRabbitRequest(byte[] bytes) {
        super(bytes);
    }

    public static RealRabbitRequest build(RabbitConfigureItem config, String message) {
        var result = new RealRabbitRequest(message.getBytes());
        result.address = config.getHost() + ":" + config.getPort();
        result.username = config.getUsername();
        result.password = config.getPassword();
        result.queue = config.getQueue();
        result.exchange = config.getExchange();
        result.virtualHost = config.getVirtualHost();
        return result;
    }

    @Override
    public String format() {
        var buf = new StringBuilder();
        buf.append("rabbit server address: ").append(address).append("\n")
                .append("username: ").append(username).append("\n")
                .append("password: ").append(password).append("\n")
                .append("virtualHost: ").append(virtualHost).append("\n");
        if (Objects.nonNull(queue)) {
            buf.append("Queue as JSON: ").append(JSON.toJSONString(queue)).append("\n");
        }
        if (Objects.nonNull(exchange)) {
            buf.append("Exchange as JSON: ").append(JSON.toJSONString(exchange)).append("\n");
        }
        if (StringUtils.isNotBlank(bytesAsString())) {
            buf.append("\n").append("message: ").append(bytesAsString());
        }
        return buf.toString();
    }
}
