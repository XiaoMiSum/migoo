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

package core.xyz.migoo.sampler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.assertions.AssertionFactory;
import org.apache.commons.lang3.StringUtils;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.MiGooException;
import xyz.migoo.exception.SkippedRun;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.http.MiGooRequest;
import xyz.migoo.report.MiGooLog;
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
        super.addVariables("title", super.getName());
        super.addToGlobals();
        super.initRequest(requestConfig);
        this.checkers = testCase.getJSONArray("checkers");
        this.testCase = testCase;
    }

    @Override
    public void run(TestResult result) {
        try {
            MiGooLog.log("--------------------------------------------------------------------");
            MiGooLog.log("test case begin: {}", this.getName());
            if (super.isSkipped) {
                throw new SkippedRun(this.getName());
            }
            this.processVariable();
            this.execute();
            result.addSuccess(this);
            MiGooLog.log("test case success");
        } catch (MiGooException | Exception e) {
            this.processException(e, result);
        } finally {
            this.checkers(checkers.toJavaList(TestChecker.class));
            super.teardown("case teardown");
            MiGooLog.log("test case end: {}", this.getName());
        }
    }

    private void processException(Throwable throwable, TestResult result) {
        if (throwable instanceof SkippedRun) {
            this.request = null;
            MiGooLog.log("case run skipped");
            result.addSkip(this);
        }
        if (throwable instanceof AssertionFailure) {
            MiGooLog.log("case assert failure");
            result.addFailure(this);
        }
        if (throwable instanceof ExecuteError) {
            MiGooLog.log("case execute error", throwable);
            result.addError(this, throwable);
        }
        if (throwable instanceof Exception) {
            this.request = null;
            MiGooLog.log("case run error", throwable);
            result.addError(this, throwable);
        }
    }

    private void buildRequest() throws ExecuteError {
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

    private void bindRequestVariable() throws ExecuteError {
        VariableHelper.bindAndEval(requestConfig, super.vars);
        if (testCase.get("data") != null) {
            VariableHelper.bindVariable(testCase.getJSONObject("data"), super.vars);
            super.vars.put("data", testCase.get("data"));
        }
        if (testCase.get("body") != null) {
            VariableHelper.bindVariable(testCase.getJSONObject("body"), super.vars);
            super.vars.put("body", testCase.get("body"));
        }
        if (testCase.get("query") != null) {
            VariableHelper.bindVariable(testCase.getJSONObject("query"), super.vars);
            super.vars.put("query", testCase.get("query"));
        }
    }

    private void execute() throws IOException, ExecuteError, HttpException {
        super.setup("case setup");
        this.buildRequest();
        MiGooLog.log("request api: {}", request.uri());
        MiGooLog.log("request header: {}", request.jsonHeaders());
        MiGooLog.log("request cookies: {}", request.cookies());
        if (StringUtils.isNotEmpty(request.query())) {
            MiGooLog.log("request query: {}", request.query());
        }
        if (StringUtils.isNotEmpty(request.data())) {
            MiGooLog.log("request data: {}", request.data());
        }
        if (StringUtils.isNotEmpty(request.body())) {
            MiGooLog.log("request body: {}", request.body());
        }
        super.response = request.execute();
        MiGooLog.log("response body: {}", response.text());
        this.check();
        if (hasFailure) {
            throw new AssertionFailure("assert has failure");
        }
    }

    public void check() {
        for (int i = 0; i < checkers.size(); i++) {
            JSONObject checker = checkers.getJSONObject(i);
            VariableHelper.evalValidate(checker, this.vars);
            if (AssertionFactory.assertThat(checker, response)) {
                checker.put("result", "success");
            } else {
                checker.put("result", "failure");
                this.hasFailure = true;
            }
        }
    }
}
