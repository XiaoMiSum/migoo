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

/**
 * @author xiaomi
 * @date 2020/7/26 16:24
 */
public class TestSet extends AbstractTest {

    private static final String TYPE = TestSet.class.getSimpleName();

    TestSet(JSONObject set, JSONObject requestConfig) {
        super(set.getString("name"));
        super.initTest(set.getJSONObject("config"), set.getJSONObject("dataset"), requestConfig);
        super.addVars("name", super.getTestName());
        super.addToGlobals();
        super.initRequest(set.getJSONObject("config"));
        JSONObject finalRequestConfig = this.requestConfig;
        set.getJSONArray("cases").forEach(testCase ->
                super.addTest(new TestCase((JSONObject) testCase, finalRequestConfig))
        );
    }

    @Override
    public IResult run() {
        Report.log("===================================================================");
        IResult result = new SuiteResult();
        try {
            Report.log("{} begin: {}", TYPE, this.getTestName());
            this.start();
            if (!this.isSkipped) {
                this.processVariable();
                this.setup();
                for (AbstractTest test : this.getRunTests()) {
                    test.addVars(getVars());
                    this.runTest(test, (ISuiteResult) result);
                }
                this.teardown();
            }
        } catch (Throwable t) {
            Report.log(TYPE + " run error. ", t);
            this.setThrowable(t);
            this.setStatus(ERROR);
        } finally {
            this.end();
            result.init(this);
            Report.log("{} end: {}", TYPE, this.getTestName());
        }
        return result;
    }

    private void runTest(ITest test, ISuiteResult result){
        result.addTestResult(test.run());
        if (test.getStatus() == PASSED) {
            result.addSuccess();
        } else if (test.getStatus() == FAILED) {
            result.addFailure();
        } else if (test.getStatus() == ERROR) {
            result.addError();
        } else if (test.getStatus() == SKIPPED) {
            result.addSkip();
        }
    }
}
