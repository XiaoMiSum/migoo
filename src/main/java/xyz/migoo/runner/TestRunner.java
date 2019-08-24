package xyz.migoo.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.framework.core.TestResult;
import xyz.migoo.framework.core.TestSuite;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.extender.Extender;
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

    private static TestRunner runner;
    private JSONObject variables;
    private JSONObject globals;
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
     * 如果传入的是目录，每个测试用例文件生成一个对应的测试报告，最后压缩成 zip 文件
     *
     * @param caseOrPath json 格式的 case 或 测试用例文件\目录
     * @param vars       全局变量
     */
    public void run(String caseOrPath, String vars) {
        try {
            this.variables = CaseLoader.loadEnv(vars);
            this.initEnv();
            this.byPath(new File(caseOrPath));
        } catch (ReaderException e) {
            // 绑定全局变量异常 停止测试
            MiGooLog.log("read vars exception.", e);
            System.exit(-1);
        }catch (Exception e) {
            MiGooLog.log("unknown exception.", e);
            System.exit(-1);
        }
        report.generateIndex();
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
            TestResult result = this.run(testSuite);
            report.addResult(result);
            report.generateReport();
        }
    }

    private TestSuite initTestSuite(String caseOrPath) {
        try {
            JSONObject caseSets = CaseLoader.loadCaseSet(caseOrPath);
            return new TestSuite(caseSets, globals);
        } catch (Exception e) {
            MiGooLog.log(e.getMessage(), e);
            System.exit(-1);
        }
        return null;
    }

    private void initEnv() {
        if (variables != null) {
            try {
                globals = variables.getJSONObject("vars") != null ? variables.getJSONObject("vars") :
                        variables.getJSONObject("variables") != null ? variables.getJSONObject("variables") : variables;
                Extender.bindAndEval(globals, globals);
                JSONArray hook = variables.getJSONArray(CaseKeys.VARS_HOOK);
                for (int i = 0; i < hook.size(); i++) {
                    Extender.hook(hook.getString(i), globals);
                }
            } catch (InvokeException e) {
                MiGooLog.log("env exception.", e);
                System.exit(-1);
            }
        }
    }

    private TestResult run(TestSuite suite) {
        TestResult result = new TestResult(suite.countTestCases(), suite.getName());
        result.setStartTime(System.currentTimeMillis());
        suite.run(result);
        result.setEndTime(System.currentTimeMillis());
        return result;
    }
}
