package xyz.migoo.runner;

/**
 * @author xiaomi
 * @date 2018/7/25 15:21
 */
public class TestRunner extends junit.textui.TestRunner{
    public TestRunner(){
        super();
    }

    public TestResult run(CaseSuite suite){
        TestResult result= suite.testResult();
        long startTime= System.currentTimeMillis();
        suite.run(result);
        long endTime= System.currentTimeMillis();
        result.responses(suite.responses());
        result.failures(suite.failures());
        result.startAt(startTime);
        result.endAt(endTime);
        result.serialization();
        return result;
    }
}
