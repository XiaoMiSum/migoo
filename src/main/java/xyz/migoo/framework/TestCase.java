package xyz.migoo.framework;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.assertions.Validator;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.ExtenderException;
import xyz.migoo.exception.SkippedRun;
import xyz.migoo.http.MiGooRequest;
import xyz.migoo.simplehttp.HttpException;
import xyz.migoo.extender.ExtenderHelper;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.TypeUtil;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public class TestCase extends AbstractTest {

    private JSONObject testCase;

    TestCase(JSONObject testCase, JSONObject request) {
        super(testCase.getString(CaseKeys.CASE_TITLE));
        super.addSetUp(testCase.getJSONArray(CaseKeys.CASE_BEFORE));
        super.addTeardown(testCase.getJSONArray(CaseKeys.CASE_AFTER));
        super.addVariables(testCase.getJSONObject(CaseKeys.CASE_VARIABLES));
        this.init(testCase, request);
    }

    private void init(JSONObject testCase, JSONObject request){
        this.testCase = testCase;
        this.request = MiGooRequest.method(request.getString(CaseKeys.CONFIG_REQUEST_METHOD))
        .headers(request.getJSONObject(CaseKeys.CASE_HEADERS));
        String url = request.getString(CaseKeys.CONFIG_REQUEST_URL);
        if (testCase.get(CaseKeys.CASE_API) != null){
            url = url + testCase.get(CaseKeys.CASE_API);
        }

        testCase.put(CaseKeys.CASE_API, url);
    }

    @Override
    public int countTestCases() {
        return 1;
    }

    @Override
    public void run(TestResult result) {
        try {
            MiGooLog.log("--------------------------------------------------------------------");
            MiGooLog.log("test case begin: {}", this.getName());
            if (TypeUtil.booleanOf(testCase.get(CaseKeys.CASE_IGNORE))){
                throw new SkippedRun(this.getName());
            }
            // bind variable to variables (testSuite.variables -> this.variables)
            ExtenderHelper.bindAndEval(super.variables, super.variables);
            super.setup("case setup");
            // bind variable to case (this.variables -> this.testCase.headers)
            this.evalRequest();
            this.executeRequest();
            this.assertThat();
            result.addSuccess(this);
            MiGooLog.log("test case success");
        } catch (SkippedRun e) {
            this.request = null;
            MiGooLog.log("case run skipped");
            result.addSkip(this, e);
        } catch (AssertionFailure e) {
            MiGooLog.log("case assert failure");
            result.addFailure(this, e);
        } catch (Exception e) {
            this.request = null;
            MiGooLog.log("case run error", e);
            result.addError(this, e);
        } finally {
            super.teardown("case teardown");
            MiGooLog.log("test case end: {}", this.getName());
        }
    }

    private void evalRequest() throws ExtenderException {
        ExtenderHelper.bind(this.testCase.getJSONObject(CaseKeys.CASE_HEADERS), super.variables);
        ExtenderHelper.bindAndEval(this.testCase.getJSONObject(CaseKeys.CASE_BODY), super.variables);
        ExtenderHelper.bindAndEval(this.testCase.getJSONObject(CaseKeys.CASE_DATA), super.variables);
        ExtenderHelper.bindAndEval(this.testCase.getJSONObject(CaseKeys.CASE_QUERY), super.variables);
        request.uri(this.testCase.getString(CaseKeys.CASE_API))
                .headers(this.testCase.getJSONObject(CaseKeys.CASE_HEADERS))
                .query(this.testCase.getJSONObject(CaseKeys.CASE_QUERY))
                .data(this.testCase.getJSONObject(CaseKeys.CASE_DATA))
                .body(this.testCase.getJSONObject(CaseKeys.CASE_BODY));
    }

    private void executeRequest() throws HttpException {
        MiGooLog.log("request api: {}", request.uri());
        MiGooLog.log("request header: {}", request.jsonHeaders());
        MiGooLog.log("request cookies: {}", request.cookies());
        MiGooLog.log("request params: {}", request.body());
        super.response = request.execute();
        MiGooLog.log("response body: {}", response.text());
    }

    private void assertThat() throws AssertionFailure, ExecuteError {
        Object validate = testCase.get(CaseKeys.VALIDATE);
        if (!(validate instanceof JSONArray)){
            validate = JSONArray.parseArray(validate.toString());
        }
        this.validate(validate);
        Validator.validation(response, (JSONArray) validate, variables);
    }
}
