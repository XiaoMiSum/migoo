package xyz.migoo.runner;

import xyz.migoo.exception.ValidatorException;
import xyz.migoo.parser.CaseSet;


/**
 * @author xiaomi
 * @date 2018/7/24 20:37
 */
public class TestCase extends junit.framework.TestCase{

    private Task task;
    private CaseSet.Case testCase;

    public TestCase(String testName, Task task, CaseSet.Case testCase){
        super(testName);
        this.task = task;
        this.testCase = testCase;
    }

    @Override
    public void runTest() throws ValidatorException {
        this.task.run(this.testCase);
    }
}
