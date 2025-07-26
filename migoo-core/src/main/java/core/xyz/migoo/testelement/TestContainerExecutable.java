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

import core.xyz.migoo.TestStatus;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.ExecuteChildrenFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import support.xyz.migoo.ValidateResult;

import java.util.*;

/**
 * 测试容器抽象类，负责调度子元件执行
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes"})
public abstract class TestContainerExecutable
        <CONFIG extends ConfigureItem<CONFIG>, SELF extends TestContainerExecutable<CONFIG, SELF, T>, T extends Result>
        extends AbstractTestElementExecutable<CONFIG, SELF, T> implements ExecuteChildrenFilterChain {

    protected List<TestElement<T>> children;

    protected Iterator<TestFilter> executeChildrenFilters;

    public TestContainerExecutable() {
    }

    @Override
    public void doExecuteChildren(ContextWrapper context) {
        if (children == null) {
            return;
        }
        if (executeChildrenFilters.hasNext()) {
            TestFilter next = executeChildrenFilters.next();
            next.doExecuteChildren(context, this);
            return;
        }
        for (TestElement<T> child : children) {
            if (child == null) {
                continue;
            }
            T result = context.getSessionRunner().runTest(child);
            if (TestStatus.failed == result.getStatus()) {
                context.getTestResult().setStatus(TestStatus.failed);
            }
            if (context.getTestResult() instanceof TestSuiteResult suiteResult) {
                suiteResult.addChild(result);
            }

        }
    }

    protected void executeChildren(ContextWrapper contextWrapper) {
        executeChildrenFilters = Objects.isNull(filters) ? Collections.emptyIterator() : filters.iterator();
        doExecuteChildren(contextWrapper);
    }

    @Override
    public ValidateResult validate() {
        ValidateResult result = super.validate();
        if (children == null) {
            result.append("\n容器类测试元件 %s 字段值缺失或为空，当前值：%s", CHILDREN, toString());
            return result;
        }
        for (TestElement<?> child : children) {
            result.append(child);
        }
        return result;
    }

    @Override
    public SELF copy() {
        SELF self = super.copy();
        if (children == null) {
            return self;
        }
        self.children = new ArrayList<>();
        for (TestElement<T> step : children) {
            self.children.add(step.copy());
        }
        return self;
    }


    public List<TestElement<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TestElement<T>> children) {
        this.children = children;
    }

}
