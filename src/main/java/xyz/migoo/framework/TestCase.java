/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package xyz.migoo.framework;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.*;
import xyz.migoo.framework.assertions.Validator;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.framework.entity.Cases;
import xyz.migoo.http.MiGooRequest;
import xyz.migoo.simplehttp.HttpException;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

import java.io.IOException;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public class TestCase extends AbstractTest {

    private Cases testCase;
    private JSONObject requestConfig;
    private boolean hasFailure = false;

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
        } catch (MiGooException | Exception e) {
            this.processException(e, result);
        } finally {
            this.validates(testCase.getValidates());
            super.teardown("case teardown");
            MiGooLog.log("test case end: {}", this.getName());
        }
    }

    private void processVariable() throws ExecuteError {
        super.addVariables(testCase.getConfig().getVariables());
        // add variables to VARS, key = case title
        Vars.add(this.getName(), super.variables);
        // bind variable to variables (testSuite.variables -> this.variables)
        VariableHelper.bindAndEval(super.variables, super.variables);
    }

    private void processException(Throwable throwable, TestResult result){
        if (throwable instanceof SkippedRun){
            this.request = null;
            MiGooLog.log("case run skipped");
            result.addSkip(this);
        }
        if (throwable instanceof AssertionFailure){
            MiGooLog.log("case assert failure");
            result.addFailure(this);
        }
        if (throwable instanceof ExecuteError){
            MiGooLog.log("case execute error", throwable);
            result.addError(this,  throwable);
        }
        if (throwable instanceof Exception){
            this.request = null;
            MiGooLog.log("case run error", throwable);
            result.addError(this, throwable);
        }
    }

    private void buildRequest() throws ExecuteError {
        this.bindRequestVariable();
        this.reorganizeRequest();
        request.uri(testCase.getConfig().getRequest().getString(CaseKeys.API))
                .headers(testCase.getConfig().getRequest().getJSONObject(CaseKeys.HEADER))
                .query(testCase.getQuery())
                .data(testCase.getData())
                .body(testCase.getBody());
    }

    private void reorganizeRequest(){
        JSONObject caseRequest = testCase.getConfig().getRequest() == null ? new JSONObject(2) : testCase.getConfig().getRequest();
        String url = requestConfig.getString(CaseKeys.URL) + StringUtil.toEmpty(caseRequest.getString(CaseKeys.API));
        JSONObject headers = new JSONObject(10);
        if (requestConfig.getJSONObject(CaseKeys.HEADER) != null){
            headers.putAll(requestConfig.getJSONObject(CaseKeys.HEADER));
        }
        if (caseRequest.getJSONObject(CaseKeys.HEADER) != null){
            headers.putAll(caseRequest.getJSONObject(CaseKeys.HEADER));
        }
        caseRequest.put(CaseKeys.API, url);
        caseRequest.put(CaseKeys.HEADER, headers);
        testCase.getConfig().setRequest(caseRequest);
        request = MiGooRequest.method(requestConfig.getString(CaseKeys.METHOD));
    }

    private void bindRequestVariable() throws ExecuteError {
        VariableHelper.bindAndEval(requestConfig,  super.variables);
        VariableHelper.bindAndEval(testCase.getConfig().getRequest(), super.variables);
        JSONObject body = testCase.getBody() == null ?
                testCase.getData() == null ? testCase.getQuery() : testCase.getData() : testCase.getBody();
        super.variables.put("body", body);
        VariableHelper.bindVariable(body, super.variables);
    }

    private void execute() throws HttpException, ExecuteError, IOException {
        super.setup("case setup");
        this.buildRequest();
        MiGooLog.log("request api: {}", request.uri());
        MiGooLog.log("request header: {}", request.jsonHeaders());
        MiGooLog.log("request cookies: {}", request.cookies());
        MiGooLog.log("request params: {}", request.body());
        super.response = request.execute();
        MiGooLog.log("response body: {}", response.text());
        Validator.validation(this, testCase.getValidates());
        if (hasFailure){
            throw new AssertionFailure("assert has failure");
        }
    }

    public void hasFailure(){
        this.hasFailure = true;
    }
}
