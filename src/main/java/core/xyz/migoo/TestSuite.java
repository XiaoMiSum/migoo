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
import core.xyz.migoo.vars.VarsHelper;

/**
 * @author xiaomi
 * @date 2019-08-10 11:07
 */
public class TestSuite extends AbstractTest {

    private static final String TYPE = TestSuite.class.getSimpleName();

    private JSONObject reportConfig;
    private JSONObject emailConfig;

    public TestSuite(JSONObject suite) {
        super(suite.getString("name"));
        this.initTest(suite.getJSONObject("config"), suite.getJSONObject("dataset"));
        super.addVars("name", this.getTestName());
        super.addToGlobals();
        suite.getJSONArray("sets").forEach(set -> {
            super.addTest(new TestSet((JSONObject) set, requestConfig));
        });
    }

    public JSONObject getReportConfig() {
        return reportConfig;
    }

    public JSONObject getEmailConfig() {
        return emailConfig;
    }

    @Override
    public void initTest(JSONObject config, JSONObject dataset) {
        super.initTest(config, dataset);
        this.reportConfig = config.get("report") == null ? config.getJSONObject("reports") : config.getJSONObject("report");
        this.emailConfig = config.get("email") == null ? config.getJSONObject("mail") : config.getJSONObject("email");
    }

    @Override
    public IResult run() {
        IResult result = new SuiteResult();
        try {
            Report.log("{} begin: {}", TYPE, this.getTestName());
            this.start();
            if (!this.isSkipped) {
                this.processVariable();
                this.setup();
                this.getRunTests().forEach(test -> {
                    test.addVars(getVars());
                    ((ISuiteResult) result).addTestResult(test.run());
                });
            }
        } catch (Throwable t) {
            Report.log(TYPE + " run error. ", t);
            this.setThrowable(t);
        } finally {
            this.end();
            this.teardown();
            result.init(this);
            Report.log("{} end: {}", TYPE, this.getTestName());
        }
        return result;
    }

    @Override
    public void processVariable() throws FunctionException {
        super.processVariable();
        VarsHelper.bindAndEval(reportConfig, super.getVars());
        VarsHelper.bindAndEval(emailConfig, super.getVars());
    }
}
