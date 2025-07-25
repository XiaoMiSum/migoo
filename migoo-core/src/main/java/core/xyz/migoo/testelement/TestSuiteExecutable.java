package core.xyz.migoo.testelement;

import core.xyz.migoo.config.EmptyConfigureItem;
import core.xyz.migoo.context.ContextWrapper;

@Alias("__testsuite__")
public class TestSuiteExecutable extends TestContainerExecutable<EmptyConfigureItem, TestSuiteExecutable, TestSuiteResult> {

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult(runtime.id, runtime.title);
    }

    @Override
    protected void execute(ContextWrapper ctx, TestSuiteResult testResult) {
        executeChildren(ctx);
    }

}
