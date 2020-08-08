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
import core.xyz.migoo.vars.VarsHelper;
import org.apache.commons.lang3.StringUtils;
import core.xyz.migoo.http.MiGooRequest;
import components.migoo.xyz.reports.Report;
import xyz.migoo.simplehttp.HttpException;

import java.io.IOException;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public class TestCase extends AbstractTest {

    private boolean hasFailure = false;
    private final JSONArray checkers;
    private final JSONObject testCase;

    TestCase(JSONObject testCase, JSONObject requestConfig) {
        super(testCase.getString("title"));
        super.initTest(testCase.getJSONObject("config"), testCase.getJSONObject("dataset"));
        super.addVars("title", super.getTestName());
        super.addToGlobals();
        super.initRequest(requestConfig);
        this.checkers = testCase.getJSONArray("checkers");
        this.testCase = testCase;
    }

    @Override
    public int countTestCases() {
        return 1;
    }

    @Override
    public void run(IResult result) {
        try {
            Report.log("--------------------------------------------------------------------");
            Report.log("test case begin: {}", this.getTestName());
            this.start();
            if (!super.isSkipped) {
                this.processVariable();
                this.run();
            }
        } catch (Throwable t) {
            this.setThrowable(t);
            Report.log("case run failure or assert failure", t);
            this.setStatus("error");
        } finally {
            this.checkers(checkers.toJavaList(TestChecker.class));
            super.teardown();
            this.end();
            this.setStatus(hasFailure ? "failure" : "success");
            Report.log("test case end: {}", this.getTestName());
            result.init(this);
        }
    }

    private void buildRequest() {
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

    private void bindRequestVariable() {
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

    private void run() throws IOException, HttpException {
        super.setup();
        this.buildRequest();
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
        super.response = request.execute();
        Report.log("response body: {}", response.text());
        this.doCheck();
    }

    public void doCheck() {
        for (int i = 0; i < checkers.size(); i++) {
            JSONObject checker = checkers.getJSONObject(i);
            VarsHelper.evalValidate(checker, this.getVars());
            boolean result;
            try {
                result = AssertionFactory.assertThat(checker, response);
            } catch (AssertionError t) {
                this.setThrowable(t);
                result = false;
            }
            checker.put("result", result ? "success" : "failure");
            if (!result) {
                this.hasFailure = true;
            }
        }
    }
}
