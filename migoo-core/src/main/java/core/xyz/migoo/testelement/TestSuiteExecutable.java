package core.xyz.migoo.testelement;

import core.xyz.migoo.config.EmptyConfigureItem;
import core.xyz.migoo.config.TestElementConfigure;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.context.SessionContext;

import java.util.ArrayList;
import java.util.List;

@Alias("__testsuite__")
public class TestSuiteExecutable extends TestContainerExecutable<EmptyConfigureItem, TestSuiteExecutable, TestSuiteResult> {

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
            if (runtime.configGroup == null) {
                runtime.configGroup = new TestElementConfigure();
            }
            // 驱动数据覆盖默认变量值
            var configGroup = (TestElementConfigure) runtime.configGroup.merge(sessionContext.getConfigGroup());
            runtime.configGroup = configGroup;
            sessionContext.setConfigGroup(configGroup);
            return;
        }
        throw new IllegalStateException("上下文链非法，缺失 SessionContext");
    }
}
