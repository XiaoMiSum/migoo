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
        suite.getJSONObject("suite").getJSONArray("sets").forEach(set -> {
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
        this.reportConfig = config.getJSONObject("report");
        this.emailConfig = config.getJSONObject("email");
    }

    @Override
    public void run(IResult result) {
        IResult resultSet = new SuiteResult();
        super.run(resultSet, TYPE);
        result.init(this);
        ((ISuiteResult) result).addTestResult(resultSet);
    }

    @Override
    public void processVariable() {
        super.processVariable();
        VarsHelper.bindAndEval(reportConfig, super.getVars());
        VarsHelper.bindAndEval(emailConfig, super.getVars());
    }
}
