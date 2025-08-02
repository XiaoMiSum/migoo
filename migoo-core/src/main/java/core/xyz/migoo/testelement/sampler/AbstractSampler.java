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

package core.xyz.migoo.testelement.sampler;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.TestStatus;
import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.builder.*;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.config.MiGooVariables;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.context.TestRunContext;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.listener.MiGooListener;
import core.xyz.migoo.listener.SampleFilterChain;
import core.xyz.migoo.testelement.AbstractTestElementExecutable;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import support.xyz.migoo.Collections;
import support.xyz.migoo.Customizer;
import support.xyz.migoo.KryoUtil;
import support.xyz.migoo.groovy.Groovy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Sampler 抽象实现类。
 *
 * <p>扩展 Sampler 请继承此类。
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractSampler<SELF extends AbstractSampler<SELF, CONFIG, R>, CONFIG extends ConfigureItem<CONFIG>, R extends SampleResult>
        extends AbstractTestElementExecutable<SELF, CONFIG, R> implements Sampler<R>, SampleFilterChain {

    @JSONField(name = VALIDATORS, ordinal = 9)
    protected List<Assertion> assertions;

    @JSONField(name = EXTRACTORS, ordinal = 10)
    protected List<Extractor> extractors;


    public AbstractSampler(Builder builder) {
        super(builder);
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
            variables = new MiGooVariables();
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
    protected final void execute(ContextWrapper ctx, R result) {
        doSample(ctx);
    }

    // ---------------------------------------------------------------------
    // 实现 SampleFilterChain 中的方法
    // ---------------------------------------------------------------------

    @Override
    public final void doSample(ContextWrapper context) {
        runtime.config = (CONFIG) context.eval(runtime.config);
        if (runtimeListeners.hasNext()) {
            MiGooListener next = runtimeListeners.next();
            next.doSample(context, this);
            return;
        }
        R result = (R) context.getTestResult();
        handleRequest(context, result);
        // 子类可以在 sample 方法或其子方法内，在合适的时机再次调用 sampleStart 和 sampleEnd 方法，
        // 以获取更准确的 sample 时间
        result.sampleStart();
        sample(context, (R) context.getTestResult());
        result.sampleEnd();
        handleResponse(context, result);
        if (Objects.nonNull(assertions) && context.getTestResult().getStatus() == TestStatus.passed) {
            for (Assertion assertion : assertions) {
                assertion.assertThat(context);
            }
        }
        if (Objects.nonNull(extractors) && context.getTestResult().getStatus() == TestStatus.passed) {
            for (Extractor extractor : extractors) {
                extractor.process(context);
            }
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
     * <p>该方法在 {@link MiGooListener#doSample} 之前调用。
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
     * <p>该方法在 {@link MiGooListener#doSample} 之后调用。
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
