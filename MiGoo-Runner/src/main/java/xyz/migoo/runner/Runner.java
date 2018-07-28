package xyz.migoo.runner;

import xyz.migoo.parser.CaseParser;
import xyz.migoo.report.Report;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2018/7/24 14:24
 */
public class Runner {
    public Runner(){

    }

    public TestResult run(String path, String reportName, String templates){
        CaseSuite caseSuite = this.initTestSuite(path);
        TestResult result = new TestRunner().run(caseSuite);
        Report.genReport(result,reportName,templates);
        return result;
    }

    private CaseSuite initTestSuite(String path){
        List<Map<String, Object>> caseSets = new CaseParser(path).caseSets();
        return new CaseSuite(caseSets);
    }


}