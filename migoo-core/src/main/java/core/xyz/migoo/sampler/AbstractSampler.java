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

package core.xyz.migoo.sampler;

import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.SampleFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.testelement.AbstractTestElementExecutable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * Sampler 抽象实现类。
 *
 * <p>扩展 Sampler 请继承此类。
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractSampler<CONFIG extends ConfigureItem, SELF extends AbstractSampler<CONFIG, SELF, T>, T extends SampleResult<T>>
        extends AbstractTestElementExecutable<CONFIG, SELF, T> implements Sampler<T>, SampleFilterChain {

    private Iterator<TestFilter> sampleFilters;

    public AbstractSampler() {
    }

    protected void handleFilters(ContextWrapper contextWrapper) {
        super.handleFilters(contextWrapper);
        sampleFilters = Objects.isNull(filters) ? Collections.emptyIterator() : filters.iterator();
    }

    // ---------------------------------------------------------------------
    // 重写 AbstractTestElementExecutable 中的方法
    // ---------------------------------------------------------------------

    @Override
    protected final void execute(ContextWrapper ctx, T result) {
        handleRequest(ctx, result);
        // 子类可以在 sample 方法或其子方法内，在合适的时机再次调用 sampleStart 和 sampleEnd 方法，
        // 以获取更准确的 sample 时间
        result.sampleStart();
        doSample(ctx);
        result.sampleEnd();
        handleResponse(ctx, result);
    }

    // ---------------------------------------------------------------------
    // 实现 SampleFilterChain 中的方法
    // ---------------------------------------------------------------------

    @Override
    public final void doSample(ContextWrapper ctx) {
        if (sampleFilters.hasNext()) {
            TestFilter next = sampleFilters.next();
            next.doSample(ctx, this);
        } else {
            sample(ctx, (T) ctx.getTestResult());
        }
    }

    /**
     * 请求执行前处理。比如请求数据的表达式计算。
     *
     * <p>该方法在 {@link TestFilter#doSample} 之前调用。
     */
    protected void handleRequest(ContextWrapper contextWrapper, T result) {
        // do nothing.
    }

    /**
     * 执行请求。
     * <p>不要在该方法内进行请求的动态数据替换，请使用 {@link AbstractSampler#handleRequest(ContextWrapper, SampleResult)}。
     */
    protected abstract void sample(ContextWrapper contextWrapper, T result);

    /**
     * 请求执行后处理。
     *
     * <p>该方法在 {@link TestFilter#doSample} 之后调用。
     */
    protected void handleResponse(ContextWrapper contextWrapper, T result) {
        // do nothing.
    }
}
