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

package core.xyz.migoo.processor;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.filter.SampleFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.TestElementConstantsInterface;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @param <T>
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes"})
public abstract class AbstractProcessor<CONFIG extends ConfigureItem, T extends SampleResult<T>>
        extends AbstractTestElement<CONFIG, AbstractProcessor<CONFIG, T>, T>
        implements Processor, SampleFilterChain, TestElementConstantsInterface {

    @JSONField(name = EXTRACTORS, ordinal = 2)
    protected List<Extractor> extractors;

    private Iterator<TestFilter> processFilters;

    public AbstractProcessor() {
        super();
    }

    @Override
    public void doSample(ContextWrapper context) {
        _process(context);
    }

    @Override
    public void process(ContextWrapper context) {
        if (!initialized) {
            initialized(context.getSessionRunner());
        }
        ContextWrapper localContext = new ContextWrapper(context.getSessionRunner());
        localContext.setTestResult(getTestResult());
        localContext.eval(config);
        if (processFilters.hasNext()) {
            TestFilter next = processFilters.next();
            next.doSample(localContext, this);
        } else {
            doSample(localContext);
        }
        extract(context, localContext);
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

    protected abstract void _process(ContextWrapper context);

}
