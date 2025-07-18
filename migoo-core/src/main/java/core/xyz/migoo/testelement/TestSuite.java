package core.xyz.migoo.testelement;

import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.context.SessionContext;

import java.util.ArrayList;
import java.util.List;

public class TestSuite extends TestContainer<TestSuite, TestSuiteResult> {

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult(runtime.id, runtime.title);
    }

    @Override
    protected void execute(ContextWrapper ctx, TestSuiteResult testResult) {
        executeSubSteps(ctx);
    }

    @Override
    protected List<Context> getContextChain(List<Context> parentContext) {
        List<Context> contextChain = new ArrayList<>(parentContext);
        updateCurrentContext(contextChain);
        return contextChain;
    }

    private void updateCurrentContext(List<Context> contextChain) {
        Context context = contextChain.getLast();
        if (context instanceof SessionContext sessionContext) {
            if (runtime.config == null) {
                runtime.config = new TestElementConfigure();
            }
            // 驱动数据覆盖默认变量值
            var configGroup = (TestElementConfigure) runtime.config.merge(sessionContext.getConfigGroup());
            runtime.config = configGroup;
            sessionContext.setConfigGroup(configGroup);
            return;
        }
        throw new IllegalStateException("上下文链非法，缺失 SessionContext");
    }
}
