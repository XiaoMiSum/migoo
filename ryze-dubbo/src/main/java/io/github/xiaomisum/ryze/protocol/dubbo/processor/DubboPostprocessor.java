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

package io.github.xiaomisum.ryze.protocol.dubbo.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.processor.AbstractProcessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Postprocessor;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.dubbo.Dubbo;
import io.github.xiaomisum.ryze.protocol.dubbo.DubboConstantsInterface;
import io.github.xiaomisum.ryze.protocol.dubbo.RealDubboRequest;
import io.github.xiaomisum.ryze.protocol.dubbo.config.DubboConfigureItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/13 20:08
 */
@KW({"dubbo_postprocessor", "dubbo_post_processor", "dubbo"})
public class DubboPostprocessor extends AbstractProcessor<DubboPostprocessor, DubboConfigureItem, DefaultSampleResult> implements Postprocessor, DubboConstantsInterface {

    @JSONField(serialize = false)
    private ReferenceConfig<GenericService> request;

    @JSONField(serialize = false)
    private Object response;

    public DubboPostprocessor() {
        super();
    }

    public DubboPostprocessor(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.getId(), StringUtils.isBlank(runtime.getTitle()) ? "Dubbo 后置处理器" : runtime.getTitle());
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        response = Dubbo.execute(request, runtime.getConfig(), result);
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
        request = Dubbo.handleRequest(runtime.getConfig());
        result.setRequest(RealDubboRequest.build(runtime.getConfig(), request.getRegistry().getAddress()));
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(RealDubboRequest.build(runtime.getConfig(), request.getRegistry().getAddress()));
        result.setResponse(SampleResult.DefaultReal.build(JSON.toJSONBytes(response)));
    }

    public static class Builder extends AbstractProcessor.PostprocessorBuilder<DubboPostprocessor, Builder, DubboConfigureItem,
            DubboConfigureItem.Builder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public DubboPostprocessor build() {
            return new DubboPostprocessor(this);
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected DubboConfigureItem.Builder getConfigureItemBuilder() {
            return DubboConfigureItem.builder();
        }
    }
}
