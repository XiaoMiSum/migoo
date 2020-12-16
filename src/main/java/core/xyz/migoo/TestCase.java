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
import core.xyz.migoo.http.MiGooRequest;
import components.xyz.migoo.reports.Report;
import xyz.migoo.simplehttp.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public class TestCase extends AbstractTest {

    private final List<Validator> validators = new ArrayList<>();
    private final JSONObject testCase;
    private MiGooRequest request;
    private Response response;
    private List<TestStep> steps;

    TestCase(JSONObject testCase, JSONObject requestConfig) {
        super(testCase.getString("title"), testCase.get("id"));
        this.initTest(testCase.get("config"), testCase.get("dataset"), testCase.get("steps"));
        super.initRequest(requestConfig);
        this.testCase = testCase;
    }

    @Override
    public IResult run() {
        IResult result = new TestResult();
        try {
            Report.log("--------------------------------------------------------------------");
            Report.log("Beginning of the test，case：{}", this.getTestName());
            this.setup();
            if (!super.isSkipped) {
                this.response = request.execute();
                this.doCheck();
                this.printRequestLog();
            }
        } catch (Throwable t) {
            this.throwable(t);
            this.status(ERROR);
            Report.log("An error occurred in the test case. ", t);
        } finally {
            super.teardown();
            this.setResult(result);
            Report.log("End of the test case");
        }
        return result;
    }

    public void initTest(Object config, Object dataset, Object steps) {
        this.initTest((JSONObject) config, (JSONObject) dataset);
        if (steps != null) {
            this.steps = ((JSONArray) steps).toJavaList(TestStep.class);
        }
    }

    private void buildRequest() throws FunctionException {
        this.convertRequestVariable();
        request = new MiGooRequest.Builder()
                .method(requestConfig.getString("method"))
                .protocol(requestConfig.getString("protocol"))
                .host(requestConfig.getString("host"))
                .port(requestConfig.getInteger("port"))
                .api(requestConfig.getString("api"))
                .headers(requestConfig.getJSONObject("headers"))
                .cookies(requestConfig.get("cookies") != null ?
                        requestConfig.get("cookie") : requestConfig.get("cookies"))
                .query(testCase.getJSONObject("query"))
                .data(testCase.getJSONObject("data"))
                .body(testCase.get("body"))
                .build();
    }

    private void convertRequestVariable() throws FunctionException {
        VarsHelper.convertVariables(requestConfig, super.getVars());
        if (testCase.get("data") != null) {
            JSONObject data = testCase.getJSONObject("data");
            VarsHelper.convertVariables(data, super.getVars());
            super.addVars("migoo.request.data", data);
        }
        if (testCase.get("migoo.request.body") != null) {
            JSONObject body = testCase.getJSONObject("body");
            VarsHelper.convertVariables(body, super.getVars());
            super.addVars("migoo.request.body", body);
        }
        if (testCase.get("query") != null) {
            JSONObject query = testCase.getJSONObject("query");
            VarsHelper.convertVariables(query, super.getVars());
            super.addVars("migoo.request.query", query);
        }
    }

    private void printRequestLog() {
        request.printRequestLog();
        Report.log("response body: {}", response.text());
    }

    public void doCheck() throws FunctionException {
        JSONArray validators = testCase.getJSONArray("validators");
        if (validators != null) {
            for (int i = 0; i < validators.size(); i++) {
                JSONObject data = validators.getJSONObject(i);
                VarsHelper.convertVariables(data, this.getVars());
                data.put("migoo.vars", this.getVars());
                Validator validator = AssertionFactory.assertThat(data, response);
                this.status(validator.isPassed() && getStatus() <= 1 ? PASSED :
                        validator.isFailed() && getStatus() != ERROR ? FAILED :
                                validator.isError() ? ERROR : SKIPPED);
                this.validators.add(validator);
            }
        } else {
            this.status(PASSED);
        }
    }

    @Override
    public void setup() throws Exception {
        this.startTime = new Date();
        if (!this.isSkipped) {
            this.processVariable();
            super.setup();
            if (steps != null) {
                for (TestStep step : steps) {
                    step.execute(getVars(), requestConfig);
                }
            }
            this.buildRequest();
        }
    }

    @Override
    protected void setResult(IResult result) {
        TestResult r = (TestResult) result;
        super.setResult(result);
        r.setValidators(validators);
        r.setSteps(steps);
        r.setRequest(request);
        r.setResponse(response);
    }
}
