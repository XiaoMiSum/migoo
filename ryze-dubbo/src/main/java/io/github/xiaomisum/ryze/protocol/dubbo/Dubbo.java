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

package io.github.xiaomisum.ryze.protocol.dubbo;

import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.protocol.dubbo.config.DubboConfigureItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/10 21:10
 */
public class Dubbo {

    public static ReferenceConfig<GenericService> handleRequest(DubboConfigureItem config) {
        var registryConfig = config.getRegistry();
        var referenceConfig = config.getReference();
        var request = new ReferenceConfig<GenericService>();
        request.setGeneric("true");
        request.getApplicationModel().getApplicationConfigManager().setApplication(new ApplicationConfig("ryze-dubbo-consumer"));
        request.setVersion(referenceConfig.getVersion());
        request.setGroup(referenceConfig.getGroup());
        request.setRetries(referenceConfig.getRetries());
        request.setTimeout(referenceConfig.getTimeout());
        request.setAsync(referenceConfig.getAsync());
        request.setLoadbalance(referenceConfig.getLoadBalance());
        request.setInterface(config.getInterfaceName());
        var registry = new RegistryConfig();
        registry.setAddress(StringUtils.isBlank(registryConfig.getProtocol()) ? registryConfig.getAddress()
                : registryConfig.getProtocol() + "://" + registryConfig.getAddress());
        registry.setGroup(registryConfig.getGroup());
        registry.setUsername(registryConfig.getUsername());
        registry.setPassword(registryConfig.getPassword());
        registry.setVersion(registryConfig.getVersion());
        registry.setTimeout(registry.getTimeout());
        request.setRegistry(registry);
        return request;
    }

    public static Object execute(ReferenceConfig<GenericService> request, DubboConfigureItem config, DefaultSampleResult result) {
        try {
            if (Objects.nonNull(config.getAttachmentArgs()) && !config.getAttachmentArgs().isEmpty()) {
                RpcContext.getClientAttachment().setAttachments(config.getAttachmentArgs());
            }
            var parameterTypes = Objects.isNull(config.getParameterTypes()) ? new String[0] : config.getParameterTypes().toArray(String[]::new);
            var parameters = Objects.isNull(config.getParameters()) ? new Object[0] : config.getParameters().toArray();
            result.sampleStart();
            return request.get().$invoke(config.getMethod(), parameterTypes, parameters);
        } finally {
            result.sampleEnd();
        }
    }
}
