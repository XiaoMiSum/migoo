package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ValidatorException;


/**
 * @author xiaomi
 * @date 2018/7/24 20:37
 */
public class TestCase extends junit.framework.TestCase{

    private RequestRunner runner;
    private JSONObject testCase;

    public TestCase(String testName,RequestRunner runner, JSONObject testCase){
        super(testName);
        this.runner = runner;
        this.testCase = testCase;
    }

    @Override
    public void runTest() throws ValidatorException {
        this.runner.run(this.testCase);
    }
}
