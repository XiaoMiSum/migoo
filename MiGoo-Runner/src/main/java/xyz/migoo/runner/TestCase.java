package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.AssertionException;

/**
 * @author xiaomi
 * @date 2018/7/24 20:37
 */
public class TestCase extends junit.framework.TestCase{

    private Task task;
    private JSONObject testCase;

    public TestCase(String testName, Task task, JSONObject testCase){
        super(testName);
        this.task = task;
        this.testCase = testCase;
    }

    @Override
    public void runTest() throws AssertionException, Exception {
        // todo 此处记录用例执行结果 Exception = error、AssertionException = failed
        this.task.run(this.testCase);
    }
}
