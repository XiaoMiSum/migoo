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

package io.github.xiaomisum.ryze.core.testelement.processor;

import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.SessionRunner;
import io.github.xiaomisum.ryze.core.builder.ExtensibleExtractorsBuilder;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.extractor.Extractor;
import io.github.xiaomisum.ryze.core.interceptor.RyzeInterceptor;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.support.Collections;
import io.github.xiaomisum.ryze.support.Customizer;
import io.github.xiaomisum.ryze.support.groovy.Groovy;

import java.util.List;
import java.util.Optional;

/**
 * @param <R>
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractProcessor<SELF extends AbstractProcessor<SELF, CONFIG, R>, CONFIG extends ConfigureItem<CONFIG>, R extends SampleResult>
        extends AbstractTestElement<AbstractProcessor<SELF, CONFIG, R>, CONFIG, R>
        implements Processor, TestElementConstantsInterface {

    @JSONField(name = EXTRACTORS, ordinal = 10)
    protected List<Extractor> extractors;

    public AbstractProcessor() {
        super();
    }

    public AbstractProcessor(Builder<SELF, ?, CONFIG, ?, ?, R> builder) {
        super(builder);
        this.extractors = builder.extractors;
    }

    public void setExtractors(List<Extractor> extractors) {
        this.extractors = extractors;
    }

    protected ContextWrapper _initialized(SessionRunner session) {
        super.initialized();
        var localContext = new ContextWrapper(session);
        localContext.setTestResult(getTestResult());
        localContext.setTestElement(this);
        runtime.id = (String) localContext.evaluate(id);
        runtime.title = (String) localContext.evaluate(title);
        runtime.config = (CONFIG) localContext.evaluate(runtime.config);
        handleFilterInterceptors(localContext);
        return localContext;
    }

    @Override
    public void process(ContextWrapper context) {
        var localContext = initialized ? context : _initialized(context.getSessionRunner());
        var result = (R) localContext.getTestResult();
        runtime.config = (CONFIG) context.evaluate(runtime.config);
        try {
            // 执行前置处理
            if (chain.applyPreHandle(localContext, runtime)) {
                // 业务处理
                handleRequest(localContext, result);
                result.sampleStart();
                sample(localContext, result);
                result.sampleEnd();
                handleResponse(localContext, result);
                // 执行后置处理
                chain.applyPostHandle(localContext, runtime);
                Optional.ofNullable(extractors).orElse(Collections.emptyList()).forEach(extractor -> extractor.process(localContext));
                //  context 与 localContext 中的 lastContext 是同一对象，无需再进行变量合并
                //  context.getLocalVariablesWrapper().getLastVariables().merge(localContext.getLocalVariablesWrapper().getLastVariables());
            }
        } catch (Throwable throwable) {
            // 1、processor 执行异常 2、extractor 提取异常  设置父级执行异常
            localContext.getTestResult().setThrowable(throwable);
            context.getTestResult().setThrowable(throwable);
        } finally {
            // 最终处理
            chain.triggerAfterCompletion(localContext);
            var status = localContext.getTestResult().getStatus();
            if (context != localContext && (status.isBroken() || status.isFailed())) {
                context.getTestResult().setStatus(localContext.getTestResult().getStatus());
            }
        }
    }

    /**
     * 请求执行前处理。比如请求数据的表达式计算。
     *
     * <p>该方法在 {@link RyzeInterceptor#preHandle(ContextWrapper, TestElement)} )} 之前调用。
     */
    protected void handleRequest(ContextWrapper context, R result) {
        // do nothing.
    }

    /**
     * 执行请求。
     * <p>不要在该方法内进行请求的动态数据替换，请使用 {@link AbstractProcessor#handleRequest(ContextWrapper, SampleResult)}。
     */
    protected abstract void sample(ContextWrapper context, R result);

    /**
     * 请求执行后处理。
     *
     * <p>该方法在 {@link RyzeInterceptor#postHandle(ContextWrapper, TestElement)} 之后调用。
     */
    protected void handleResponse(ContextWrapper context, R result) {
        // do nothing.
    }

    /**
     * 处理器 构建器抽象类
     *
     * @param <ELE>               处理器元件类型
     * @param <SELF>              构建器自己
     * @param <CONFIG>            配置项类型
     * @param <CONFIGURE_BUILDER> 配置项构建器
     * @param <R>                 执行结果类型
     */
    public static abstract class Builder<ELE extends AbstractProcessor<ELE, CONFIG, R>,
            SELF extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTORS_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder<EXTRACTORS_BUILDER>,
            R extends SampleResult>
            extends AbstractTestElement.Builder<AbstractProcessor<ELE, CONFIG, R>, SELF, CONFIG, CONFIGURE_BUILDER, R> {

        protected List<Extractor> extractors;

        public SELF extractors(Customizer<EXTRACTORS_BUILDER> customizer) {
            EXTRACTORS_BUILDER builder = getExtractorsBuilder();
            customizer.customize(builder);
            this.extractors = Collections.addAllIfNonNull(this.extractors, builder.build());
            return self;
        }

        public SELF extractors(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "EXTRACTORS_BUILDER") Closure<?> closure) {
            EXTRACTORS_BUILDER builder = getExtractorsBuilder();
            Groovy.call(closure, builder);
            this.extractors = Collections.addAllIfNonNull(this.extractors, builder.build());
            return self;
        }

        protected abstract EXTRACTORS_BUILDER getExtractorsBuilder();
    }

    public static abstract class PreprocessorBuilder<ELE extends AbstractProcessor<ELE, CONFIG, R>,
            SELF extends PreprocessorBuilder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTORS_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder<EXTRACTORS_BUILDER>,
            R extends SampleResult>
            extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTORS_BUILDER, R> {
    }

    public static abstract class PostprocessorBuilder<ELE extends AbstractProcessor<ELE, CONFIG, R>,
            SELF extends PostprocessorBuilder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTORS_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder<EXTRACTORS_BUILDER>,
            R extends SampleResult>
            extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTORS_BUILDER, R> {
    }


}
