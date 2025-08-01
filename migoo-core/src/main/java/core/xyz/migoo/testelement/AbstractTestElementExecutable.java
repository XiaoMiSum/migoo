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

package core.xyz.migoo.testelement;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.TestStatus;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.config.MiGooVariables;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.context.TestSuiteContext;
import core.xyz.migoo.filter.ExecuteFilterChain;
import core.xyz.migoo.filter.RunFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.configure.ConfigureElement;
import core.xyz.migoo.testelement.processor.Postprocessor;
import core.xyz.migoo.testelement.processor.Preprocessor;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import support.xyz.migoo.Closeable;
import support.xyz.migoo.Collections;
import support.xyz.migoo.Customizer;
import support.xyz.migoo.KryoUtil;
import support.xyz.migoo.groovy.Groovy;

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
        extends AbstractTestElement<SELF, CONFIG, R> implements TestElementExecutable<R>, RunFilterChain, ExecuteFilterChain, TestElementConstantsInterface {

    @JSONField(name = VARIABLES, ordinal = 4)
    protected MiGooVariables variables;

    @JSONField(name = CONFIG_ELEMENTS, ordinal = 5)
    protected List<ConfigureElement> configureElements;

    @JSONField(name = PREPROCESSORS, ordinal = 6)
    protected List<Preprocessor> preprocessors = new ArrayList<>();

    @JSONField(name = POSTPROCESSORS, ordinal = 7)
    protected List<Postprocessor> postprocessors = new ArrayList<>();


    protected TestElementConfigureGroup configGroup = new TestElementConfigureGroup();
    private Iterator<TestFilter> runtimeFilters;
    private Iterator<TestFilter> executeFilters;

    public AbstractTestElementExecutable() {
        super();
    }

    public AbstractTestElementExecutable(Builder builder) {
        super(builder);
        this.variables = builder.variables;
        this.configureElements = builder.configureElements;
        this.preprocessors = builder.preprocessors;
        this.postprocessors = builder.postprocessors;
        this.filters = builder.filters;
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
            variables = new MiGooVariables();
        }
        runtime.configGroup.put(VARIABLES, variables.merge(parentContext.getLast().getConfigGroup().getVariables()));
        context.setConfigGroup(runtime.getConfigGroup());
        contextChain.add(context);
        return contextChain;
    }


    protected void evalConfig(ContextWrapper ctx) {
        // 变量与config
        MiGooVariables item;
        if (runtime.variables != null && (item = runtime.configGroup.getVariables()) != null) {
            ctx.eval(item);
        }
        CONFIG config;
        if (runtime.config != null && (config = runtime.configGroup.get(CONFIG)) != null) {
            ctx.eval(config);
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
            initialized(session);
        }
        testStarted(snapshot);
        ContextWrapper context = updateCurrentContextInfo(session, snapshot);
        handleFilters(context);
        doRun(context);
        restoreCurrentContextInfo(session, snapshot);
        testEnd(context);
        snapshot.testResult.testEnd();
        return snapshot.testResult;
    }

    @Override
    public final void doRun(ContextWrapper ctx) {
        if (runtimeFilters.hasNext()) {
            TestFilter next = runtimeFilters.next();
            next.doRun(ctx, this);
            return;
        }
        internalRun(ctx);
    }

    @Override
    public final void doExecute(ContextWrapper ctx) {
        if (executeFilters.hasNext()) {
            TestFilter next = executeFilters.next();
            next.doExecute(ctx, this);
            return;
        }
        execute(ctx, (R) ctx.getTestResult());
    }


    private void testStarted(Snapshot snapshot) {
        R result = getTestResult();
        if (Objects.isNull(result)) {
            throw new NullPointerException(
                    String.format("%s#getTestResult() 返回值为 null，请在该方法返回测试组件执行结果对象", this.getClass().getName()));
        }
        result.testStart();
        snapshot.testResult = result;
    }

    protected void handleFilters(ContextWrapper context) {
        super.handleFilters(context);
        runtimeFilters = Objects.isNull(filters) ? Collections.emptyIterator() : filters.iterator();
        executeFilters = Objects.isNull(filters) ? Collections.emptyIterator() : filters.iterator();
    }


    private ContextWrapper updateCurrentContextInfo(SessionRunner session, Snapshot snapshot) {
        // 记录更新前的上下文信息
        List<Context> parentContextChain = session.getContextChain();
        ContextWrapper previousContextWrapper = session.getContextWrapper();
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
        context.setTestResult(snapshot.testResult);

        session.setContextWrapper(context);
        return context;
    }

    private void restoreCurrentContextInfo(SessionRunner session, Snapshot snapshotData) {
        session.setContextChain(snapshotData.parentContextChain);
        session.setContextWrapper(snapshotData.previousContextWrapper);
    }

    protected void internalRun(ContextWrapper context) {
        // 模板计算：当前元件的变量配置项（不会计算父级元件）
        evalConfig(context);
        // 处理配置元件
        if (Objects.nonNull(configureElements)) {
            for (ConfigureElement configureElement : configureElements) {
                configureElement.process(context);
            }
        }
        // 执行前置动作
        for (Preprocessor preprocessor : preprocessors) {
            if (preprocessor.isDisabled() || context.getTestResult().getStatus() != TestStatus.passed) {
                continue;
            }
            preprocessor.process(context);
        }
        // 执行请求
        doExecute(context);
        for (Postprocessor postprocessor : postprocessors) {
            if (postprocessor.isDisabled() || context.getTestResult().getStatus() != TestStatus.passed) {
                continue;
            }
            postprocessor.process(context);
        }
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
    protected abstract void execute(ContextWrapper ctx, R testResult);

    protected void testEnd(ContextWrapper context) {
        if (Objects.isNull(configureElements)) {
            return;
        }
        for (ConfigureElement configureElement : configureElements) {
            if (configureElement instanceof Closeable closeable) {
                closeable.close();
            }
        }
    }
    // getter/setter

    public MiGooVariables getVariables() {
        return variables;
    }

    public void setVariables(MiGooVariables variables) {
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

        protected MiGooVariables variables;

        protected List<ConfigureElement> configureElements;

        protected List<Preprocessor> preprocessors;

        protected List<Postprocessor> postprocessors;

        public SELF variables(Consumer<MiGooVariables.Builder> consumer) {
            MiGooVariables.Builder builder = MiGooVariables.builder();
            consumer.accept(builder);
            this.variables = builder.build();
            return self;
        }

        public SELF variables(Map<? extends String, ?> variables) {
            this.variables = new MiGooVariables(variables);
            return self;
        }

        public SELF variables(MiGooVariables variables) {
            this.variables = variables;
            return self;
        }

        public SELF variables(String name, Object value) {
            synchronized (this) {
                if (Objects.isNull(variables)) {
                    synchronized (this) {
                        this.variables = new MiGooVariables();
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

        public SELF preprocessors(Customizer<PREPROCESSORS_BUILDER> customizer) {
            PREPROCESSORS_BUILDER builder = getPreprocessorsBuilder();
            customizer.customize(builder);
            this.preprocessors = Collections.addAllIfNonNull(this.preprocessors, builder.build());
            return self;
        }

        public SELF preprocessors(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "PREPROCESSORS_BUILDER") Closure<?> closure) {
            PREPROCESSORS_BUILDER builder = getPreprocessorsBuilder();
            Groovy.call(closure, builder);
            this.preprocessors = Collections.addAllIfNonNull(this.preprocessors, builder.build());
            return self;
        }

        public SELF postprocessors(Customizer<POSTPROCESSORS_BUILDER> customizer) {
            POSTPROCESSORS_BUILDER builder = getPostprocessorsBuilder();
            customizer.customize(builder);
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
