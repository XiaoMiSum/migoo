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

package io.github.xiaomisum.ryze.protocol.dubbo.sampler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.builder.DefaultAssertionsBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultExtractorsBuilder;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;
import io.github.xiaomisum.ryze.protocol.dubbo.Dubbo;
import io.github.xiaomisum.ryze.protocol.dubbo.DubboConstantsInterface;
import io.github.xiaomisum.ryze.protocol.dubbo.RealDubboRequest;
import io.github.xiaomisum.ryze.protocol.dubbo.builder.DubboConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.dubbo.builder.DubboPostprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.dubbo.builder.DubboPreprocessorsBuilder;
import io.github.xiaomisum.ryze.protocol.dubbo.config.DubboConfigureItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Objects;

/**
 * @author mi.xiao
 * @date 2021/4/10 21:10
 */
@KW({"dubbo", "dubbo_sampler"})
public class DubboSampler extends AbstractSampler<DubboSampler, DubboConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, DubboConstantsInterface {

    @JSONField(serialize = false)
    private ReferenceConfig<GenericService> request;

    @JSONField(serialize = false)
    private Object response;

    public DubboSampler() {
        super();
    }

    public DubboSampler(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.id, runtime.title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        response = Dubbo.execute(request, runtime.config, result);
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
        request = Dubbo.handleRequest(runtime.config);
        result.setRequest(RealDubboRequest.build(runtime.config, request.getRegistry().getAddress()));
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setResponse(SampleResult.DefaultReal.build(JSON.toJSONBytes(response)));
    }

    public static class Builder extends AbstractSampler.Builder<DubboSampler, Builder, DubboConfigureItem,
            DubboConfigureItem.Builder, DubboConfigureElementsBuilder, DubboPreprocessorsBuilder, DubboPostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public DubboSampler build() {
            return new DubboSampler(this);
        }

        @Override
        protected DefaultAssertionsBuilder getAssertionsBuilder() {
            return DefaultAssertionsBuilder.builder();
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected DubboConfigureElementsBuilder getConfiguresBuilder() {
            return DubboConfigureElementsBuilder.builder();
        }

        @Override
        protected DubboPreprocessorsBuilder getPreprocessorsBuilder() {
            return DubboPreprocessorsBuilder.builder();
        }

        @Override
        protected DubboPostprocessorsBuilder getPostprocessorsBuilder() {
            return DubboPostprocessorsBuilder.builder();
        }

        @Override
        protected DubboConfigureItem.Builder getConfigureItemBuilder() {
            return DubboConfigureItem.builder();
        }
    }
}
