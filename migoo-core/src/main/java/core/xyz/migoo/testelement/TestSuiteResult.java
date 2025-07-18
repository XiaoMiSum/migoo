package core.xyz.migoo.testelement;

import core.xyz.migoo.report.Result;

public class TestSuiteResult extends Result<TestSuiteResult> {

    public TestSuiteResult(String title) {
        super(title);
    }

    public TestSuiteResult(String id, String title) {
        super(id, title);
    }
}
