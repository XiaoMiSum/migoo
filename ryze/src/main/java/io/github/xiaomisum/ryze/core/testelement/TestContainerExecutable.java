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

package io.github.xiaomisum.ryze.core.testelement;

import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.Result;
import io.github.xiaomisum.ryze.core.SessionRunner;
import io.github.xiaomisum.ryze.core.TestStatus;
import io.github.xiaomisum.ryze.core.builder.ExtensibleChildrenBuilder;
import io.github.xiaomisum.ryze.core.builder.ExtensibleConfigureElementsBuilder;
import io.github.xiaomisum.ryze.core.builder.ExtensiblePostprocessorsBuilder;
import io.github.xiaomisum.ryze.core.builder.ExtensiblePreprocessorsBuilder;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;
import io.github.xiaomisum.ryze.support.Collections;
import io.github.xiaomisum.ryze.support.Customizer;
import io.github.xiaomisum.ryze.support.ValidateResult;
import io.github.xiaomisum.ryze.support.groovy.Groovy;

import java.util.ArrayList;
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
        extends AbstractTestElementExecutable<SELF, CONFIG, R> {

    @JSONField(name = CHILDREN, ordinal = 10)
    protected List<TestElement<R>> children;

    public TestContainerExecutable() {
    }

    public TestContainerExecutable(Builder builder) {
        super(builder);
        this.children = builder.children;
    }

    protected void executeChildren(ContextWrapper context) {
        if (children == null) {
            return;
        }

        try {
            // 执行前置处理
            if (!chain.applyPreHandle(context, runtime)) {
                return;
            }
            // 业务处理
            for (TestElement<R> child : children) {
                if (Objects.isNull(child)) {
                    continue;
                }
                R result = context.getSessionRunner().runTest(child);
                if (context.getTestResult() instanceof TestSuiteResult suiteResult) {
                    suiteResult.addChild(result);
                }
                if (child instanceof Sampler<R> && !result.getStatus().isPassed()) {
                    // 如果子元件是取样器，并且执行失败，则后续步骤无需执行
                    break;
                }
            }
            var hasFailedChildren = !((TestSuiteResult) context.getTestResult()).getChildren().stream()
                    .filter(result -> result.getStatus().isBroken() || result.getStatus().isFailed())
                    .toList().isEmpty();
            if (hasFailedChildren) {
                context.getTestResult().setStatus(TestStatus.failed);
            }
            chain.applyPostHandle(context, runtime);
        } catch (Throwable throwable) {
            context.getTestResult().setStatus(throwable instanceof AssertionError ? TestStatus.failed : TestStatus.broken);
            if (SessionRunner.getSession().isRunInTestFrameworkSupport()) {
                throw throwable;
            }
        } finally {
            // 最终处理
            chain.triggerAfterCompletion(context);
        }

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
            extends AbstractTestElementExecutable.Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, CONFIGURES_BUILDER, PREPROCESSORS_BUILDER, POSTPROCESSORS_BUILDER, R> {

        protected List<TestElement<?>> children;

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


        public <T extends ExtensibleChildrenBuilder> SELF children(Class<T> type, Customizer<T> customizer) {
            try {
                T builder = type.getConstructor().newInstance();
                customizer.customize(builder);
                this.children = Collections.addAllIfNonNull(this.children, builder.build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return self;
        }

        public SELF children(Customizer<CHILDREN_BUILDER> customizer) {
            CHILDREN_BUILDER builder = getChildrenBuilder();
            customizer.customize(builder);
            this.children = Collections.addAllIfNonNull(this.children, builder.build());
            return self;
        }

        public <T extends ExtensibleChildrenBuilder> SELF children(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            this.children = Collections.addAllIfNonNull(this.children, builder.build());
            return self;
        }

        public SELF children(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "CHILDREN_BUILDER") Closure<?> closure) {
            CHILDREN_BUILDER builder = getChildrenBuilder();
            Groovy.call(closure, builder);
            this.children = Collections.addAllIfNonNull(this.children, builder.build());
            return self;
        }

        protected abstract CONFIGURES_BUILDER getConfiguresBuilder();

        protected abstract CHILDREN_BUILDER getChildrenBuilder();

    }
}
