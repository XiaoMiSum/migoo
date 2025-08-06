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

package io.github.xiaomisum.ryze.protocol.dubbo;

import com.alibaba.fastjson2.JSON;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;
import io.github.xiaomisum.ryze.protocol.dubbo.config.DubboConfigureItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaomi
 * Created at 2025/7/26 22:24
 */
public class RealDubboRequest extends SampleResult.Real {

    private String address;

    private String interfaceName;

    private String method;

    private List<String> parameterTypes;

    private Map<String, String> attachmentArgs;

    public RealDubboRequest(byte[] bytes) {
        super(bytes);
    }

    public static RealDubboRequest build(DubboConfigureItem config, String address) {
        var result = new RealDubboRequest(JSON.toJSONBytes(config.getParameters()));
        result.address = address;
        result.interfaceName = config.getInterfaceName();
        result.method = config.getMethod();
        result.parameterTypes = config.getParameterTypes();
        result.attachmentArgs = config.getAttachmentArgs();
        return result;
    }

    @Override
    public String format() {
        var buf = new StringBuilder();
        buf.append("registry center address: ").append(address).append("\n")
                .append("interface: ").append(interfaceName).append(".").append(method).append("()");
        if (Objects.nonNull(parameterTypes)) {
            buf.append("\n").append(JSON.toJSONString(parameterTypes));
        }
        if (StringUtils.isNotBlank(bytesAsString())) {
            buf.append("\n").append("parameters: ").append(bytesAsString());
        }
        if (Objects.nonNull(attachmentArgs) && !attachmentArgs.isEmpty()) {
            buf.append("\n").append("attachmentArgs: ").append(JSON.toJSONString(attachmentArgs));
        }
        return buf.toString();
    }
}
