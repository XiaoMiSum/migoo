package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ValidatorException;

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
    public void runTest() throws ValidatorException, Exception {
        this.task.run(this.testCase);
    }
}
