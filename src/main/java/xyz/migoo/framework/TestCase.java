package xyz.migoo.framework;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.*;
import xyz.migoo.framework.assertions.Validator;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.framework.entity.Cases;
import xyz.migoo.http.MiGooRequest;
import xyz.migoo.simplehttp.HttpException;
import xyz.migoo.simplehttp.Response;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public class TestCase extends AbstractTest {

    private Cases testCase;
    private JSONObject requestConfig;

    TestCase(JSONObject request, Cases testCase) {
        super(testCase.getTitle());
        this.initCase(testCase);
        this.requestConfig = request;
    }

    private void initCase(Cases testCase){
        this.testCase = testCase;
        super.variables.put("title", testCase.getTitle());
        super.addSetUp(testCase.getConfig().getBefore());
        super.addTeardown(testCase.getConfig().getAfter());
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
            if (TypeUtil.booleanOf(testCase.getConfig().getIgnore())) {
                throw new SkippedRun(this.getName());
            }
            this.processVariable();
            this.execute();
            result.addSuccess(this);
            MiGooLog.log("test case success");
        } catch (MiGooException e) {
            this.processException(e, result);
        } catch (Exception e) {
            this.request = null;
            result.addError(this, e);
            MiGooLog.log("case run error", e);
        } finally {
            this.validates(testCase.getValidates());
            super.teardown("case teardown");
            MiGooLog.log("test case end: {}", this.getName());
        }
    }

    private void processVariable() throws ExtenderException {
        super.addVariables(testCase.getConfig().getVariables());
        // add variables to VARS, key = case title
        Vars.add(this.getName(), super.variables);
        // bind variable to variables (testSuite.variables -> this.variables)
        VariableHelper.bindAndEval(super.variables, super.variables);
    }

    private void processException(MiGooException ex, TestResult result){
        if (ex instanceof SkippedRun){
            this.request = null;
            MiGooLog.log("case run skipped");
            result.addSkip(this, (SkippedRun) ex);
        }
        if (ex instanceof AssertionFailure){
            MiGooLog.log("case assert failure");
            result.addFailure(this, (AssertionFailure) ex);
        }
    }

    private void buildRequest() throws ExtenderException {
        this.bindRequestVariable();
        this.reorganizeRequest();
        request.uri(testCase.getRequest().getString(CaseKeys.API))
                .headers(testCase.getRequest().getJSONObject(CaseKeys.HEADER))
                .query(testCase.getQuery())
                .data(testCase.getData())
                .body(testCase.getBody());
    }

    private void reorganizeRequest(){
        String url = requestConfig.getString(CaseKeys.URL);
        JSONObject caseRequest = testCase.getRequest();
        JSONObject headers = new JSONObject(10);
        if (caseRequest != null){
            url = url + StringUtil.toEmpty(testCase.getRequest().getString(CaseKeys.API));
            caseRequest.put(CaseKeys.API, url);
        } else {
            caseRequest = new JSONObject(2);
        }
        if (requestConfig.getJSONObject(CaseKeys.HEADER) != null){
            headers.putAll(requestConfig.getJSONObject(CaseKeys.HEADER));
        }
        if (caseRequest.getJSONObject(CaseKeys.HEADER) != null){
            headers.putAll(caseRequest.getJSONObject(CaseKeys.HEADER));
        }
        caseRequest.put(CaseKeys.API, url);
        caseRequest.put(CaseKeys.HEADER, headers);
        testCase.setRequest(caseRequest);
        request = MiGooRequest.method(requestConfig.getString(CaseKeys.METHOD));
    }

    private void bindRequestVariable() throws ExtenderException {
        VariableHelper.bind(requestConfig,  super.variables);
        VariableHelper.bind(testCase.getRequest(), super.variables);
        JSONObject body = testCase.getBody() == null ?
                testCase.getData() == null ? testCase.getQuery() : testCase.getData() : testCase.getBody();
        super.variables.put("body", body);
        VariableHelper.bindAndEval(testCase.getBody(), super.variables);
        VariableHelper.bindAndEval(testCase.getData(), super.variables);
        VariableHelper.bindAndEval(testCase.getQuery(), super.variables);
    }

    private void execute() throws HttpException, ExtenderException {
        super.setup("case setup");
        this.buildRequest();
        MiGooLog.log("request api: {}", request.uri());
        MiGooLog.log("request header: {}", request.jsonHeaders());
        MiGooLog.log("request cookies: {}", request.cookies());
        MiGooLog.log("request params: {}", request.body());
        super.response = request.execute();
        MiGooLog.log("response body: {}", response.text());
        Validator.validation(response, testCase.getValidates(), super.variables);
    }
}
