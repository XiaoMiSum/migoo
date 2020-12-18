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
import components.xyz.migoo.reports.Report;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.functions.FunctionHelper;
import core.xyz.migoo.vars.Vars;
import core.xyz.migoo.vars.VarsHelper;

import java.util.Date;
import java.util.Vector;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public abstract class Test implements ITest {

    private final Vars vars = new Vars(100);

    private final Vector<Test> rTests = new Vector<>(10);

    protected Date startTime;

    protected Date endTime;

    private int status = CREATED;

    private Throwable throwable;

    protected final TestContext context;

    Test(TestContext suite) {
        this.context = suite;
        this.vars.putAll(suite.getVariables());
    }

    /**
     * Returns the name of the test. Not all
     * test suites have a name and this method
     * can return null.
     *
     * @return test name
     */
    @Override
    public String getTestName() {
        return context.getName();
    }

    public Object getTestId() {
        return context.getId();
    }

    protected boolean isSkipped() {
        return this.context.isSkipped();
    }

    protected void mergeRequest(TestContext superContext) {
        // headers 同名 key 总用于小的覆盖作用域大的
        JSONObject headers = new JSONObject();
        headers.putAll(superContext.getRequestHeaders());
        headers.putAll(context.getRequestHeaders());
        context.getRequest().putAll(superContext.getRequest());
        context.setRequestHeaders(headers);
        // api 如果当前层级的suite 没有设置api地址，则使用上层的api地址
        context.setRequestApi(superContext.getRequestApi());
    }

    /**
     * add the variables of test.
     *
     * @param key   the variable key
     * @param value the variable value
     */
    protected void addVars(String key, Object value) {
        if (key != null || value != null) {
            this.vars.put(key, value);
        }
    }

    protected void mergeVars(Vars vars) {
        Vars temp = new Vars(100);
        temp.putAll(this.vars);
        this.vars.putAll(vars);
        this.vars.putAll(temp);
    }

    /**
     * get the variables of test.
     */
    protected Vars getVars() {
        return this.vars;
    }

    protected void processVariable() throws FunctionException {
        VarsHelper.convertVariables(vars);
        VarsHelper.convertVariables(context.getRequest(), vars);
    }

    /**
     * invoke the setUp of the test.
     *
     * @throws FunctionException e
     */
    @Override
    public void setup() throws Exception {
        for (int i = 0; i < context.getSetupHook().size(); i++) {
            FunctionHelper.execute(context.getSetupHook().getString(i), vars);
        }
    }

    /**
     * invoke the teardown of the test.
     */
    @Override
    public void teardown() {
        for (int i = 0; i < context.getTeardownHook().size(); i++) {
            try {
                FunctionHelper.execute(context.getTeardownHook().getString(i), vars);
            } catch (FunctionException e) {
                Report.log(context.getTeardownHook().getString(i) + " error", e);
            }
        }
        this.endTime = new Date();
    }

    void addTest(Test test) {
        rTests.add(test);
    }

    protected Vector<Test> getRunTests() {
        return this.rTests;
    }

    @Override
    public int getStatus() {
        return isSkipped() ? SKIPPED : this.status;
    }

    @Override
    public void status(int status) {
        this.status = status;
    }

    void throwable(Throwable throwable) {
        this.throwable = throwable;
    }

    protected void setResult(IResult result) {
        result.setTestId(context.getId());
        result.setStartTime(startTime);
        result.setEndTime(endTime);
        result.setStatus(this.getStatus());
        result.setTestName(context.getName());
        result.setThrowable(throwable);
    }

    @Override
    public IResult run() {
        IResult result = new SuiteResult();
        try {
            this.setup();
            ISuiteResult suiteResult = (ISuiteResult) result;
            if (!isSkipped()) {
                for (Test test : this.getRunTests()) {
                    test.mergeVars(this.getVars());
                    suiteResult.addTestResult(test.run());
                }
                this.status(suiteResult.getErrorCount() > 0 ? ERROR : suiteResult.getNotPassedCount() > 0 ? NOT_PASSED : PASSED);
            }
        } catch (Throwable t) {
            this.throwable(t);
            this.status(ERROR);
            Report.log( "An error occurred in the api test . ", t);
        } finally {
            this.teardown();
            this.setResult(result);
        }
        return result;
    }
}
