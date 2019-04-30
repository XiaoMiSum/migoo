package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.http.Client;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.utils.Hook;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.Variable;

import java.util.Vector;

/**
 * @author xiaomi
 * @date 2018/7/24 15:37
 */
public class TestSuite extends junit.framework.TestSuite {

    private CaseSuite caseSuite;
    private Vector<TestCase> testCases;

    protected TestSuite(JSONObject caseSet, CaseSuite caseSuite) throws InvokeException {
        super();
        this.init(caseSet);
        this.caseSuite = caseSuite;
        this.testCases = new Vector<>(10);
    }

    private void init(JSONObject caseSet) throws InvokeException {
        JSONObject variables = caseSet.getJSONObject(CaseKeys.CONFIG).getJSONObject(CaseKeys.CONFIG_VARIABLES);
        // 防止还存在未被计算的方法变量，再次做一次绑定
        Variable.loopBindVariables(variables, variables);
        Variable.loopBindVariables(variables, caseSet);
        JSONObject config = caseSet.getJSONObject(CaseKeys.CONFIG);
        JSONObject request = config.getJSONObject(CaseKeys.CONFIG_REQUEST);
        JSONArray testCases = caseSet.getJSONArray(CaseKeys.CASE);
        // 测试用例执行前的数据准备 相当于 JUnit 的 before class
        Object beforeClass = config.get(CaseKeys.CONFIG_BEFORE_CLASS);
        if (beforeClass instanceof String){
            beforeClass = JSON.parseArray(beforeClass.toString());
        }
        Hook.hook((JSONArray) beforeClass);
        JSONObject headers = request.getJSONObject(CaseKeys.CONFIG_REQUEST_HEADERS);
        Object encode = request.get(CaseKeys.CONFIG_REQUEST_ENCODE);
        Client client = new Client.Config().https(request.get(CaseKeys.CONFIG_REQUEST_HTTPS)).build();
        for (int i = 0; i < testCases.size(); i++) {
            Request.Builder builder = new Request.Builder().method(request.getString(CaseKeys.CONFIG_REQUEST_METHOD));
            JSONObject testCase = testCases.getJSONObject(i);
            JSONObject caseVars = testCase.getJSONObject(CaseKeys.CASE_VARIABLES);
            // 防止还存在未被计算的方法变量，再次做一次绑定
            Variable.bindVariable(variables, caseVars);
            Variable.loopBindVariables(caseVars, caseVars);
            Variable.loopBindVariables(caseVars, testCase);
            this.request(testCase, encode, headers, builder, request);
            Task task = new Task(client, builder);
            testCase.put(CaseKeys.CASE_VARIABLES, caseVars);
            this.addTest(task, testCase);
        }
    }

    private void request(JSONObject testCase, Object encode, JSONObject headers, Request.Builder builder, JSONObject requestConfig){
        JSONObject caseHeaders = testCase.getJSONObject(CaseKeys.CASE_HEADERS);
        if (caseHeaders != null && !caseHeaders.isEmpty()){
            headers.putAll(caseHeaders);
        }
        StringBuilder url = new StringBuilder(requestConfig.getString(CaseKeys.CONFIG_REQUEST_URL));
        String apiUrl = testCase.getString(CaseKeys.CASE_API);
        if (StringUtil.isNotBlank(apiUrl)){
            url.append(apiUrl);
        }
        if (testCase.get(CaseKeys.CONFIG_REQUEST_ENCODE) != null){
            encode = testCase.get(CaseKeys.CONFIG_REQUEST_ENCODE);
        }
        String cookies = requestConfig.getString(CaseKeys.CONFIG_REQUEST_COOKIE);
        cookies = cookies.replaceAll("'", "\\\"").replaceAll("=", ",").replaceAll("「","{").replaceAll("」", "}");
        builder.cookies(cookies).headers(headers).url(url.toString()).encode(encode);
    }

    private void addTest(Task task, JSONObject testCase){
        TestCase cases = new TestCase(task, testCase, caseSuite);
        this.testCases.add(cases);
    }

    public Vector<TestCase> testCases(){
        return testCases;
    }
}
