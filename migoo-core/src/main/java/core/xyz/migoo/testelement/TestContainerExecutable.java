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

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.TestStatus;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.ExecuteChildrenFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.configure.AbstractConfigureElement;
import core.xyz.migoo.testelement.configure.ConfigureElement;
import support.xyz.migoo.Closeable;
import support.xyz.migoo.ValidateResult;

import java.util.*;
import java.util.function.Supplier;

/**
 * 测试容器抽象类，负责调度子元件执行
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class TestContainerExecutable<SELF extends TestContainerExecutable<SELF, CONFIG, R>,
        CONFIG extends ConfigureItem<CONFIG>, R extends Result>
        extends AbstractTestElementExecutable<SELF, CONFIG, R> implements ExecuteChildrenFilterChain {

    @JSONField(name = CONFIG_ELEMENTS, ordinal = 5)
    protected List<ConfigureElement> configureElements;

    @JSONField(name = CHILDREN, ordinal = 10)
    protected List<TestElement<R>> children;

    protected Iterator<TestFilter> executeChildrenFilters;

    public TestContainerExecutable() {
    }

    public TestContainerExecutable(Builder builder) {
        super(builder);
        this.configureElements = builder.configureElements;
        this.children = builder.children;
    }

    @Override
    public void testEnd(ContextWrapper context) {
        if (Objects.isNull(configureElements)) {
            return;
        }
        for (ConfigureElement configureElement : configureElements) {
            if (configureElement instanceof Closeable closeable) {
                closeable.close();
            }
        }
    }

    @Override
    protected void internalRun(ContextWrapper context) {
        // 模板计算：当前元件的变量配置项（不会计算父级元件）
        evalConfig(context);
        // 处理配置元件
        if (Objects.nonNull(configureElements)) {
            for (ConfigureElement configureElement : configureElements) {
                configureElement.process(context);
            }
        }
        super.internalRun(context);
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
        for (TestElement<R> child : children) {
            if (child == null) {
                continue;
            }
            R result = context.getSessionRunner().runTest(child);
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
        for (TestElement<R> step : children) {
            self.children.add(step.copy());
        }
        return self;
    }

    public List<ConfigureElement> getConfigureElements() {
        return configureElements;
    }

    public void setConfigureElements(List<ConfigureElement> configureElements) {
        this.configureElements = configureElements;
    }

    public List<TestElement<R>> getChildren() {
        return children;
    }

    public void setChildren(List<TestElement<R>> children) {
        this.children = children;
    }

    /**
     * 容器基础构建器
     *
     * @param <ELE>               容器类型
     * @param <SELF>              自己的类型
     * @param <CONFIG>            容器配置类型
     * @param <CONFIGURE_BUILDER> 容器配置类型构建器
     * @param <R>                 处理结果类型
     */
    public static abstract class Builder<ELE extends TestContainerExecutable<ELE, CONFIG, R>,
            SELF extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            R extends Result>
            extends AbstractTestElementExecutable.Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, R> {

        protected List<ConfigureElement> configureElements;

        protected List<TestElement<R>> children;

        public Builder configureElements(List<ConfigureElement> configureElements) {
            this.configureElements = configureElements;
            return self;
        }

        public Builder configureElement(Supplier<AbstractConfigureElement.Builder> supplier) {
            return configureElement((ConfigureElement) supplier.get().build());
        }

        public Builder configureElement(ConfigureElement configureElement) {
            synchronized (this) {
                if (Objects.isNull(configureElements)) {
                    synchronized (this) {
                        this.configureElements = new ArrayList<>();
                    }
                }
            }
            this.configureElements.add(configureElement);
            return self;
        }

        public Builder children(List<TestElement<R>> children) {
            this.children = children;
            return self;
        }

        public Builder children(Supplier<AbstractTestElementExecutable.Builder> supplier) {
            return children((TestElement<R>) supplier.get().build());
        }

        public Builder children(TestElement<R> child) {
            synchronized (this) {
                if (Objects.isNull(children)) {
                    synchronized (this) {
                        this.children = new ArrayList<>();
                    }
                }
            }
            this.children.add(child);
            return self;
        }
    }
}
