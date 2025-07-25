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
import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.config.MiGooVariables;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.context.TestSuiteContext;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.filter.ExecuteFilterChain;
import core.xyz.migoo.filter.RunFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.configure.ConfigureElement;
import core.xyz.migoo.testelement.processor.Postprocessor;
import core.xyz.migoo.testelement.processor.Preprocessor;
import support.xyz.migoo.Closeable;
import support.xyz.migoo.KryoUtil;

import java.util.*;

/**
 * 测试组件抽象类，提供公共属性和方法
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractTestElementExecutable<CONFIG extends ConfigureItem<CONFIG>, SELF extends AbstractTestElementExecutable<CONFIG, SELF, T>, T extends Result>
        extends AbstractTestElement<CONFIG, SELF, T>
        implements TestElementExecutable<T>, RunFilterChain, ExecuteFilterChain, TestElementConstantsInterface {

    @JSONField(name = VARIABLES, ordinal = 4)
    protected MiGooVariables variables;
    @JSONField(name = CONFIG_ELEMENTS, ordinal = 5)
    protected List<ConfigureElement> configureElements;

    @JSONField(name = PREPROCESSORS, ordinal = 6)
    protected List<Preprocessor> preprocessors = new ArrayList<>();

    @JSONField(name = POSTPROCESSORS, ordinal = 7)
    protected List<Postprocessor> postprocessors = new ArrayList<>();

    @JSONField(name = VALIDATORS, ordinal = 9)
    protected List<Assertion> assertions;

    @JSONField(name = EXTRACTORS, ordinal = 10)
    protected List<Extractor> extractors;


    protected TestElementConfigure configGroup = new TestElementConfigure();
    private Iterator<TestFilter> runtimeFilters;
    private Iterator<TestFilter> executeFilters;

    public AbstractTestElementExecutable() {
        super();
    }

    protected void initialized(SessionRunner session) {
        super.initialized(session);
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
    public final T run(SessionRunner session) {
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

        doRun(context);
        restoreCurrentContextInfo(session, snapshot);
        testEnded(snapshot);
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
        execute(ctx, (T) ctx.getTestResult());
    }


    private void testStarted(Snapshot snapshot) {
        T result = getTestResult();
        if (Objects.isNull(result)) {
            throw new NullPointerException(
                    String.format("%s#getTestResult() 返回值为 null，请在该方法返回测试组件执行结果对象", this.getClass().getName()));
        }
        result.testStart();
        snapshot.testResult = result;
    }

    private void testEnded(Snapshot snapshot) {
        if (Objects.isNull(configureElements)) {
            snapshot.testResult.testEnd();
            return;
        }
        for (ConfigureElement configureElement : configureElements) {
            if (configureElement instanceof Closeable closeable) {
                closeable.close();
            }
        }
        snapshot.testResult.testEnd();
    }

    protected void handleFilters(ContextWrapper contextWrapper) {
        filters = contextWrapper.getConfigGroup().get(FILTERS);
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
        ContextWrapper contextWrapper = new ContextWrapper(session);
        contextWrapper.setTestElement(this);
        contextWrapper.setTestResult(snapshot.testResult);

        session.setContextWrapper(contextWrapper);
        return contextWrapper;
    }

    private void restoreCurrentContextInfo(SessionRunner session, Snapshot snapshotData) {
        session.setContextChain(snapshotData.parentContextChain);
        session.setContextWrapper(snapshotData.previousContextWrapper);
    }


    private void internalRun(ContextWrapper contextWrapper) {
        // 模板计算：当前元件的变量配置项（不会计算父级元件）
        evalConfig(contextWrapper);
        // 处理配置元件
        if (Objects.nonNull(configureElements)) {
            for (ConfigureElement configureElement : configureElements) {
                configureElement.process(contextWrapper);
            }
        }
        // 执行前置动作
        for (Preprocessor preprocessor : preprocessors) {
            if (preprocessor.isDisabled()) {
                continue;
            }
            preprocessor.process(contextWrapper);
        }
        // 执行请求
        doExecute(contextWrapper);
        if (Objects.nonNull(assertions)) {
            for (Assertion assertion : assertions) {
                assertion.assertThat(contextWrapper);
            }
        }
        if (Objects.nonNull(extractors)) {
            for (Extractor extractor : extractors) {
                extractor.process(contextWrapper);
            }
        }
        for (Postprocessor postprocessor : postprocessors) {
            if (postprocessor.isDisabled()) {
                continue;
            }
            postprocessor.process(contextWrapper);
        }
    }

    @Override
    public SELF copy() {
        SELF self = super.copy();
        self.variables = KryoUtil.copy(variables);
        self.preprocessors = KryoUtil.copy(preprocessors);
        self.postprocessors = KryoUtil.copy(postprocessors);
        self.assertions = KryoUtil.copy(assertions);
        self.extractors = KryoUtil.copy(extractors);
        return self;
    }


    /**
     * 测试元件的功能实现，比如发起 HTTP 请求
     */
    protected abstract void execute(ContextWrapper ctx, T testResult);

    // getter/setter

    public MiGooVariables getVariables() {
        return variables;
    }

    public void setVariables(MiGooVariables variables) {
        this.variables = variables;
    }

    public TestElementConfigure getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(TestElementConfigure configGroup) {
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

    public List<Extractor> getExtractors() {
        return extractors;
    }

    public void setExtractors(List<Extractor> extractors) {
        this.extractors = extractors;
    }

    public List<Assertion> getAssertions() {
        return assertions;
    }

    public void setAssertions(List<Assertion> assertions) {
        this.assertions = assertions;
    }

    private final class Snapshot {

        private List<Context> parentContextChain;
        private ContextWrapper previousContextWrapper;
        private T testResult;
    }
}
