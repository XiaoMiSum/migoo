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
        if (variables != null) {
            vars = variables.getJSONObject("variables") == null ?
                    variables.getJSONObject("vars") : variables.getJSONObject("variables");
            vars = vars != null ? vars : variables;
            Hook.hook(variables.get("hook"), vars);
        }
        JSONObject config = caseSet.getJSONObject(CaseKeys.CONFIG);
        JSONObject configVars = config.getJSONObject(CaseKeys.CONFIG_VARIABLES);
        // 1. 处理 config.variables 中的变量 (合并 + 计算)
        BindVariable.merge(vars, configVars);
        // 2. 执行数据准备工作 beforeClass 中的只能使用准确数据 或 步骤1 能计算出结果的变量
        Object beforeClass = config.get(CaseKeys.CONFIG_BEFORE_CLASS);
        Hook.hook(beforeClass, configVars);
        // 3. 再次 计算 + 绑定 变量
        BindVariable.loopBindVariables(configVars, configVars);
        JSONObject request = config.getJSONObject(CaseKeys.CONFIG_REQUEST);
        // 4. 将变量绑定到 request 中
        BindVariable.loopBindVariables(configVars, request);
        JSONObject headers = request.getJSONObject(CaseKeys.CONFIG_REQUEST_HEADERS);
        Object encode = request.get(CaseKeys.CONFIG_REQUEST_ENCODE);
        Client client = new Client.Config().https(request.get(CaseKeys.CONFIG_REQUEST_HTTPS)).build();
        JSONArray testCases = caseSet.getJSONArray(CaseKeys.CASE);
        for (int i = 0; i < testCases.size(); i++) {
            JSONObject testCase = testCases.getJSONObject(i);
            JSONObject caseVars = testCase.getJSONObject(CaseKeys.CASE_VARIABLES);
            // 1. 处理 case.variables 中的变量 （合并 + 计算）
            BindVariable.merge(configVars, caseVars);
            // 2. 执行数据准备工作 before 中的只能使用准确数据 或 步骤1 能计算出结果的变量
            Hook.hook(testCase.get(CaseKeys.CASE_BEFORE), configVars);
            // 3. 再次 计算 + 绑定 变量
            BindVariable.loopBindVariables(configVars, configVars);
            // 4. case_variables 中的 绑定到 case
            BindVariable.bind(caseVars, testCase);
            Request.Builder builder = new Request.Builder().method(request.getString(CaseKeys.CONFIG_REQUEST_METHOD));
            this.request(testCase, encode, headers, builder, request);
            Task task = new Task(client, builder);
            this.addTest(task, testCase);
        }
    }

    private void request(JSONObject testCase, Object encode, JSONObject headers, Request.Builder builder, JSONObject requestConfig) {
        JSONObject caseHeaders = testCase.getJSONObject(CaseKeys.CASE_HEADERS);
        if (caseHeaders != null && !caseHeaders.isEmpty()) {
            headers.putAll(caseHeaders);
        }
        StringBuilder url = new StringBuilder(requestConfig.getString(CaseKeys.CONFIG_REQUEST_URL));
        String apiUrl = testCase.getString(CaseKeys.CASE_API);
        if (StringUtil.isNotBlank(apiUrl)) {
            url.append(apiUrl);
        }
        encode = testCase.get(CaseKeys.CONFIG_REQUEST_ENCODE) != null ? testCase.get(CaseKeys.CONFIG_REQUEST_ENCODE) : encode;
        Object cookies = requestConfig.get(CaseKeys.CONFIG_REQUEST_COOKIE);
        builder.cookies(cookies).headers(headers).url(url.toString()).encode(encode);
    }

    private void addTest(Task task, JSONObject testCase) {
        TestCase cases = new TestCase(task, testCase, this);
        super.addTest(cases);
    }

    Vector<TestCase> testCases() {
        return testCases;
    }

    void addTest(TestCase cases) {
        this.testCases.add(cases);
    }


    void addFTests() {
        fTests += 1;
    }

    void addETests() {
        eTests += 1;
    }

    void addRTests() {
        rTests += 1;
    }

    void addIgnore() {
        ignore += 1;
    }

    public int fTests() {
        return fTests;
    }

    public int eTests() {
        return eTests;
    }

    public int rTests() {
        return rTests;
    }

    public int iTests() {
        return ignore;
    }

    public int getSuccessCount() {
        return rTests - ignore - fTests - eTests;
    }
}
