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

package io.github.xiaomisum.ryze.core.testelement.sampler;

import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.assertion.Assertion;
import io.github.xiaomisum.ryze.core.builder.*;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.config.RyzeVariables;
import io.github.xiaomisum.ryze.core.context.Context;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.context.TestRunContext;
import io.github.xiaomisum.ryze.core.extractor.Extractor;
import io.github.xiaomisum.ryze.core.interceptor.RyzeInterceptor;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElementExecutable;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.support.Collections;
import io.github.xiaomisum.ryze.support.Customizer;
import io.github.xiaomisum.ryze.support.KryoUtil;
import io.github.xiaomisum.ryze.support.groovy.Groovy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Sampler 抽象实现类。
 *
 * <p>扩展 Sampler 请继承此类。
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractSampler<SELF extends AbstractSampler<SELF, CONFIG, R>, CONFIG extends ConfigureItem<CONFIG>, R extends SampleResult>
        extends AbstractTestElementExecutable<SELF, CONFIG, R> implements Sampler<R> {

    @JSONField(name = VALIDATORS, ordinal = 9)
    protected List<Assertion> assertions;

    @JSONField(name = EXTRACTORS, ordinal = 10)
    protected List<Extractor> extractors;


    public AbstractSampler(Builder builder) {
        super(builder);
        this.assertions = builder.assertions;
        this.extractors = builder.extractors;
    }

    public AbstractSampler() {
        super();
    }

    /**
     * 覆盖父类该方法，取样器无需创建新的 context, 返回最后一个  context
     *
     * @param parentContext 测试上下文链路
     * @return 拷贝的新上下文链路
     */
    protected List<Context> getContextChain(List<Context> parentContext) {
        List<Context> contextChain = new ArrayList<>(parentContext);
        Context context = parentContext.getLast();
        if (Objects.isNull(variables)) {
            variables = new RyzeVariables();
        }
        runtime.configGroup.put(VARIABLES, variables.merge(context.getConfigGroup().getVariables()));
        if (context instanceof TestRunContext testRunContext) {
            testRunContext.setConfigGroup(runtime.configGroup);
        }
        return contextChain;
    }

    // ---------------------------------------------------------------------
    // 重写 AbstractTestElementExecutable 中的方法
    // ---------------------------------------------------------------------

    @Override
    protected final void execute(ContextWrapper context, R result) {
        if (!result.getStatus().isPassed()) {
            // 前置处理器可能执行失败，无需执行测试步骤
            return;
        }
        runtime.config = (CONFIG) context.evaluate(runtime.config);
        try {
            // 执行前置处理
            if (chain.applyPreHandle(context, runtime)) {
                // 业务处理
                handleRequest(context, result);
                result.sampleStart();
                sample(context, result);
                result.sampleEnd();
                handleResponse(context, result);
                // 执行后置处理
                chain.applyPostHandle(context, runtime);
                Optional.ofNullable(assertions).orElse(Collections.emptyList()).forEach(assertion -> assertion.assertThat(context));
                Optional.ofNullable(extractors).orElse(Collections.emptyList()).forEach(extractor -> extractor.process(context));
            }
        } catch (Throwable throwable) {
            // 1、sampler 执行异常 2、assertion 断言异常 3、extractor 提取异常
            result.setThrowable(throwable);
        } finally {
            // 最终处理
            chain.triggerAfterCompletion(context);
        }
    }


    @Override
    public SELF copy() {
        SELF self = super.copy();
        self.assertions = KryoUtil.copy(assertions);
        self.extractors = KryoUtil.copy(extractors);
        return self;
    }

    /**
     * 请求执行前处理。比如请求数据的表达式计算。
     *
     * <p>该方法在 {@link RyzeInterceptor#preHandle(ContextWrapper, TestElement)} 之前调用。
     */
    protected void handleRequest(ContextWrapper context, R result) {
        // do nothing.
    }

    /**
     * 执行请求。
     * <p>不要在该方法内进行请求的动态数据替换，请使用 {@link AbstractSampler#handleRequest(ContextWrapper, SampleResult)}。
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


    public List<Extractor> getExtractors() {
        return extractors;
    }

    public void setExtractors(List<Extractor> extractors) {
        this.extractors = extractors;
    }

    public List<Assertion> getAssertions() {
        return assertions;
    }

    public void setAssertions(List<Assertion> assertions) {
        this.assertions = assertions;
    }

    /**
     * 取样器基础构建器
     *
     * @param <ELE>               取样器类型
     * @param <SELF>              自己的类型
     * @param <CONFIG>            取样器配置类型
     * @param <CONFIGURE_BUILDER> 取样器配置类型构建器
     * @param <R>                 处理结果类型
     */
    public static abstract class Builder<ELE extends AbstractSampler<ELE, CONFIG, R>,
            SELF extends AbstractSampler.Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, CONFIGURES_BUILDER, PREPROCESSORS_BUILDER, POSTPROCESSORS_BUILDER, ASSERTIONS_BUILDER, EXTRACTORS_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            CONFIGURES_BUILDER extends ExtensibleConfigureElementsBuilder,
            PREPROCESSORS_BUILDER extends ExtensiblePreprocessorsBuilder,
            POSTPROCESSORS_BUILDER extends ExtensiblePostprocessorsBuilder,
            ASSERTIONS_BUILDER extends ExtensibleAssertionsBuilder<ASSERTIONS_BUILDER>,
            EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder<EXTRACTORS_BUILDER>,
            R extends SampleResult>
            extends AbstractTestElementExecutable.Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, CONFIGURES_BUILDER, PREPROCESSORS_BUILDER, POSTPROCESSORS_BUILDER, R> {

        protected List<Assertion> assertions;

        protected List<Extractor> extractors;

        public SELF assertions(List<Assertion> assertions) {
            this.assertions = Collections.addAllIfNonNull(this.assertions, assertions);
            return self;
        }

        public SELF assertions(Customizer<ASSERTIONS_BUILDER> customizer) {
            ASSERTIONS_BUILDER builder = getAssertionsBuilder();
            customizer.customize(builder);
            this.assertions = Collections.addAllIfNonNull(this.assertions, builder.build());
            return self;
        }

        public SELF assertions(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "ASSERTIONS_BUILDER") Closure<?> closure) {
            ASSERTIONS_BUILDER builder = getAssertionsBuilder();
            Groovy.call(closure, builder);
            this.assertions = Collections.addAllIfNonNull(this.assertions, builder.build());
            return self;
        }

        public SELF validators(List<Assertion> assertions) {
            return assertions(assertions);
        }

        public SELF validators(Customizer<ASSERTIONS_BUILDER> customizer) {
            return assertions(customizer);
        }

        public SELF validators(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "ASSERTIONS_BUILDER") Closure<?> closure) {
            return assertions(closure);
        }

        public SELF extractors(List<Extractor> extractors) {
            this.extractors = Collections.addAllIfNonNull(this.extractors, extractors);
            return self;
        }

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


        protected abstract ASSERTIONS_BUILDER getAssertionsBuilder();

        protected abstract EXTRACTORS_BUILDER getExtractorsBuilder();
    }

}
