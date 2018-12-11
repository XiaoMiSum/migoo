package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.parser.CaseParser;
import xyz.migoo.report.Report;
import xyz.migoo.utils.EmailUtil;

import java.io.File;
import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/24 14:24
 */
public class Runner {

    private boolean isMain = false;
    private static Runner runner;

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

    private Runner(){
    }

    private Runner(boolean isMain){
        this.isMain = isMain;
    }

    public TestResult run(String testSet){
        CaseSuite caseSuite = null;
        try {
            caseSuite = this.initTestSuite(testSet);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        TestResult result = new TestRunner().run(caseSuite);
        Report.generateReport(result.report(), caseSuite.name(), true, isMain);
        return result;
    }

    public void execute(String caseSetOrPath) throws ReaderException {
        try {
            JSON.parse(caseSetOrPath);
            this.run(caseSetOrPath);
        }catch (Exception e){
            this.runByPath(caseSetOrPath);
        }
    }

    private void runByPath(String caseSetOrPath) throws ReaderException {
        File file = new File(caseSetOrPath);
        if (file.isDirectory()){
            for (String f: file.list()){
                runByPath(file.getPath() + File.separator + f);
            }
        }else {
            CaseSuite caseSuite = this.initTestSuite(caseSetOrPath);
            TestResult result = new TestRunner().run(caseSuite);
            Report.generateReport(result.report(), caseSuite.name(), false, isMain);
            EmailUtil.sendEmail(isMain);
        }
    }

    private CaseSuite initTestSuite(String path) throws ReaderException {
        List<JSONObject> caseSets = new CaseParser().loadCaseSets(path);
        return new CaseSuite(caseSets);
    }
}