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
public class TestCase extends Test {

    private final List<Validator> validators = new ArrayList<>();
    private MiGooRequest request;
    private Response response;

    TestCase(TestContext context, TestContext superContext) {
        super(context);
        super.mergeRequest(superContext);
    }

    @Override
    public IResult run() {
        Report.log("--------------------------------------------------------------------");
        IResult result = new TestResult();
        try {
            Report.log("Beginning of the test，case：{}", this.getTestName());
            this.setup();
            if (!isSkipped()) {
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
        }
        return result;
    }

    private void buildRequest() throws FunctionException {
        this.convertRequestVariable();
        request = new MiGooRequest.Builder()
                .method(context.getRequestMethod())
                .protocol(context.getRequestProtocol())
                .host(context.getRequestHost())
                .port(context.getRequestPort())
                .api(context.getRequestApi())
                .headers(context.getRequestHeaders())
                .cookies(context.getRequestCookies())
                .query(context.getQuery())
                .data(context.getData())
                .body(context.getBody())
                .build();
    }

    private void convertRequestVariable() throws FunctionException {
        VarsHelper.convertVariables(context.getRequest(), super.getVars());
        VarsHelper.convertVariables(context.getQuery(), super.getVars());
        VarsHelper.convertVariables(context.getData(), super.getVars());
        VarsHelper.convertVariables(context.getBody(), super.getVars());
        super.addVars("migoo.request.query", context.getQuery());
        super.addVars("migoo.request.data", context.getData());
        super.addVars("migoo.request.body", context.getBody());
    }

    private void printRequestLog() {
        request.printRequestLog();
        Report.log("response body: {}", response.text());
    }

    public void doCheck() throws FunctionException {
        if (context.getValidators().isEmpty()) {
            this.status(PASSED);
            return;
        }
        for (int i = 0; i < context.getValidators().size(); i++) {
            JSONObject data = context.getValidators().get(i);
            VarsHelper.convertVariables(data, this.getVars());
            data.put("migoo.vars", this.getVars());
            Validator validator = AssertionFactory.assertThat(data, response);
            this.status(validator.isPassed() && getStatus() <= 1 ? PASSED :
                    validator.isFailed() && getStatus() != ERROR ? NOT_PASSED :
                            validator.isError() ? ERROR : SKIPPED);
            this.validators.add(validator);
        }
    }

    @Override
    public void setup() throws Exception {
        this.startTime = new Date();
        if (!isSkipped()) {
            this.processVariable();
            super.setup();
            if (context.getSteps() != null) {
                for (TestStep step : context.getSteps()) {
                    step.execute(getVars(), context.getRequest());
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
        r.setSteps(context.getSteps());
        r.setRequest(request);
        r.setResponse(response);
    }
}
