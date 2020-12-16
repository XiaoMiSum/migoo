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

import java.util.Date;

/**
 * @author xiaomi
 * @date 2020/7/26 16:24
 */
public class TestSet extends AbstractTest {

    TestSet(JSONObject set, JSONObject requestConfig) {
        super(set.getString("name"), set.get("id"));
        super.initTest(set.getJSONObject("config"), set.getJSONObject("dataset"));
        super.initRequest(requestConfig);
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
            Report.log("Beginning of the test，api：{}", this.getTestName());
            this.setup();
            ISuiteResult suiteResult = (ISuiteResult) result;
            if (!this.isSkipped) {
                for (AbstractTest test : this.getRunTests()) {
                    test.mergeVars(this.getVars());
                    suiteResult.addTestResult(test.run());
                }
                this.status(suiteResult.getErrorCount() > 0 ? ERROR : suiteResult.getNotPassedCount() > 0 ? FAILED : PASSED);
            }
        } catch (Throwable t) {
            this.throwable(t);
            this.status(ERROR);
            Report.log( "An error occurred in the api test . ", t);
        } finally {
            this.teardown();
            this.setResult(result);
            Report.log("End of the test");
        }
        return result;
    }

    @Override
    public void setup() throws Exception {
        this.startTime = new Date();
        if (!this.isSkipped) {
            this.processVariable();
            super.setup();
        }
    }
}
