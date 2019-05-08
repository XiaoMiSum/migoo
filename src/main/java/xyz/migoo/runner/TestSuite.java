package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.http.Client;
import xyz.migoo.http.Request;
import xyz.migoo.utils.Hook;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.parser.BindVariable;

import java.util.Vector;

/**
 * @author xiaomi
 * @date 2018/7/24 15:37
 */
public class TestSuite extends junit.framework.TestSuite {

    private Vector<TestCase> testCases;
    private int fTests = 0;
    private int eTests = 0;
    private int rTests = 0;

    protected TestSuite(JSONObject caseSet, JSONObject variables) throws InvokeException {
        super();
        this.testCases = new Vector<>(10);
        this.init(caseSet, variables);
        super.setName(caseSet.getString(CaseKeys.NAME));
    }

    private void init(JSONObject caseSet, JSONObject variables) throws InvokeException {
        JSONObject config = caseSet.getJSONObject(CaseKeys.CONFIG);
        JSONObject configVars = config.getJSONObject(CaseKeys.CONFIG_VARIABLES);
        // 处理 变量
        BindVariable.merge(variables, configVars);
        JSONObject request = config.getJSONObject(CaseKeys.CONFIG_REQUEST);
        BindVariable.loopBindVariables(configVars, request);
        // 测试用例执行前的数据准备 相当于 JUnit 的 before class
        Object beforeClass = config.get(CaseKeys.CONFIG_BEFORE_CLASS);
        BindVariable.loopBindVariables(configVars, beforeClass);
        Hook.hook(beforeClass, configVars);
        JSONObject headers = request.getJSONObject(CaseKeys.CONFIG_REQUEST_HEADERS);
        Object encode = request.get(CaseKeys.CONFIG_REQUEST_ENCODE);
        Client client = new Client.Config().https(request.get(CaseKeys.CONFIG_REQUEST_HTTPS)).build();
        JSONArray testCases = caseSet.getJSONArray(CaseKeys.CASE);
        for (int i = 0; i < testCases.size(); i++) {
            JSONObject testCase = testCases.getJSONObject(i);
            JSONObject caseVars = testCase.getJSONObject(CaseKeys.CASE_VARIABLES);
            // 处理变量
            BindVariable.merge(configVars, caseVars);
            // 将 case_variables 中的 绑定到 case
            BindVariable.bind(caseVars, testCase);
            Request.Builder builder = new Request.Builder().method(request.getString(CaseKeys.CONFIG_REQUEST_METHOD));
            this.request(testCase, encode, headers, builder, request);
            Task task = new Task(client, builder);
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
        Object cookies = requestConfig.get(CaseKeys.CONFIG_REQUEST_COOKIE);
        builder.cookies(cookies).headers(headers).url(url.toString()).encode(encode);
    }

    void addTest(Task task, JSONObject testCase){
        TestCase cases = new TestCase(task, testCase, this);
        super.addTest(cases);
    }

    public Vector<TestCase> testCases(){
        return testCases;
    }

    public void addTest(TestCase cases){
        this.testCases.add(cases);
    }


    void addFTests(){
        fTests += 1;
    }

    void addETests(){
        eTests += 1;
    }

    void addRTests(){
        rTests += 1;
    }

    int fTests(){
        return fTests;
    }

    int eTests(){
        return eTests;
    }

    int rTests(){
        return rTests;
    }
}
