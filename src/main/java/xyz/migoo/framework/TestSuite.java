package xyz.migoo.framework;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.entity.Cases;
import xyz.migoo.framework.entity.Config;
import xyz.migoo.framework.entity.MiGooCase;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.report.MiGooLog;

import java.util.List;
import java.util.Vector;

/**
 * @author xiaomi
 * @date 2019-08-10 11:07
 */
public class TestSuite extends AbstractTest {

    private Vector<AbstractTest> fTests= new Vector<>(10);
    private JSONObject request;

    public TestSuite(MiGooCase testSuite){
        super(testSuite.getName());
        Config config = testSuite.getConfig();
        this.initSuite(config);
        request = config.getRequest();
        List<Cases> testCases = testSuite.getCases();
        testCases.forEach(testCase -> {
            if (testCase.getConfig() == null){
                testCase.setConfig(new Config());
            }
            this.addTest(new TestCase(request, testCase));
        });
    }

    private void initSuite(Config config){
        // 1. add config.variables to variables;
        super.addVariables(config.getVariables());
        // 2. add config.beforeClass to setUp
        super.addSetUp(config.getBeforeClass());
        // 3. add config.beforeClass to teardown
        super.addTeardown(config.getAfterClass());

    }

    @Override
    public int countTestCases() {
        int count= 0;
        for (ITest each : fTests) {
            count += each.countTestCases();
        }
        return count;
    }

    @Override
    public void run(TestResult result) {
        try {
            MiGooLog.log("===================================================================");
            MiGooLog.log("test suite begin: {}", this.getName());
            // bind variable to variables (globals -> variables)
            VariableHelper.bindAndEval(super.variables, super.variables);
            VariableHelper.bind(request, super.variables);
            super.setup("suite setup");
            this.fTests.forEach(test -> {
                test.addVariables(variables);
                test.run(result);
            });
            super.teardown("suite teardown");
        } catch (ExecuteError e) {
            MiGooLog.log("test suite run error. ", e);
        } finally {
            MiGooLog.log("test suite end: {}", this.getName());
        }
    }

    private void addTest(AbstractTest test){
        this.fTests.add(test);
    }
}
