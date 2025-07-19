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

import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.config.ConfigureElement;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.context.TestStepContext;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.filter.ExecuteFilterChain;
import core.xyz.migoo.filter.RunFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.processor.Postprocessor;
import core.xyz.migoo.processor.Preprocessor;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.*;

/**
 * 测试组件抽象类，提供公共属性和方法
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractTestElement<SELF extends AbstractTestElement<SELF, T>, T extends Result<T>>
        implements TestElement<T>, RunFilterChain, ExecuteFilterChain, TestElementConstantsInterface {

    protected String id;

    protected String title;

    protected boolean disabled = false;

    protected TestElementConfigure config;

    protected List<ConfigureElement> configureElements;

    protected List<Preprocessor> preprocessors = new ArrayList<>();

    protected List<Postprocessor> postprocessors = new ArrayList<>();

    protected List<Extractor> extractors;

    protected List<Assertion> assertions;

    protected List<TestFilter> filters;
    // 元数据，可以挂载一些辅助数据
    protected Map<String, Object> metadata = new HashMap<>();
    protected Map<String, Object> rawData;
    protected SELF runtime;
    protected boolean initialized = false;
    private Iterator<TestFilter> runtimeFilters;
    private Iterator<TestFilter> executeFilters;

    public AbstractTestElement() {
    }

    protected void initialized(SessionRunner session) {
        initialized = true;
        runtime = newInstance();
    }

    /**
     * 当前测试元件的上下文链
     *
     * @param parentContext 父上下文链
     */
    protected List<Context> getContextChain(List<Context> parentContext) {
        List<Context> contextChain = new ArrayList<>(parentContext);
        contextChain.add(createCurrentContext());
        return contextChain;
    }

    /**
     * 创建当前测试元件的上下文对象，当子类有额外的需求时重写该方法
     *
     * @return 当前测试元件的上下文对象
     */
    protected TestStepContext createCurrentContext() {
        TestStepContext context = new TestStepContext();
        context.setConfigGroup(runtime.config);
        return context;
    }

    protected void evalVariableConfigItem(ContextWrapper ctx) {
        MiGooVariables item;
        if (runtime.config != null && (item = runtime.config.getVariables()) != null) {
            ctx.eval(item);
        }
    }

    private SELF newInstance() {
        try {
            //noinspection unchecked
            return (SELF) this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("实例化 %s 失败", this.getClass().getName()), e);
        }
    }


    @Override
    public final T run(SessionRunner session) {
        Snapshot snapshot = new Snapshot();
        testStarted(snapshot);
        if (disabled) {
            snapshot.testResult.sampleEnd();
            return snapshot.testResult;
        }
        if (!initialized) {
            initialized(session);
        }

        ContextWrapper context = updateCurrentContextInfo(session, snapshot);
        // 获取所有符合条件的 TestFilter
        handleFilters(context);
        doRun(context);
        restoreCurrentContextInfo(session, snapshot);
        snapshot.testResult.sampleEnd();
        return snapshot.testResult;
    }

    @Override
    public final void doRun(ContextWrapper ctx) {
        if (runtimeFilters.hasNext()) {
            TestFilter next = runtimeFilters.next();
            next.doRun(ctx, this);
        } else {
            internalRun(ctx);
        }
    }

    @Override
    public final void doExecute(ContextWrapper ctx) {
        if (executeFilters.hasNext()) {
            TestFilter next = executeFilters.next();
            next.doExecute(ctx, this);
        } else {
            execute(ctx, (T) ctx.getTestResult());
        }
    }


    private void testStarted(Snapshot snapshot) {
        T result = getTestResult();
        if (Objects.isNull(result)) {
            throw new NullPointerException(
                    String.format("%s#getTestResult() 返回值为 null，请在该方法返回测试组件执行结果对象", this.getClass().getName()));
        }
        result.sampleStart();
        snapshot.testResult = result;
    }

    private void handleFilters(ContextWrapper contextWrapper) {
        filters = contextWrapper.getConfigGroup().get(FILTERS);
        runtimeFilters = filters.iterator();
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
        // List/Map/String 类型对象会深度计算，BeanWrapper 类型会解包返回，Supplier 类型会调用 get 返回，其他类型直接返回
        evalVariableConfigItem(contextWrapper);
        // 处理配置元件
        for (ConfigureElement configureElement : configureElements) {
            configureElement.run(contextWrapper.getSessionRunner());
        }
        // 执行前置动作
        for (Preprocessor preprocessor : preprocessors) {
            if (preprocessor.isDisabled()) {
                continue;
            }
            preprocessor.process(contextWrapper);
        }
        // 执行请求
        executeFilters = filters.iterator();
        doExecute(contextWrapper);
        if (Objects.nonNull(assertions)) {
            for (Assertion assertion : assertions) {
                assertion.assertThat(contextWrapper);
            }
        }
        for (Postprocessor postprocessor : postprocessors) {
            if (postprocessor.isDisabled()) {
                continue;
            }
            postprocessor.process(contextWrapper);
        }
    }

    protected abstract T getTestResult();

    @Override
    public SELF copy() {
        SELF self = newInstance();
        self.title = title;
        self.id = id;
        self.disabled = disabled;
        self.config = config;
        self.metadata = metadata;
        self.preprocessors = preprocessors;
        self.postprocessors = postprocessors;
        self.assertions = assertions;
        self.extractors = extractors;
        return self;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public TestElementConfigure getConfig() {
        return config;
    }

    public void setConfig(TestElementConfigure config) {
        this.config = config;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getRawData() {
        return rawData;
    }

    public void setRawData(Map<String, Object> rawData) {
        this.rawData = rawData;
    }


    /**
     * 测试元件的功能实现，比如发起 HTTP 请求
     */
    protected abstract void execute(ContextWrapper ctx, T testResult);

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
