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

package core.xyz.migoo.testelement.processor;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.TestStatus;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.extractor.AbstractExtractor;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.filter.SampleFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.TestElementConstantsInterface;
import core.xyz.migoo.testelement.sampler.SampleResult;

import java.util.*;

/**
 * @param <R>
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractProcessor<SELF extends AbstractProcessor<SELF, CONFIG, R>, CONFIG extends ConfigureItem<CONFIG>, R extends SampleResult>
        extends AbstractTestElement<AbstractProcessor<SELF, CONFIG, R>, CONFIG, R>
        implements Processor, SampleFilterChain, TestElementConstantsInterface {

    @JSONField(name = EXTRACTORS, ordinal = 10)
    protected List<Extractor> extractors;

    private Iterator<TestFilter> processFilters;

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
        super.initialized(session);
        var localContext = new ContextWrapper(session);
        localContext.setTestResult(getTestResult());
        runtime.config = (CONFIG) localContext.eval(runtime.config);
        return localContext;
    }

    @Override
    public void doSample(ContextWrapper context) {
        process(context);
    }

    @Override
    public void process(ContextWrapper context) {
        var localContext = initialized ? context : _initialized(context.getSessionRunner());
        if (processFilters.hasNext()) {
            TestFilter next = processFilters.next();
            next.doSample(localContext, this);
            return;
        }
        handleRequest(localContext, (R) localContext.getTestResult());
        sample(localContext, (R) localContext.getTestResult());
        handleResponse(localContext, (R) localContext.getTestResult());
        extract(context, localContext);
        if (context == localContext) {
            return;
        }
        if (this instanceof Preprocessor) {
            context.getTestResult().addPreprocessor(localContext.getTestResult());
        } else if (this instanceof Postprocessor) {
            context.getTestResult().addPostprocessor(localContext.getTestResult());
        }
        if (localContext.getTestResult().getStatus() != TestStatus.passed) {
            context.getTestResult().setStatus(localContext.getTestResult().getStatus());
        }
    }

    protected void handleFilters(ContextWrapper contextWrapper) {
        filters = contextWrapper.getConfigGroup().get(FILTERS);
        processFilters = Objects.isNull(filters) ? Collections.emptyIterator() : filters.iterator();
    }

    private void extract(ContextWrapper context, ContextWrapper localContext) {
        if (Objects.isNull(extractors)) {
            return;
        }
        for (Extractor extractor : extractors) {
            extractor.process(localContext);
        }
        context.getLocalVariablesWrapper().getLastVariables()
                .merge(localContext.getLocalVariablesWrapper().getLastVariables());
    }

    /**
     * 请求执行前处理。比如请求数据的表达式计算。
     *
     * <p>该方法在 {@link TestFilter#doSample} 之前调用。
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
     * <p>该方法在 {@link TestFilter#doSample} 之后调用。
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
            SELF extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTOR_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            EXTRACTOR_BUILDER extends AbstractExtractor.Builder,
            R extends SampleResult>
            extends AbstractTestElement.Builder<AbstractProcessor<ELE, CONFIG, R>, SELF, CONFIG, CONFIGURE_BUILDER, R> {
        private List<Extractor> extractors;

        protected SELF extractors(List<Extractor> extractors) {
            this.extractors = extractors;
            return self;
        }

        protected SELF addExtractor(Extractor extractor) {
            synchronized (this) {
                if (this.extractors == null) {
                    synchronized (this) {
                        this.extractors = new ArrayList<>();
                    }
                }
            }
            this.extractors.add(extractor);
            return self;
        }
    }

    public static abstract class PreprocessorBuilder<ELE extends AbstractProcessor<ELE, CONFIG, R>,
            SELF extends PreprocessorBuilder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTOR_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            EXTRACTOR_BUILDER extends AbstractExtractor.Builder,
            R extends SampleResult>
            extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTOR_BUILDER, R> {
    }

    public static abstract class PostprocessorBuilder<ELE extends AbstractProcessor<ELE, CONFIG, R>,
            SELF extends PostprocessorBuilder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTOR_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            EXTRACTOR_BUILDER extends AbstractExtractor.Builder,
            R extends SampleResult>
            extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, EXTRACTOR_BUILDER, R> {
    }


}
