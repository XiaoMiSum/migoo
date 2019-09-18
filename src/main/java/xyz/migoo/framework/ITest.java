package xyz.migoo.framework;

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
     */
    void run(TestResult result);
}