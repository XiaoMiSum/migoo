/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.xiaomisum.ryze.core.testelement;

import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.Result;
import io.github.xiaomisum.ryze.core.SessionRunner;
import io.github.xiaomisum.ryze.core.builder.ExtensibleConfigureElementsBuilder;
import io.github.xiaomisum.ryze.core.builder.ExtensiblePostprocessorsBuilder;
import io.github.xiaomisum.ryze.core.builder.ExtensiblePreprocessorsBuilder;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.config.RyzeVariables;
import io.github.xiaomisum.ryze.core.context.Context;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.context.TestSuiteContext;
import io.github.xiaomisum.ryze.core.testelement.configure.ConfigureElement;
import io.github.xiaomisum.ryze.core.testelement.processor.Postprocessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Preprocessor;
import io.github.xiaomisum.ryze.support.Closeable;
import io.github.xiaomisum.ryze.support.Collections;
import io.github.xiaomisum.ryze.support.Customizer;
import io.github.xiaomisum.ryze.support.KryoUtil;
import io.github.xiaomisum.ryze.support.groovy.Groovy;

import java.util.*;
import java.util.function.Consumer;

/**
 * 测试组件抽象类，提供公共属性和方法
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractTestElementExecutable<SELF extends AbstractTestElementExecutable<SELF, CONFIG, R>,
        CONFIG extends ConfigureItem<CONFIG>, R extends Result>
        extends AbstractTestElement<SELF, CONFIG, R> implements TestElementExecutable<R>, TestElementConstantsInterface {

    @JSONField(name = VARIABLES, ordinal = 4)
    protected RyzeVariables variables;

    @JSONField(name = CONFIG_ELEMENTS, ordinal = 5)
    protected List<ConfigureElement> configureElements;

    @JSONField(name = PREPROCESSORS, ordinal = 6)
    protected List<Preprocessor> preprocessors = new ArrayList<>();

    @JSONField(name = POSTPROCESSORS, ordinal = 7)
    protected List<Postprocessor> postprocessors = new ArrayList<>();


    protected TestElementConfigureGroup configGroup = new TestElementConfigureGroup();

    public AbstractTestElementExecutable() {
        super();
    }

    public AbstractTestElementExecutable(Builder builder) {
        super(builder);
        this.variables = builder.variables;
        this.configureElements = builder.configureElements;
        this.preprocessors = builder.preprocessors;
        this.postprocessors = builder.postprocessors;
    }

    /**
     * 当前测试元件的上下文链, 当子类有额外的需求时重写该方法
     *
     * @param parentContext 父上下文链
     */
    protected List<Context> getContextChain(List<Context> parentContext) {
        List<Context> contextChain = new ArrayList<>(parentContext);

        TestSuiteContext context = new TestSuiteContext();
        if (Objects.isNull(variables)) {
            variables = new RyzeVariables();
        }
        runtime.configGroup.put(VARIABLES, variables.merge(parentContext.getLast().getConfigGroup().getVariables()));
        context.setConfigGroup(runtime.getConfigGroup());
        contextChain.add(context);
        return contextChain;
    }


    protected void evalConfig(ContextWrapper context) {
        // 变量与config
        RyzeVariables item;
        if (runtime.variables != null && (item = runtime.configGroup.getVariables()) != null) {
            context.evaluate(item);
        }
        CONFIG config;
        if (runtime.config != null && (config = runtime.configGroup.get(CONFIG)) != null) {
            context.evaluate(config);
        }
    }


    @Override
    public final R run(SessionRunner session) {
        Snapshot snapshot = new Snapshot();
        if (disabled) {
            snapshot.testResult.testEnd();
            return snapshot.testResult;
        }
        if (!initialized) {
            initialized();
        }

        ContextWrapper context = updateCurrentContextInfo(session, snapshot);
        runtime.id = (String) context.evaluate(id);
        runtime.title = (String) context.evaluate(title);
        snapshot.testResult = getTestResult();
        context.setTestResult(snapshot.testResult);
        snapshot.testResult.testStart();
        handleFilterInterceptors(context);
        internalRun(context);
        restoreCurrentContextInfo(session, snapshot);
        snapshot.testResult.testEnd();
        return snapshot.testResult;
    }

    private ContextWrapper updateCurrentContextInfo(SessionRunner session, Snapshot snapshot) {
        // 记录更新前的上下文信息
        List<Context> parentContextChain = session.getContextChain();
        ContextWrapper previousContextWrapper = session.getContext();
        snapshot.parentContextChain = parentContextChain;
        snapshot.previousContextWrapper = previousContextWrapper;

        // 更新当前上下文信息
        List<Context> currentContextChain = getContextChain(parentContextChain);
        session.setContextChain(currentContextChain);
        // 构建上下文包装器，封装本次执行的相关信息
        // 在后续多个方法间传递该对象，使用了方法传参，而不是成员变量，防止该对象在不正确的位置被使用，
        // 另一方面，该对象不是对象状态表示，只是一个临时对象，没有必要使用成员变量
        ContextWrapper context = new ContextWrapper(session);
        context.setTestElement(this);

        session.setContext(context);
        return context;
    }

    private void restoreCurrentContextInfo(SessionRunner session, Snapshot snapshotData) {
        session.setContextChain(snapshotData.parentContextChain);
        session.setContext(snapshotData.previousContextWrapper);
    }

    protected void internalRun(ContextWrapper context) {
        // 模板计算：当前元件的变量配置项（不会计算父级元件）
        evalConfig(context);
        // 处理配置元件
        Optional.ofNullable(configureElements).orElse(Collections.emptyList()).forEach(ele -> ele.process(context));
        // 执行前置动作
        Optional.ofNullable(preprocessors).orElse(Collections.emptyList())
                .stream().filter(preprocessor -> !preprocessor.isDisabled())
                .forEach(preprocessor -> preprocessor.process(context));
        if (context.getTestResult().getStatus().isFailed() || context.getTestResult().getStatus().isBroken()) {
            // 前置步骤执行失败，后续步骤无需执行
            return;
        }
        // 执行请求
        execute(context, (R) context.getTestResult());
        Optional.ofNullable(postprocessors).orElse(Collections.emptyList())
                .stream().filter(postprocessor -> !postprocessor.isDisabled())
                .forEach(postprocessor -> postprocessor.process(context));
        // 关闭配置元件
        Optional.ofNullable(configureElements).orElse(Collections.emptyList()).stream()
                .filter(ele -> ele instanceof Closeable)
                .forEach(ele -> ((Closeable) ele).close());
    }

    @Override
    public SELF copy() {
        SELF self = super.copy();
        self.variables = KryoUtil.copy(variables);
        self.preprocessors = KryoUtil.copy(preprocessors);
        self.postprocessors = KryoUtil.copy(postprocessors);
        return self;
    }


    /**
     * 测试元件的功能实现，比如发起 HTTP 请求
     */
    protected abstract void execute(ContextWrapper context, R result);

    // getter/setter

    public RyzeVariables getVariables() {
        return variables;
    }

    public void setVariables(RyzeVariables variables) {
        this.variables = variables;
    }

    public TestElementConfigureGroup getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(TestElementConfigureGroup configGroup) {
        this.configGroup = configGroup;
    }

    public List<ConfigureElement> getConfigureElements() {
        return configureElements;
    }

    public void setConfigureElements(List<ConfigureElement> configureElements) {
        this.configureElements = configureElements;
    }

    public List<Preprocessor> getPreprocessors() {
        return preprocessors;
    }

    public void setPreprocessors(List<Preprocessor> preprocessors) {
        this.preprocessors = preprocessors;
    }

    public List<Postprocessor> getPostprocessors() {
        return postprocessors;
    }

    public void setPostprocessors(List<Postprocessor> postprocessors) {
        this.postprocessors = postprocessors;
    }

    public static abstract class Builder<ELE extends AbstractTestElementExecutable<ELE, CONFIG, R>,
            SELF extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, CONFIGURES_BUILDER, PREPROCESSORS_BUILDER, POSTPROCESSORS_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            CONFIGURES_BUILDER extends AbstractTestElement.ConfigureElementsBuilder,
            PREPROCESSORS_BUILDER extends AbstractTestElement.PreprocessorsBuilder,
            POSTPROCESSORS_BUILDER extends AbstractTestElement.PostprocessorsBuilder,
            R extends Result>
            extends AbstractTestElement.Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, R> {

        protected RyzeVariables variables;

        protected List<ConfigureElement> configureElements;

        protected List<Preprocessor> preprocessors;

        protected List<Postprocessor> postprocessors;

        public SELF variables(Consumer<RyzeVariables.Builder> consumer) {
            RyzeVariables.Builder builder = RyzeVariables.builder();
            consumer.accept(builder);
            this.variables = (RyzeVariables) Collections.putAllIfNonNull(this.variables, builder.build());
            return self;
        }

        public SELF variables(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RyzeVariables.Builder.class) Closure<?> closure) {
            RyzeVariables.Builder builder = RyzeVariables.builder();
            Groovy.call(closure, builder);
            this.variables = (RyzeVariables) Collections.putAllIfNonNull(this.variables, builder.build());
            return self;
        }

        public SELF variables(Map<? extends String, ?> variables) {
            this.variables = (RyzeVariables) Collections.putAllIfNonNull(this.variables, new RyzeVariables(variables));
            return self;
        }

        public SELF variables(RyzeVariables variables) {
            this.variables = (RyzeVariables) Collections.putAllIfNonNull(this.variables, variables);
            return self;
        }

        public SELF variables(String name, Object value) {
            synchronized (this) {
                if (Objects.isNull(variables)) {
                    synchronized (this) {
                        this.variables = new RyzeVariables();
                    }
                }
            }
            this.variables.put(name, value);
            return self;
        }

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

        public <T extends ExtensibleConfigureElementsBuilder> SELF configureElements(Class<T> type, Customizer<T> customizer) {
            try {
                T builder = type.getConstructor().newInstance();
                customizer.customize(builder);
                this.configureElements = Collections.addAllIfNonNull(this.configureElements, builder.build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return self;
        }

        public SELF configureElements(Customizer<CONFIGURES_BUILDER> customizer) {
            CONFIGURES_BUILDER builder = getConfiguresBuilder();
            customizer.customize(builder);
            this.configureElements = Collections.addAllIfNonNull(this.configureElements, builder.build());
            return self;
        }

        public <T extends ExtensibleConfigureElementsBuilder> SELF configureElements(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            this.configureElements = Collections.addAllIfNonNull(this.configureElements, builder.build());
            return self;
        }

        public SELF configureElements(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "CONFIGURES_BUILDER") Closure<?> closure) {
            CONFIGURES_BUILDER builder = getConfiguresBuilder();
            Groovy.call(closure, builder);
            this.configureElements = Collections.addAllIfNonNull(this.configureElements, builder.build());
            return self;
        }

        public SELF preprocessors(List<Preprocessor> preprocessors) {
            this.preprocessors = Collections.addAllIfNonNull(this.preprocessors, preprocessors);
            return self;
        }

        public <T extends ExtensiblePreprocessorsBuilder> SELF preprocessors(Class<T> type, Customizer<T> customizer) {
            try {
                T builder = type.getConstructor().newInstance();
                customizer.customize(builder);
                this.preprocessors = Collections.addAllIfNonNull(this.preprocessors, builder.build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return self;
        }

        public SELF preprocessors(Customizer<PREPROCESSORS_BUILDER> customizer) {
            PREPROCESSORS_BUILDER builder = getPreprocessorsBuilder();
            customizer.customize(builder);
            this.preprocessors = Collections.addAllIfNonNull(this.preprocessors, builder.build());
            return self;
        }

        public <T extends ExtensiblePreprocessorsBuilder> SELF preprocessors(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            this.preprocessors = Collections.addAllIfNonNull(this.preprocessors, builder.build());
            return self;
        }

        public SELF preprocessors(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "PREPROCESSORS_BUILDER") Closure<?> closure) {
            PREPROCESSORS_BUILDER builder = getPreprocessorsBuilder();
            Groovy.call(closure, builder);
            this.preprocessors = Collections.addAllIfNonNull(this.preprocessors, builder.build());
            return self;
        }

        public SELF postprocessors(List<Postprocessor> postprocessors) {
            this.postprocessors = Collections.addAllIfNonNull(this.postprocessors, postprocessors);
            return self;
        }


        public <T extends ExtensiblePostprocessorsBuilder> SELF postprocessors(Class<T> type, Customizer<T> customizer) {
            try {
                T builder = type.getConstructor().newInstance();
                customizer.customize(builder);
                this.postprocessors = Collections.addAllIfNonNull(this.postprocessors, builder.build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return self;
        }

        public SELF postprocessors(Customizer<POSTPROCESSORS_BUILDER> customizer) {
            POSTPROCESSORS_BUILDER builder = getPostprocessorsBuilder();
            customizer.customize(builder);
            this.postprocessors = Collections.addAllIfNonNull(this.postprocessors, builder.build());
            return self;
        }

        public <T extends ExtensiblePostprocessorsBuilder> SELF postprocessors(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            this.postprocessors = Collections.addAllIfNonNull(this.postprocessors, builder.build());
            return self;
        }

        public SELF postprocessors(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "POSTPROCESSORS_BUILDER") Closure<?> closure) {
            POSTPROCESSORS_BUILDER builder = getPostprocessorsBuilder();
            Groovy.call(closure, builder);
            this.postprocessors = Collections.addAllIfNonNull(this.postprocessors, builder.build());
            return self;
        }

        protected abstract CONFIGURES_BUILDER getConfiguresBuilder();

        protected abstract PREPROCESSORS_BUILDER getPreprocessorsBuilder();

        protected abstract POSTPROCESSORS_BUILDER getPostprocessorsBuilder();
    }

    /**
     * 运行快照数据
     */
    private final class Snapshot {

        private List<Context> parentContextChain;
        private ContextWrapper previousContextWrapper;
        private R testResult;
    }
}
