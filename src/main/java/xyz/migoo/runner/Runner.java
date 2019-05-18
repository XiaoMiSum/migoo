package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.parser.BindVariable;
import xyz.migoo.parser.CaseParser;
import xyz.migoo.report.Report;
import xyz.migoo.report.EmailUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.io.File;

/**
 * @author xiaomi
 * @date 2018/7/24 14:24
 */
public class Runner {

    private static Log LOG = new Log(Runner.class);
    private boolean isMain = false;
    private static Runner runner;
    private JSONObject variables;
    private Report report = new Report();

    public static Runner getInstance(){
        if (runner == null){
            synchronized (Runner.class){
                if (runner == null){
                    runner = new Runner();
                }
            }
        }
        return runner;
    }

    public static Runner getInstance(boolean isMain){
        if (runner == null){
            synchronized (Runner.class){
                if (runner == null){
                    runner = new Runner(isMain);
                }
            }
        }
        return runner;
    }

    private Runner(){
    }

    private Runner(boolean isMain){
        this.isMain = isMain;
    }

    /**
     * 如果传入的是目录，生成的测试报告在同一个文件中
     * @param caseOrPath json 格式的 case 或 测试用例文件\目录
     * @return
     */
    private TestResult byCase(String caseOrPath){
        TestSuite caseSuite = this.initTestSuite(caseOrPath);
        TestResult result = new TestRunner().run(caseSuite);
        Report.generateReport(result.report(), caseSuite.getName(), isMain, false);
        return result;
    }

    /**
     * 如果传入的是目录，每个测试用例文件生成一个对应的测试报告，最后压缩成 zip 文件
     * @param caseOrPath json 格式的 case 或 测试用例文件\目录
     * @param vars 全局变量
     */
    public void run(String caseOrPath, String vars) {
        LOG.info("run test: " + caseOrPath);
        try {
            this.variables = CaseParser.loadVariables(vars);
            BindVariable.bindVariables(variables, variables);
            JSON.parse(caseOrPath);
            report.addResult(this.byCase(caseOrPath));
        } catch (ReaderException | InvokeException e){
            // 绑定全局变量异常 停止测试
            LOG.error("bind vars exception.", e);
            throw new RuntimeException("bind vars exception.");
        } catch (JSONException e){
            report.addResult(this.byCase(caseOrPath));
        }
        EmailUtil.sendEmail(isMain);
    }

    private TestResult byPath(String path){
        TestResult result = null;
        File file = new File(path);
        if (file.isDirectory()) {
            String[] fList = file.list();
            assert fList != null;
            for (String f : fList) {
                if (StringUtil.contains(f, "vars.")
                        ||f.startsWith(".")) {
                    continue;
                }
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                this.byPath(path + f);
            }
        } else {
            TestSuite testSuite = this.initTestSuite(path);
            result = new TestRunner().run(testSuite);
            Report.generateReport(result.report(), testSuite.getName(), isMain,false);
        }
        return result;
    }

    private TestSuite initTestSuite(String caseOrPath){
        try {
            JSONObject caseSets = new CaseParser().loadCaseSets(caseOrPath);
            return new TestSuite(caseSets, variables);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }
}
