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

    private final JSONArray validators;
    private final JSONObject testCase;
    private MiGooRequest request;
    private Response response;
    private boolean hasFailure = false;

    TestCase(JSONObject testCase, JSONObject requestConfig) {
        super(testCase.getString("title"));
        super.initTest(testCase.getJSONObject("config"), testCase.getJSONObject("dataset"));
        super.addVars("title", super.getTestName());
        super.addToGlobals();
        super.initRequest(requestConfig);
        this.validators = testCase.getJSONArray("validators");
        this.testCase = testCase;
    }

    @Override
    public IResult run() {
        IResult result = new TestResult();
        try {
            Report.log("--------------------------------------------------------------------");
            Report.log("test case begin: {}", this.getTestName());
            this.setup();
            if (!super.isSkipped) {
                this.response = request.execute();
                this.printRequestLog();
                this.doCheck();
                this.status(hasFailure ? FAILED : PASSED);
            }
        } catch (Throwable t) {
            this.throwable(t);
            this.status(ERROR);
            Report.log("case run error or assert failed", t);
        } finally {
            super.teardown();
            this.setResult(result);
            Report.log("test case end: {}", this.getTestName());
        }
        return result;
    }

    private void buildRequest() throws FunctionException {
        this.bindRequestVariable();
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

    private void bindRequestVariable() throws FunctionException {
        VarsHelper.convertVariables(requestConfig, super.getVars());
        if (testCase.get("data") != null) {
            VarsHelper.convertVariables(testCase.getJSONObject("data"), super.getVars());
            super.addVars("data", testCase.get("data"));
        }
        if (testCase.get("body") != null) {
            VarsHelper.convertVariables(testCase.getJSONObject("body"), super.getVars());
            super.addVars("body", testCase.get("body"));
        }
        if (testCase.get("query") != null) {
            VarsHelper.convertVariables(testCase.getJSONObject("query"), super.getVars());
            super.addVars("query", testCase.get("query"));
        }
    }

    private void printRequestLog() {
        Report.log("request api: {}", request.uriNotContainsParam());
        if (request.jsonHeaders() != null && !request.jsonHeaders().isEmpty()) {
            Report.log("request header: {}", request.jsonHeaders());
        }
        if (request.cookies() != null && !request.cookies().isEmpty()) {
            Report.log("request cookies: {}", request.cookies());
        }
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

    public void doCheck() throws FunctionException {
        for (int i = 0; i < validators.size(); i++) {
            JSONObject validator = validators.getJSONObject(i);
            VarsHelper.convertVariables(validator, this.getVars());
            try {
                if (AssertionFactory.assertThat(validator, response)) {
                    validator.put("result", "passed");
                } else {
                    validator.put("result", "failed");
                    hasFailure = true;
                }
            } catch (AssertionError t) {
                validator.put("result", "failed");
                validator.put("throwable", t);
                this.hasFailure = true;
            } catch (Exception e) {
                validator.put("throwable", e);
                validator.put("result", "error");
                this.status(ERROR);
            }
        }
    }

    private List<Validator> validators() {
        return validators != null ? validators.toJavaList(Validator.class) : new ArrayList<>();
    }

    @Override
    public void setup() throws FunctionException {
        this.startTime = new Date();
        if (!this.isSkipped) {
            this.processVariable();
            super.setup();
            this.buildRequest();
        }
    }

    @Override
    protected void setResult(IResult result) {
        TestResult r = (TestResult) result;
        super.setResult(result);
        r.setValidators(this.validators());
        r.setRequest(request);
        r.setResponse(response);
    }
}
