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

import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.config.MiGooVariables;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.context.TestRunContext;
import core.xyz.migoo.filter.SampleFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.testelement.AbstractTestElementExecutable;

import java.util.*;

/**
 * Sampler 抽象实现类。
 *
 * <p>扩展 Sampler 请继承此类。
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractSampler<CONFIG extends ConfigureItem<CONFIG>, SELF extends AbstractSampler<CONFIG, SELF, T>, T extends SampleResult>
        extends AbstractTestElementExecutable<CONFIG, SELF, T> implements Sampler<T>, SampleFilterChain {

    private Iterator<TestFilter> sampleFilters;

    public AbstractSampler() {
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

    protected void handleFilters(ContextWrapper contextWrapper) {
        super.handleFilters(contextWrapper);
        sampleFilters = Objects.isNull(filters) ? Collections.emptyIterator() : filters.iterator();
    }

    // ---------------------------------------------------------------------
    // 重写 AbstractTestElementExecutable 中的方法
    // ---------------------------------------------------------------------

    @Override
    protected final void execute(ContextWrapper ctx, T result) {
        doSample(ctx);
    }

    // ---------------------------------------------------------------------
    // 实现 SampleFilterChain 中的方法
    // ---------------------------------------------------------------------

    @Override
    public final void doSample(ContextWrapper ctx) {
        runtime.config = (CONFIG) ctx.eval(runtime.config);
        if (sampleFilters.hasNext()) {
            TestFilter next = sampleFilters.next();
            next.doSample(ctx, this);
        } else {
            T result = (T) ctx.getTestResult();
            handleRequest(ctx, result);
            // 子类可以在 sample 方法或其子方法内，在合适的时机再次调用 sampleStart 和 sampleEnd 方法，
            // 以获取更准确的 sample 时间
            result.sampleStart();
            sample(ctx, (T) ctx.getTestResult());
            result.sampleEnd();
            handleResponse(ctx, result);
        }
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
     * <p>不要在该方法内进行请求的动态数据替换，请使用 {@link AbstractSampler#handleRequest(ContextWrapper, SampleResult)}。
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
