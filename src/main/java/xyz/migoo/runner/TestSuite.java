package xyz.migoo.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.http.Client;
import xyz.migoo.http.Request;
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
    private int ignore = 0;

    protected TestSuite(JSONObject caseSet, JSONObject variables) throws InvokeException {
        super();
        this.testCases = new Vector<>(10);
        this.init(caseSet, variables);
        super.setName(caseSet.getString(CaseKeys.NAME));
    }

    private void init(JSONObject caseSet, JSONObject variables) throws InvokeException {
        // 执行 variables 中的 hook
        JSONObject vars = null;
        if (variables != null){
            vars = variables.getJSONObject("variables");
            if (vars == null){
                vars = variables;
            }
            Hook.hook(variables.getJSONObject("hook"), variables.getJSONObject("variables"));
        }
        JSONObject config = caseSet.getJSONObject(CaseKeys.CONFIG);
        JSONObject configVars = config.getJSONObject(CaseKeys.CONFIG_VARIABLES);
        // 先执行数据准备工作 beforeClass 中的只能使用准确数据 或 vars 中的变量
        Object beforeClass = config.get(CaseKeys.CONFIG_BEFORE_CLASS);
        Hook.hook(beforeClass, configVars);
        // 然后再处理 config.variables 中的变量
        BindVariable.merge(vars, configVars);
        JSONObject request = config.getJSONObject(CaseKeys.CONFIG_REQUEST);
        BindVariable.loopBindVariables(configVars, request);
        JSONObject headers = request.getJSONObject(CaseKeys.CONFIG_REQUEST_HEADERS);
        Object encode = request.get(CaseKeys.CONFIG_REQUEST_ENCODE);
        Client client = new Client.Config().https(request.get(CaseKeys.CONFIG_REQUEST_HTTPS)).build();
        JSONArray testCases = caseSet.getJSONArray(CaseKeys.CASE);
        for (int i = 0; i < testCases.size(); i++) {
            JSONObject testCase = testCases.getJSONObject(i);
            // 先执行数据准备工作 before 中的只能使用准确数据 或 vars、config.vars 中的变量
            Hook.hook(testCase.get(CaseKeys.CASE_AFTER), configVars);
            // 然后再处理 case.variables 中的变量
            JSONObject caseVars = testCase.getJSONObject(CaseKeys.CASE_VARIABLES);
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

    void addIgnore(){
        ignore += 1;
    }

    public int fTests(){
        return fTests;
    }

    public int eTests(){
        return eTests;
    }

    public int rTests(){
        return rTests;
    }

    public int getIgnoreCount(){
        return ignore;
    }

    public int getSuccessCount(){
        return rTests - ignore - fTests - eTests;
    }
}
