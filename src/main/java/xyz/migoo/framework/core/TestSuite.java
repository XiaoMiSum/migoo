package xyz.migoo.framework.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.framework.http.Client;
import xyz.migoo.framework.http.Request;
import xyz.migoo.parser.BindVariable;
import xyz.migoo.utils.MiGooLog;

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
        // 1. add config.variables to variables;
        super.addVariables(config.getJSONObject(CaseKeys.CONFIG_VARIABLES));
        // 2. // bind variable to variables (variables -> variables)
        BindVariable.bind(super.variables, super.variables);
        // 3. add config.beforeClass to setUp
        super.addSetUp(config.getJSONArray(CaseKeys.CONFIG_BEFORE_CLASS));
        // 4. add config.beforeClass to teardown
        super.addTeardown(config.getJSONArray(CaseKeys.CONFIG_AFTER_CLASS));
        // 5. bind variable tp request
        JSONObject request = config.getJSONObject(CaseKeys.CONFIG_REQUEST);
        BindVariable.bind(super.variables, request);
        Client client = new Client.Config().https(request.get(CaseKeys.CONFIG_REQUEST_HTTPS)).build();
        JSONArray testCases = testSuite.getJSONArray(CaseKeys.CASE);
        for (int i = 0; i < testCases.size(); i++) {
            JSONObject testCase = testCases.getJSONObject(i);
            Request.Builder builder = new Request.Builder()
                    .method(request.getString(CaseKeys.CONFIG_REQUEST_METHOD))
                    .url(request.getString(CaseKeys.CONFIG_REQUEST_URL))
                    .cookies(request.get(CaseKeys.CONFIG_REQUEST_COOKIE))
                    .headers(request.getJSONObject(CaseKeys.CONFIG_REQUEST_HEADERS))
                    .encode(request.get(CaseKeys.CONFIG_REQUEST_ENCODE));
            this.addTest(new TestCase(testCase, client, builder));
        }
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
    public void run(TestResult result, JSONObject globals) {
        try {
            MiGooLog.log("===================================================================");
            MiGooLog.log("test suite begin: {}", this.getName());
            // bind variable to variables (globals -> variables)
            BindVariable.bind(globals, super.variables, true);
            super.setUp("before class");
            this.fTests.forEach(test -> test.run(result, super.variables));
            super.teardown("after class");
        } catch (InvokeException e) {
            MiGooLog.log("test suite run error;");
        } finally {
            MiGooLog.log("test suite end: {}", this.getName());
            MiGooLog.log("===================================================================");
        }

    }

    private void addTest(AbstractTest test){
        this.fTests.add(test);
    }
}
