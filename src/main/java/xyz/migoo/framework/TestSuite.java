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


package xyz.migoo.framework;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.entity.Cases;
import xyz.migoo.framework.entity.Config;
import xyz.migoo.framework.entity.MiGooCase;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.report.MiGooLog;

import java.util.List;
import java.util.Vector;

/**
 * @author xiaomi
 * @date 2019-08-10 11:07
 */
public class TestSuite extends AbstractTest {

    private Vector<AbstractTest> fTests= new Vector<>(10);
    private JSONObject request;
    private Config config;

    public TestSuite(MiGooCase testSuite){
        super(testSuite.getName());
        config = testSuite.getConfig();
        this.initSuite(config);
        request = config.getRequest();
        List<Cases> testCases = testSuite.getCases();
        testCases.forEach(testCase -> {
            if (testCase.getConfig() == null){
                testCase.setConfig(new Config());
            }
            this.addTest(new TestCase(request, testCase));
        });
    }

    private void initSuite(Config config){
        // 2. add config.beforeClass to setUp
        super.addSetUp(config.getBeforeClass());
        // 3. add config.beforeClass to teardown
        super.addTeardown(config.getAfterClass());
        Vars.add(super.getName(), super.variables);
    }

    @Override
    public int countTestCases() {
        int count= 0;
        for (ITest each : fTests) {
            count += each.countTestCases();
        }
        return count;
    }

    @Override
    public void run(TestResult result) {
        try {
            MiGooLog.log("===================================================================");
            MiGooLog.log("test suite begin: {}", this.getName());
            // 1. add config.variables to variables;
            super.addVariables(config.getVariables());
            VariableHelper.bindAndEval(super.variables, super.variables);
            VariableHelper.bindAndEval(request, super.variables);
            super.setup("suite setup");
            this.fTests.forEach(test -> {
                test.addVariables(variables);
                test.run(result);
            });
            super.teardown("suite teardown");
        } catch (ExecuteError e) {
            MiGooLog.log("test suite run error. ", e);
        } finally {
            MiGooLog.log("test suite end: {}", this.getName());
        }
    }

    private void addTest(AbstractTest test){
        this.fTests.add(test);
    }
}
