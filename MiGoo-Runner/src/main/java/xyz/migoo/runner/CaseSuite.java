package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.http.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/24 15:55
 */
public class CaseSuite extends junit.framework.TestSuite {

    private List<String> failures;
    private List<String> errors;
    private List<Response> responses;
    private StringBuilder testName;

    public CaseSuite(List<JSONObject> caseSets) throws InvokeException {
        responses = new ArrayList<>();
        failures = new ArrayList<>();
        errors = new ArrayList<>();
        testName = new StringBuilder();
        for (JSONObject caseSet : caseSets){
            TestSuite suite = new TestSuite(caseSet, this);
            testName.append(caseSet.getString(CaseKeys.NAME)).append("&");
            addTest(suite);
        }
    }

    protected void responses(Response response){
        this.responses.add(response);
    }

    protected void failures(String failure){
        this.failures.add(failure);
    }

    protected void errors(String errors){
        this.errors.add(errors);
    }

    public List<String> failures(){
        return this.failures;
    }

    public List<String> errors(){
        return this.errors;
    }

    public List<Response> responses(){
        return this.responses;
    }

    public String name(){
        return testName.delete(testName.length() - 1, testName.length()).toString();
    }
}
