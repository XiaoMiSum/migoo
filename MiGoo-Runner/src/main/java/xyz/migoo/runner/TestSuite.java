package xyz.migoo.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.utils.Hook;
import xyz.migoo.utils.Variable;

/**
 * @author xiaomi
 * @date 2018/7/24 15:37
 */
public class TestSuite extends junit.framework.TestSuite{

    private CaseSuite caseSuite;

    public TestSuite(JSONObject caseSet, CaseSuite caseSuite){
        super();
        init(caseSet);
        this.caseSuite = caseSuite;
    }


    private void init(JSONObject caseSet){
        JSONObject config = caseSet.getJSONObject(Dict.CONFIG);
        JSONArray testCases = caseSet.getJSONArray(Dict.CASE);
        JSONObject configRequest = config.getJSONObject(Dict.CONFIG_REQUEST);
        Hook.hook(config.getJSONObject(Dict.CONFIG_VARIABLES), Dict.CONFIG_BEFORE_CLASS);
        JSONObject headers = configRequest.getJSONObject("headers");
        Request.Builder builder = new Request.Builder().method(configRequest.getString("method"));
        for (int i = 0; i < testCases.size(); i++) {
            JSONObject testCase = testCases.getJSONObject(i);
            String title = testCase.getString(Dict.CASE_TITLE);
            JSONObject setUp = testCase.getJSONObject(Dict.CASE_SETUP);
            JSONObject caseHeaders = testCase.getJSONObject(Dict.CASE_HEADERS);
            Variable.evalVariable(setUp);
            Variable.bindVariable(setUp, testCase);
            Hook.hook(setUp, Dict.CASE_SETUP_BEFORE);
            if (caseHeaders != null || !caseHeaders.isEmpty()){
                headers.putAll(caseHeaders);
            }
            Request request = builder.headers(headers)
                                     .url(configRequest.getString("url"))
                                     .body(testCase.getJSONObject(Dict.CASE_BODY))
                    .title(title).build();
            Task task = new Task(request, this);
            this.addTest(title, task, testCase);
        }
    }

    private void addTest(String testName, Task task, JSONObject testCase){
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
