/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package core.xyz.migoo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.assertions.AssertionFactory;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.vars.VarsHelper;
import org.apache.commons.lang3.StringUtils;
import core.xyz.migoo.http.MiGooRequest;
import components.migoo.xyz.reports.Report;
import xyz.migoo.simplehttp.Response;

import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public class TestCase extends AbstractTest {

    private final JSONArray arrayCheck;
    private final JSONObject testCase;
    private MiGooRequest request;
    private Response response;
    private List<TestChecker> checkers;
    private boolean hasFailure = false;

    TestCase(JSONObject testCase, JSONObject requestConfig) {
        super(testCase.getString("title"));
        super.initTest(testCase.getJSONObject("config"), testCase.getJSONObject("dataset"));
        super.addVars("title", super.getTestName());
        super.addToGlobals();
        super.initRequest(requestConfig);
        this.arrayCheck = testCase.getJSONArray("checkers");
        this.testCase = testCase;
    }

    @Override
    public int countTestCases() {
        return 1;
    }

    @Override
    public IResult run() {
        IResult result = new TestResult();
        try {
            Report.log("--------------------------------------------------------------------");
            Report.log("test case begin: {}", this.getTestName());
            this.start();
            if (!super.isSkipped) {
                this.processVariable();
                super.setup();
                this.buildRequest();
                this.response = request.execute();
                this.printRequestLog();
                this.doCheck();
            }
            this.setStatus(hasFailure ? FAILURE : SUCCESS);
        } catch (Throwable t) {
            this.setThrowable(t);
            Report.log("case run failure or assert failure", t);
            this.setStatus(ERROR);
        } finally {
            this.checkers(arrayCheck.toJavaList(TestChecker.class));
            super.teardown();
            this.end();
            Report.log("test case end: {}", this.getTestName());
            result.init(this);
        }
        return result;
    }

    private void buildRequest() throws FunctionException {
        this.bindRequestVariable();
        request = MiGooRequest.create(requestConfig.getString("method"))
                .protocol(requestConfig.getString("protocol"))
                .host(requestConfig.getString("host"))
                .port(requestConfig.getInteger("port"))
                .api(requestConfig.getString("api"))
                .headers(requestConfig.getJSONObject("headers"))
                .query(requestConfig.getJSONObject("query"))
                .data(requestConfig.getJSONObject("data"))
                .body(requestConfig.getJSONObject("body"))
                .build();
    }

    private void bindRequestVariable() throws FunctionException {
        VarsHelper.bindAndEval(requestConfig, super.getVars());
        if (testCase.get("data") != null) {
            VarsHelper.bindVariable(testCase.getJSONObject("data"), super.getVars());
            super.addVars("data", testCase.get("data"));
        }
        if (testCase.get("body") != null) {
            VarsHelper.bindVariable(testCase.getJSONObject("body"), super.getVars());
            super.addVars("body", testCase.get("body"));
        }
        if (testCase.get("query") != null) {
            VarsHelper.bindVariable(testCase.getJSONObject("query"), super.getVars());
            super.addVars("query", testCase.get("query"));
        }
    }

    private void printRequestLog() {
        Report.log("request api: {}", request.uri());
        Report.log("request header: {}", request.jsonHeaders());
        Report.log("request cookies: {}", request.cookies());
        if (StringUtils.isNotEmpty(request.query())) {
            Report.log("request query: {}", request.query());
        }
        if (StringUtils.isNotEmpty(request.data())) {
            Report.log("request data: {}", request.data());
        }
        if (StringUtils.isNotEmpty(request.body())) {
            Report.log("request body: {}", request.body());
        }
        Report.log("response body: {}", response.text());
    }

    public void doCheck() {
        for (int i = 0; i < arrayCheck.size(); i++) {
            JSONObject checker = arrayCheck.getJSONObject(i);
            VarsHelper.evalValidate(checker, this.getVars());
            try {
                boolean result = AssertionFactory.assertThat(checker, response);
                checker.put("result", result ? "success" : "failure");
                this.hasFailure = hasFailure && !result ? true : false;
            } catch (AssertionError t) {
                checker.put("result", "failure");
                checker.put("throwable", t);
                this.hasFailure = true;
            } catch (Exception e) {
                checker.put("throwable", e);
                checker.put("result", "error");
                this.setStatus(ERROR);
            }
        }
    }

    public MiGooRequest request() {
        return request;
    }

    public Response response() {
        return response;
    }

    public List<TestChecker> checkers() {
        return checkers;
    }

    void checkers(List<TestChecker> checkers) {
        this.checkers = checkers;
    }
}
