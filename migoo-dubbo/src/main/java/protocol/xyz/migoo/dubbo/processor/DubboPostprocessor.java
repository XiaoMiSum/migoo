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

package protocol.xyz.migoo.dubbo.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.processor.AbstractProcessor;
import core.xyz.migoo.testelement.processor.Postprocessor;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;
import protocol.xyz.migoo.dubbo.DubboConstantsInterface;
import protocol.xyz.migoo.dubbo.RealDubboRequest;
import protocol.xyz.migoo.dubbo.config.DubboConfigureItem;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/13 20:08
 */
@Alias({"dubbo_postprocessor", "dubbo_post_processor", "dubbo"})
public class DubboPostprocessor extends AbstractProcessor<DubboPostprocessor, DubboConfigureItem, DefaultSampleResult> implements Postprocessor, DubboConstantsInterface {

    @JSONField(serialize = false)
    private ReferenceConfig<GenericService> request;

    @JSONField(serialize = false)
    private Object response;

    public DubboPostprocessor() {
        super();
    }

    public DubboPostprocessor(Builder builder) {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        try {
            if (Objects.nonNull(runtime.getConfig().getAttachmentArgs()) && !runtime.getConfig().getAttachmentArgs().isEmpty()) {
                RpcContext.getClientAttachment().setAttachments(runtime.getConfig().getAttachmentArgs());
            }
            var parameterTypes = Objects.isNull(runtime.getConfig().getParameterTypes()) ? new String[0] : runtime.getConfig().getParameterTypes().toArray(String[]::new);
            var parameters = Objects.isNull(runtime.getConfig().getParameters()) ? new Object[0] : runtime.getConfig().getParameters().toArray();
            result.sampleStart();
            response = request.get().$invoke(runtime.getConfig().getMethod(), parameterTypes, parameters);
        } catch (Exception e) {
            result.setTrack(e);
        } finally {
            result.sampleEnd();
        }
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.getConfig()) ? new DubboConfigureItem() : runtime.getConfig();
        var ref = StringUtils.isBlank(localConfig.getRef()) ? DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (DubboConfigureItem) context.getLocalVariablesWrapper().get(ref);
        runtime.setConfig(localConfig.merge(otherConfig));
        // 2. 创建Dubbo对象
        var registryConfig = runtime.getConfig().getRegistry();
        var referenceConfig = runtime.getConfig().getReference();

        request = new ReferenceConfig<>();
        request.setGeneric("true");
        request.getApplicationModel().getApplicationConfigManager().setApplication(new ApplicationConfig("migoo-dubbo-consumer"));
        request.setVersion(referenceConfig.getVersion());
        request.setGroup(referenceConfig.getGroup());
        request.setRetries(referenceConfig.getRetries());
        request.setTimeout(referenceConfig.getTimeout());
        request.setAsync(referenceConfig.getAsync());
        request.setLoadbalance(referenceConfig.getLoadBalance());
        request.setInterface(runtime.getConfig().getInterfaceName());
        var registry = new RegistryConfig();
        var protocol = DubboConfigureItem.Protocol.valueOf(registryConfig.getProtocol());
        registry.setAddress(protocol.getProtocol() + registryConfig.getAddress());
        registry.setGroup(registryConfig.getGroup());
        registry.setUsername(registryConfig.getUsername());
        registry.setPassword(registryConfig.getPassword());
        registry.setVersion(registryConfig.getVersion());
        registry.setTimeout(registry.getTimeout());
        request.setRegistry(registry);
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(RealDubboRequest.build(runtime.getConfig(), request.getRegistry().getAddress()));
        result.setResponse(SampleResult.DefaultReal.build(JSON.toJSONBytes(response)));
    }

    public static class Builder extends AbstractProcessor.PostprocessorBuilder<DubboPostprocessor, Builder, DubboConfigureItem,
            AbstractTestElement.ConfigureBuilder<?, DubboConfigureItem>, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public DubboPostprocessor build() {
            return new DubboPostprocessor(this);
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }
    }
}
