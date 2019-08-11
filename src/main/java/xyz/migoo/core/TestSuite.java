package xyz.migoo.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import junit.framework.Test;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.SkippedRun;
import xyz.migoo.http.Client;
import xyz.migoo.http.Request;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.parser.BindVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author xiaomi
 * @date 2018/7/24 15:37
 */
public class TestSuite extends junit.framework.TestSuite {

    private static Log log = new Log(TestSuite.class);
    private Vector<CaseResult> caseResults;

    public TestSuite(JSONObject caseSet, JSONObject variables){
        super();
        this.caseResults = new Vector<>();
        this.init(caseSet, variables);
        super.setName(caseSet.getString(CaseKeys.NAME));
    }

    private void init(JSONObject caseSet, JSONObject variables) {
        try {
            // 执行 variables 中的 hook
            JSONObject vars = null;
            if (variables != null) {
                vars = variables.getJSONObject("variables") == null ?
                        variables.getJSONObject("vars") == null ? variables : variables.getJSONObject("vars") : variables.getJSONObject("variables");
                Hook.hook(variables.get("hook"), vars);
            }
            JSONObject config = caseSet.getJSONObject(CaseKeys.CONFIG);
            // 1. 处理 config.variables 中的变量 (合并 + 计算)
            JSONObject configVars = BindVariable.merge(vars, config.getJSONObject(CaseKeys.CONFIG_VARIABLES));
            // 2. 执行数据准备工作 beforeClass 中的只能使用准确数据 或 步骤1 能计算出结果的变量
            Object beforeClass = config.get(CaseKeys.CONFIG_BEFORE_CLASS);
            Hook.hook(beforeClass, configVars);
            // 3. 再次 计算 + 绑定 变量
            BindVariable.loopBindVariables(configVars, configVars);
            JSONObject request = config.getJSONObject(CaseKeys.CONFIG_REQUEST);
            // 4. 将变量绑定到 request 中
            BindVariable.loopBindVariables(configVars, request);
            Client client = new Client.Config().https(request.get(CaseKeys.CONFIG_REQUEST_HTTPS)).build();
            JSONArray testCases = caseSet.getJSONArray(CaseKeys.CASE);
            for (int i = 0; i < testCases.size(); i++) {
                JSONObject testCase = testCases.getJSONObject(i);
                try {
                    // 1. 处理 case.variables 中的变量 （合并 + 计算）
                    JSONObject caseVars = BindVariable.merge(configVars, testCase.getJSONObject(CaseKeys.CASE_VARIABLES));
                    // 2. 执行数据准备工作 before 中的只能使用准确数据 或 步骤1 能计算出结果的变量
                    Hook.hook(testCase.get(CaseKeys.CASE_BEFORE), caseVars);
                    // 3. 再次 计算 + 绑定 变量
                    BindVariable.loopBindVariables(caseVars, caseVars);
                    // 4. case_variables 中的 绑定到 case
                    BindVariable.bind(caseVars, testCase);
                    Request.Builder builder = new Request.Builder().method(request.getString(CaseKeys.CONFIG_REQUEST_METHOD));
                    this.request(testCase, builder, request);
                    super.addTest(new TestCase(new Task(client, builder), testCase, this));
                }catch (InvokeException e){
                    log.error("run case exception", e);
                    this.addCaseResult(new CaseResult().name(testCase.getString(CaseKeys.CASE_TITLE))
                            .error().throwable(e).validates(testCase.getJSONArray(CaseKeys.VALIDATE)));
                }
            }
        } catch (Exception e){
            log.error("run case exception", e);
        }
    }

    private void request(JSONObject testCase, Request.Builder builder, JSONObject request) {
        JSONObject headers = request.getJSONObject(CaseKeys.CONFIG_REQUEST_HEADERS);
        JSONObject caseHeaders = testCase.getJSONObject(CaseKeys.CASE_HEADERS);
        if (caseHeaders != null && !caseHeaders.isEmpty()) {
            headers.putAll(caseHeaders);
        }
        StringBuilder url = new StringBuilder(request.getString(CaseKeys.CONFIG_REQUEST_URL));
        String apiUrl = testCase.getString(CaseKeys.CASE_API);
        if (StringUtil.isNotBlank(apiUrl)) {
            url.append(apiUrl);
        }
        Object encode = testCase.get(CaseKeys.CONFIG_REQUEST_ENCODE) != null ?
                testCase.get(CaseKeys.CONFIG_REQUEST_ENCODE) : request.get(CaseKeys.CONFIG_REQUEST_ENCODE);
        Object cookies = request.get(CaseKeys.CONFIG_REQUEST_COOKIE);
        builder.cookies(cookies).headers(headers).url(url.toString()).encode(encode).title(testCase.getString(CaseKeys.CASE_TITLE));
    }

    public Vector<CaseResult> caseResults() {
        return caseResults;
    }

    void addCaseResult(CaseResult result) {
        this.caseResults.add(result);
    }
}
