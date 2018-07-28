package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2018/7/24 15:37
 */
public class TestSuite extends junit.framework.TestSuite{

    private CaseSuite caseSuite;


    public TestSuite(Map<String, Object> caseSet, CaseSuite caseSuite){
        super();
        init(caseSet);
        this.caseSuite = caseSuite;
    }

    private void init(Map<String, Object> caseSuite){
        // todo 丑陋的代码 初版开发完毕后 重构
        String url = (String) ((Map)((Map)caseSuite.get("config")).get("request")).get("url");
        String method = (String) ((Map)((Map)caseSuite.get("config")).get("request")).get("method");
        JSONObject headers = (JSONObject)((Map)((Map)caseSuite.get("config")).get("request")).get("headers");
        Request.Builder builder = new Request.Builder().url(url).method(method).headers(headers);
        List testCaseList = (List) caseSuite.get("cases");
        for (int i = 0; i < testCaseList.size(); i++) {
            JSONObject testCase = ((JSONObject) testCaseList.get(i));
            Map body = (Map) testCase.get("body");
            String title = (String) testCase.get("title");
            Request request = builder.body(body).title(title).build();
            RequestRunner runner = new RequestRunner(request, this);
            addTestToSuite(title, runner, testCase);
        }
    }

    private void addTestToSuite(String testName, RequestRunner runner, JSONObject testCase){
        TestCase cases = new TestCase(testName, runner, testCase);
        addTest(cases);
    }

    protected void failures(String failure) {
        this.caseSuite.failures(failure);
    }

    protected void response(Response response){
        this.caseSuite.responses(response);
    }
}
