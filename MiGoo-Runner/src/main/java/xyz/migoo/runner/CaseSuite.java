package xyz.migoo.runner;

import xyz.migoo.http.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2018/7/24 15:55
 */
public class CaseSuite extends junit.framework.TestSuite{

    private List<String> failures;
    private List<Response> responses;
    private TestResult testResult;

    public CaseSuite(List<Map<String, Object>> caseSets){
        responses = new ArrayList<>();
        failures = new ArrayList<>();
        testResult = new TestResult();
        for (Map<String, Object> caseSet: caseSets){
            TestSuite suite = new TestSuite(caseSet, this);
            addTest(suite);
        }
    }
    protected TestResult testResult(){
        return testResult;
    }

    protected void responses(Response responses){
        this.responses.add(responses);
    }

    protected void failures(String failure){
        this.failures.add(failure);
    }

    public List<String> failures(){
        return this.failures;
    }

    public List<Response> responses(){
        return this.responses;
    }
}
