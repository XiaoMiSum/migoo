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
        TestResult result= new TestResult();
        long startTime= System.currentTimeMillis();
        suite.run(result);
        long endTime= System.currentTimeMillis();
        result.setTestSuite(suite);
        result.startAt(startTime);
        result.endAt(endTime);
        result.serialization();
        return result;
    }
}
