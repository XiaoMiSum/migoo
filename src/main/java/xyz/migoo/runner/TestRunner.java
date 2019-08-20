package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.utils.Hook;
import xyz.migoo.framework.core.TestResult;
import xyz.migoo.framework.core.TestSuite;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.parser.BindVariable;
import xyz.migoo.parser.CaseParser;
import xyz.migoo.report.Report;
import xyz.migoo.utils.MiGooLog;

import java.io.File;

import static xyz.migoo.framework.config.Platform.IGNORE_DIRECTORY;

/**
 * @author xiaomi
 * @date 2018/7/24 14:24
 */
public class TestRunner {

    private static TestRunner runner;
    private JSONObject variables;
    private Report report = new Report();

    public static TestRunner getInstance() {
        if (runner == null) {
            synchronized (TestRunner.class) {
                if (runner == null) {
                    runner = new TestRunner();
                }
            }
        }
        return runner;
    }

    private TestRunner() {
    }

    /**
     * 如果传入的是目录，生成的测试报告在同一个文件中
     *
     * @param caseOrPath json 格式的 case 或 测试用例文件\目录
     */
    private void byCase(String caseOrPath) {
        TestSuite caseSuite = this.initTestSuite(caseOrPath);
        this.initEnv();
        TestResult result = this.run(caseSuite);
        report.addResult(result);
        report.generateReport();
    }

    /**
     * 如果传入的是目录，每个测试用例文件生成一个对应的测试报告，最后压缩成 zip 文件
     *
     * @param caseOrPath json 格式的 case 或 测试用例文件\目录
     * @param vars       全局变量
     */
    public void run(String caseOrPath, String vars) {
        try {
            this.variables = CaseParser.loadVariables(vars);
            JSON.parse(caseOrPath);
            this.byCase(caseOrPath);
        } catch (ReaderException e) {
            // 绑定全局变量异常 停止测试
            MiGooLog.log("read vars exception.", e);
            System.exit(-1);
        } catch (JSONException e) {
            this.byPath(new File(caseOrPath));
        } catch (Exception e) {
            MiGooLog.log("unknown exception.", e);
            System.exit(-1);
        }
        report.serialization();
        report.index();
        //EmailUtil.sendEmail();
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
            this.initEnv();
            TestResult result = this.run(testSuite);
            report.addResult(result);
            report.generateReport();
        }
    }

    private TestSuite initTestSuite(String caseOrPath) {
        try {
            JSONObject caseSets = new CaseParser().loadCaseSets(caseOrPath);
            return new TestSuite(caseSets);
        } catch (Exception e) {
            MiGooLog.log(e.getMessage(), e);
            System.exit(-1);
        }
        return null;
    }

    private void initEnv() {
        if (variables != null) {
            try {
                JSONObject globals = variables.getJSONObject("vars") != null ? variables.getJSONObject("vars") :
                        variables.getJSONObject("variables") != null ? variables.getJSONObject("variables") : variables;
                BindVariable.bind(globals, variables, true);
                Hook.hook(variables.get(CaseKeys.VARS_HOOK), globals);
            } catch (InvokeException e) {
                MiGooLog.log("env exception.", e);
                System.exit(-1);
            }
        }
    }

    private TestResult run(TestSuite suite) {
        TestResult result = new TestResult(suite.countTestCases(), suite.getName());
        result.setStartTime(System.currentTimeMillis());
        suite.run(result, variables);
        result.setEndTime(System.currentTimeMillis());
        return result;
    }
}
