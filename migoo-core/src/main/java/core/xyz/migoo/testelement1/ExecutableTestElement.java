package core.xyz.migoo.testelement1;

import core.xyz.migoo.ContextWrapper;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.config.ConfigureElement;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.filter.ExecuteFilterChain;
import core.xyz.migoo.filter.RunFilterChain;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.processor.PostProcessor;
import core.xyz.migoo.processor.PreProcessor;
import core.xyz.migoo.report.Result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public abstract class ExecutableTestElement<SELF extends AbstractTestElement<SELF, T>, T extends Result>
        extends AbstractTestElement<SELF, T> implements RunFilterChain, ExecuteFilterChain {

    protected List<ConfigureElement> configureElements;

    protected List<PreProcessor> preprocessors = new ArrayList<>();

    protected List<PostProcessor> postprocessors = new ArrayList<>();

    protected List<Extractor> extractors;

    protected List<Assertion> assertions;

    protected List<TestFilter> filters;
    private Iterator<TestFilter> runFilters;
    private Iterator<TestFilter> executeFilters;

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
        if (runFilters.hasNext()) {
            TestFilter next = runFilters.next();
            next.doRun(ctx, this);
        } else {
            internalRun(ctx);
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
        runFilters = filters.iterator();
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


    protected abstract T getTestResult();


    public List<ConfigureElement> getConfigureElements() {
        return configureElements;
    }

    public void setConfigureElements(List<ConfigureElement> configureElements) {
        this.configureElements = configureElements;
    }

    public List<PreProcessor> getPreprocessors() {
        return preprocessors;
    }

    public void setPreprocessors(List<PreProcessor> preprocessors) {
        this.preprocessors = preprocessors;
    }

    public List<PostProcessor> getPostprocessors() {
        return postprocessors;
    }

    public void setPostprocessors(List<PostProcessor> postprocessors) {
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
