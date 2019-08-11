package xyz.migoo.framework.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.assertions.Validator;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.SkippedRun;
import xyz.migoo.framework.http.Client;
import xyz.migoo.framework.http.Request;
import xyz.migoo.framework.http.Response;
import xyz.migoo.parser.BindVariable;
import xyz.migoo.utils.MiGooLog;
import xyz.migoo.utils.TypeUtil;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public class TestCase extends AbstractTest {

    private Request.Builder builder;
    private JSONObject testCase;
    private Client client;

    TestCase(JSONObject testCase, Client client, Request.Builder builder) {
        super();
        super.setName(testCase.getString(CaseKeys.CASE_TITLE));
        super.addSetUp(testCase.getJSONArray(CaseKeys.CASE_BEFORE));
        super.addTeardown(testCase.getJSONArray(CaseKeys.CASE_AFTER));
        super.addVariables(testCase.getJSONObject(CaseKeys.CASE_VARIABLES));
        this.testCase = testCase;
        this.builder = builder;
        this.client = client;
    }

    @Override
    public int countTestCases() {
        return 1;
    }

    @Override
    public void run(TestResult result, JSONObject vars) {
        try {
            MiGooLog.log("--------------------------------------------------------------------");
            MiGooLog.log("test case begin: {}", this.getName());
            if (TypeUtil.booleanOf(testCase.get(CaseKeys.CASE_IGNORE)) == null? false :
                    TypeUtil.booleanOf(testCase.get(CaseKeys.CASE_IGNORE))){
                throw new SkippedRun("");
            }
            // bind variable to variables (testSuite.variables -> this.variables)
            BindVariable.bindVariables(vars, super.variables);
            super.setUp("before");
            // bind variable to case (this.variables -> this.testCase.headers)
            this.evalRequest();
            Request request = builder.build();
            MiGooLog.log("request api: {}", request.url());
            MiGooLog.log("request header: {}", request.headers());
            MiGooLog.log("request cookies: {}", request.cookies());
            MiGooLog.log("request params: {}", request.query() != null ?
                    request.query() : request.data() != null ? request.data() : request.body());
            super.response = client.execute(request);
            MiGooLog.log("response body: {}", response.body());
            this.assertThat(testCase, response);
            super.teardown("after");
            result.addSuccess(this);
            MiGooLog.log("test case success");
        } catch (SkippedRun e) {
            MiGooLog.log("case run skipped");
            result.addSkip(this, e);
        } catch (AssertionFailure e) {
            MiGooLog.log("case assert failure");
            result.addFailure(this, e);
        } catch (Exception e) {
            MiGooLog.log("case run error", e);
            result.addError(this, e);
        }finally {
            MiGooLog.log("test case end: {}", this.getName());
        }
    }

    private void evalRequest() throws InvokeException {
        BindVariable.bind(super.variables, this.testCase.getJSONObject(CaseKeys.CASE_HEADERS), true);
        BindVariable.bind(super.variables, this.testCase.getJSONObject(CaseKeys.CASE_BODY), true);
        BindVariable.bind(super.variables, this.testCase.getJSONObject(CaseKeys.CASE_DATA), true);
        BindVariable.bind(super.variables, this.testCase.getJSONObject(CaseKeys.CASE_QUERY), true);
        builder.title(testCase.getString(CaseKeys.CASE_TITLE))
                .api(testCase.getString(CaseKeys.CONFIG_REQUEST_URL))
                .headers(this.testCase.getJSONObject(CaseKeys.CASE_HEADERS))
                .query(this.testCase.getJSONObject(CaseKeys.CASE_QUERY))
                .data(this.testCase.getJSONObject(CaseKeys.CASE_DATA))
                .body(this.testCase.getJSONObject(CaseKeys.CASE_BODY));
    }

    private void assertThat(JSONObject jsonCase, Response response) throws AssertionFailure, ExecuteError {
        Object validate = jsonCase.get(CaseKeys.VALIDATE);
        if (!(validate instanceof JSON)){
            validate = JSON.parse(validate.toString());
        }
        this.validate(validate);
        Validator.validation(response, (JSON) validate, jsonCase.getJSONObject(CaseKeys.CASE_VARIABLES));
    }
}
