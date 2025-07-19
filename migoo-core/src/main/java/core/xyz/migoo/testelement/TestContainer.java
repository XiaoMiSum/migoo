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

package core.xyz.migoo.testelement;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.ExecuteSubStepsFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 测试容器抽象类，负责调度子元件执行
 *
 * @author xiaomi
 */
public abstract class TestContainer<SELF extends TestContainer<SELF, T>, T extends Result<T>>
        extends AbstractTestElement<SELF, T> implements ExecuteSubStepsFilterChain {

    protected List<TestElement<?>> children;

    protected Iterator<TestFilter> executeSubStepsFilters;

    public TestContainer() {
    }

    @Override
    public void doExecuteSubSteps(ContextWrapper ctx) {
        if (executeSubStepsFilters.hasNext()) {
            TestFilter next = executeSubStepsFilters.next();
            next.doExecuteSubSteps(ctx, this);
        } else {
            if (children != null) {
                for (TestElement<?> step : children) {
                    if (step != null) {
                        ctx.getSessionRunner().runTest(step);
                    }
                }
            }
        }
    }

    protected void executeSubSteps(ContextWrapper contextWrapper) {
        executeSubStepsFilters = filters.iterator();
        doExecuteSubSteps(contextWrapper);
    }

    @Override
    public ValidateResult validate() {
        ValidateResult result = super.validate();
        if (children != null) {
            for (TestElement<?> step : children) {
                result.append(step);
            }
        }
        return result;
    }

    @Override
    public SELF copy() {
        SELF self = super.copy();
        if (children != null) {
            List<TestElement<?>> newChildren = new ArrayList<>();
            for (TestElement<?> step : children) {
                newChildren.add(step.copy());
            }
            self.children = newChildren;
        }
        return self;
    }


    public List<TestElement<?>> getChildren() {
        return children;
    }

    public void setChildren(List<TestElement<?>> children) {
        this.children = children;
    }

}
