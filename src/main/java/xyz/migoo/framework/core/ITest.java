package xyz.migoo.framework.core;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xiaomi
 * @date 2019-08-10 11:02
 */
public interface ITest {

    /**
     * Returns count of the test.
     * can return zero.
     *
     * @return test count
     */
    int countTestCases();

    /**
     * running the test
     *
     * @param result the test result
     * @param vars   runtime variables
     */
    void run(TestResult result, JSONObject vars);
}