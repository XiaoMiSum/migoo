package xyz.migoo.runner;

import xyz.migoo.parser.CaseParser;
import xyz.migoo.parser.CaseSet;
import xyz.migoo.report.Report;
import xyz.migoo.utils.StringUtil;

import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/24 14:24
 */
public class Runner {

    public Runner(){
    }

    public TestResult run(String path, String template){
        CaseSuite caseSuite = this.initTestSuite(path);
        TestResult result = new TestRunner().run(caseSuite);
        //Report.generateReport(result.report(), caseSuite.name(), template);
        return result;
    }

    private CaseSuite initTestSuite(String path){
        List<CaseSet> caseSets = new CaseParser().loadCaseSets(path);
        return new CaseSuite(caseSets);
    }


}