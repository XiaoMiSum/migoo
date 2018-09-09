package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;
import xyz.migoo.http.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/24 15:55
 */
public class CaseSuite extends junit.framework.TestSuite{

    private List<String> failures;
    private List<Response> responses;
    private TestResult testResult;
    private StringBuilder testName;

    public CaseSuite(List<JSONObject> caseSets){
        responses = new ArrayList<>();
        failures = new ArrayList<>();
        testResult = new TestResult();
        testName = new StringBuilder();
        for (JSONObject caseSet: caseSets){
            TestSuite suite = new TestSuite(caseSet, this);
            testName.append(caseSet.getString(Dict.NAME)).append("&");
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

    public String name(){
        return testName.delete(testName.length() - 1, testName.length()).toString();
    }
}
