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

package xyz.migoo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.framework.TestResult;
import xyz.migoo.framework.TestSuite;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.framework.entity.MiGooCase;
import xyz.migoo.framework.functions.FunctionFactory;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.report.Report;
import xyz.migoo.loader.CaseLoader;
import xyz.migoo.report.MiGooLog;

import java.io.File;

import static xyz.migoo.framework.config.Platform.IGNORE_DIRECTORY;

/**
 * @author xiaomi
 * @date 2018/7/24 14:24
 */
public class TestRunner {

    private JSONObject variables;
    private JSONObject globals;
    private Report report = new Report();

    public TestRunner(String projectName) {
        report.setProjectName(projectName);
    }

    /**
     * run test and generate report
     *
     * @param path test case file or dir
     * @param vars test env file
     */
    public void run(String path, String vars) {
        try {
            this.globals = CaseLoader.loadEnv(vars);
            this.initEnv();
            this.byPath(new File(path));
        } catch (ReaderException e) {
            // 绑定全局变量异常 停止测试
            MiGooLog.log("read vars exception.", e);
            System.exit(-1);
        }catch (Exception e) {
            MiGooLog.log("unknown exception.", e);
            System.exit(-1);
        }
        report.generateIndex();
    }

    private void byPath(File path) {
        if (path.isDirectory()) {
            File[] fList = path.listFiles();
            if (fList != null) {
                for (File file : fList) {
                    if (file.getName().startsWith(".") || IGNORE_DIRECTORY.contains(file.getName())) {
                        continue;
                    }
                    this.byPath(file);
                }
            }
        } else {
            TestSuite testSuite = this.initTestSuite(path.getPath());
            TestResult result = this.run(testSuite);
            report.addResult(result);
            report.generateReport();
        }
    }

    private TestSuite initTestSuite(String path) {
        try {
            MiGooCase caseSets = CaseLoader.loadMiGooCase(path);
            return new TestSuite(caseSets);
        } catch (Exception e) {
            MiGooLog.log(e.getMessage(), e);
            System.exit(-1);
        }
        return null;
    }

    private void initEnv() {
        if (globals != null) {
            try {
                variables = globals.getJSONObject("vars") != null ? globals.getJSONObject("vars") :
                        globals.getJSONObject("variables") != null ? globals.getJSONObject("variables") : globals;
                VariableHelper.bindAndEval(variables, variables);
                JSONArray hook = globals.getJSONArray(CaseKeys.HOOK) != null ? globals.getJSONArray(CaseKeys.HOOK) : new JSONArray();
                for (int i = 0; i < hook.size(); i++) {
                    FunctionFactory.execute(hook.getString(i), globals);
                }
            } catch (ExecuteError e) {
                MiGooLog.log("env exception.", e);
                System.exit(-1);
            }
        }
    }

    private TestResult run(TestSuite suite) {
        TestResult result = new TestResult(suite.countTestCases(), suite.getName());
        result.setStartTime(System.currentTimeMillis());
        suite.addVariables(variables);
        suite.run(result);
        result.setEndTime(System.currentTimeMillis());
        return result;
    }
}
