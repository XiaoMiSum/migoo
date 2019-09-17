package xyz.migoo.framework;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.ExtenderException;
import xyz.migoo.extender.ExtenderHelper;
import xyz.migoo.report.MiGooLog;

import java.util.Vector;

/**
 * @author xiaomi
 * @date 2019-08-10 11:07
 */
public class TestSuite extends AbstractTest {

    private Vector<AbstractTest> fTests= new Vector<>(10);

    public TestSuite(JSONObject testSuite){
        super(testSuite.getString(CaseKeys.NAME));
        JSONObject config = testSuite.getJSONObject(CaseKeys.CONFIG);
        this.initSuite(config);
        // 4. bind variable tp request
        JSONObject request = config.getJSONObject(CaseKeys.CONFIG_REQUEST);
        ExtenderHelper.bind(request, super.variables);
        JSONArray testCases = testSuite.getJSONArray(CaseKeys.CASE);
        for (int i = 0; i < testCases.size(); i++) {
            JSONObject testCase = testCases.getJSONObject(i);
            this.addTest(new TestCase(testCase, request));
        }
    }

    private void initSuite(JSONObject config){
        // 1. add config.variables to variables;
        super.addVariables(config.getJSONObject(CaseKeys.CONFIG_VARIABLES));
        // 2. add config.beforeClass to setUp
        super.addSetUp(config.getJSONArray(CaseKeys.CONFIG_BEFORE_CLASS));
        // 3. add config.beforeClass to teardown
        super.addTeardown(config.getJSONArray(CaseKeys.CONFIG_AFTER_CLASS));
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
            ExtenderHelper.bindAndEval(super.variables, super.variables);
            super.setup("suite setup");
            this.fTests.forEach(test -> {
                test.addVariables(variables);
                test.run(result);
            });
            super.teardown("suite teardown");
        } catch (ExtenderException e) {
            MiGooLog.log("test suite run error. ", e);
        } finally {
            MiGooLog.log("test suite end: {}", this.getName());
        }
    }

    private void addTest(AbstractTest test){
        this.fTests.add(test);
    }
}
