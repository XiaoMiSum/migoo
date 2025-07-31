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
import core.xyz.migoo.builder.ExtensibleChildrenBuilder;
import core.xyz.migoo.builder.ExtensibleConfigureElementsBuilder;
import core.xyz.migoo.builder.ExtensiblePostprocessorsBuilder;
import core.xyz.migoo.builder.ExtensiblePreprocessorsBuilder;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.ExecuteChildrenFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.configure.ConfigureElement;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import support.xyz.migoo.Closeable;
import support.xyz.migoo.Collections;
import support.xyz.migoo.Customizer;
import support.xyz.migoo.ValidateResult;
import support.xyz.migoo.groovy.Groovy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 测试容器抽象类，负责调度子元件执行
 *
 * @author xiaomi
 */
@SuppressWarnings("all")
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
            SELF extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, CONFIGURES_BUILDER, PREPROCESSORS_BUILDER, POSTPROCESSORS_BUILDER, CHILDREN_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            CONFIGURES_BUILDER extends ExtensibleConfigureElementsBuilder,
            PREPROCESSORS_BUILDER extends ExtensiblePreprocessorsBuilder,
            POSTPROCESSORS_BUILDER extends ExtensiblePostprocessorsBuilder,
            CHILDREN_BUILDER extends ExtensibleChildrenBuilder,
            R extends Result>
            extends AbstractTestElementExecutable.Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, PREPROCESSORS_BUILDER, POSTPROCESSORS_BUILDER, R> {

        protected List<ConfigureElement> configureElements;

        protected List<TestElement<?>> children;

        /**
         * 配置元件
         *
         * @param configureElements 配置元件列表
         * @return 当前对象
         */
        public SELF configureElements(List<ConfigureElement> configureElements) {
            this.configureElements = Collections.addAllIfNonNull(this.configureElements, configureElements);
            return self;
        }

        public SELF configureElements(Customizer<CONFIGURES_BUILDER> customizer) {
            CONFIGURES_BUILDER builder = getConfiguresBuilder();
            customizer.customize(builder);
            this.configureElements = Collections.addAllIfNonNull(this.configureElements, builder.build());
            return self;
        }

        public SELF configureElements(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "CONFIGURES_BUILDER") Closure<?> closure) {
            CONFIGURES_BUILDER builder = getConfiguresBuilder();
            Groovy.call(closure, builder);
            this.configureElements = Collections.addAllIfNonNull(this.configureElements, builder.build());
            return self;
        }

        /**
         * 子节点
         *
         * @param children 子节点
         * @return 当前对象
         */
        public SELF children(List<TestElement<?>> children) {
            this.children = Collections.addAllIfNonNull(this.children, children);
            return self;
        }

        public SELF children(Customizer<CHILDREN_BUILDER> customizer) {
            CHILDREN_BUILDER builder = getChildrenBuilder();
            customizer.customize(builder);
            this.configureElements = Collections.addAllIfNonNull(this.configureElements, builder.build());
            return self;
        }

        public SELF children(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "CHILDREN_BUILDER") Closure<?> closure) {
            CHILDREN_BUILDER builder = getChildrenBuilder();
            Groovy.call(closure, builder);
            this.children = Collections.addAllIfNonNull(this.configureElements, builder.build());
            return self;
        }

        protected abstract CONFIGURES_BUILDER getConfiguresBuilder();

        protected abstract CHILDREN_BUILDER getChildrenBuilder();

    }
}
