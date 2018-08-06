package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.parser.CaseSet;
import xyz.migoo.utils.Variable;

/**
 * @author xiaomi
 * @date 2018/7/24 15:37
 */
public class TestSuite extends junit.framework.TestSuite{

    private CaseSuite caseSuite;

    public TestSuite(CaseSet caseSet, CaseSuite caseSuite){
        super();
        init(caseSet);
        this.caseSuite = caseSuite;
    }


    private void init(CaseSet caseSet){
        JSONObject requestConfig = caseSet.getConfig().getRequest();

        String url = requestConfig.getString("url");
        String method = requestConfig.getString("method");
        JSONObject headers = requestConfig.getJSONObject("headers");

        Request.Builder builder = new Request.Builder().url(url).method(method).headers(headers);
        for (CaseSet.Case testCase : caseSet.getCases()) {
            String title = testCase.getTitle();
            JSONObject body = testCase.getBody();
            JSONObject setUp = testCase.getSetUp();

            Variable.bindVariable(setUp, body);

            Request request = builder.body(body).title(title).build();
            Task task = new Task(request, this);
            this.addTest(title, task, testCase);
        }
    }

    private void addTest(String testName, Task task, CaseSet.Case testCase){
        TestCase cases = new TestCase(testName, task, testCase);
        super.addTest(cases);
    }

    protected void failures(String failure) {
        this.caseSuite.failures(failure);
    }

    protected void response(Response response){
        this.caseSuite.responses(response);
    }
}
