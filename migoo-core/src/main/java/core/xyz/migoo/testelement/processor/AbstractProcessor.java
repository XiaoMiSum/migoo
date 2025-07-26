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
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.filter.SampleFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.TestElementConstantsInterface;
import core.xyz.migoo.testelement.sampler.SampleResult;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractProcessor<CONFIG extends ConfigureItem, SELF, T extends SampleResult>
        extends AbstractTestElement<CONFIG, AbstractProcessor<CONFIG, SELF, T>, T>
        implements Processor, SampleFilterChain, TestElementConstantsInterface {

    @JSONField(name = EXTRACTORS, ordinal = 10)
    protected List<Extractor> extractors;

    private Iterator<TestFilter> processFilters;

    public AbstractProcessor() {
        super();
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
        } else {
            handleRequest(localContext, (T) localContext.getTestResult());
            sample(localContext, (T) localContext.getTestResult());
            handleResponse(localContext, (T) localContext.getTestResult());
            extract(context, localContext);
        }
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
    protected void handleRequest(ContextWrapper context, T result) {
        // do nothing.
    }

    /**
     * 执行请求。
     * <p>不要在该方法内进行请求的动态数据替换，请使用 {@link AbstractProcessor#handleRequest(ContextWrapper, SampleResult)}。
     */
    protected abstract void sample(ContextWrapper context, T result);

    /**
     * 请求执行后处理。
     *
     * <p>该方法在 {@link TestFilter#doSample} 之后调用。
     */
    protected void handleResponse(ContextWrapper context, T result) {
        // do nothing.
    }

}
