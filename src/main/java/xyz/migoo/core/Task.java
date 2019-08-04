package xyz.migoo.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.assertions.Validator;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.AssertionFailure;
import xyz.migoo.http.Client;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.parser.BindVariable;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

/**
 * @author xiaomi
 * @date 2018/7/24 20:34
 */
class Task {

    private CaseResult caseResult = new CaseResult();
    private Client client;
    private Request request;
    private Request.Builder builder;
    private static Log log = new Log(Task.class);

    Task(Client client, Request.Builder builder){
        this.client = client;
        this.builder = builder;
    }

    synchronized void run(JSONObject jsonCase, TestCase testCase){
        try {
            caseResult.name(jsonCase.getString(CaseKeys.CASE_TITLE));
            this.buildRequest(jsonCase);
            caseResult.request(request);
            Response response = client.execute(request);
            caseResult.response(response);
            this.addLog(request.title(), response);
            this.assertThat(jsonCase, response, testCase);
            caseResult.success();
        } catch (AssertionFailure e) {
            log.error(e.getMessage(), e);
            caseResult.failure().throwable(e);
        } catch (ExecuteError | Exception e){
            log.error(e.getMessage(), e);
            caseResult.error().throwable(e);
        }
        testCase.addCaseResult(caseResult);
    }

    private void buildRequest(JSONObject testCase) throws InvokeException {
        JSONObject variables = testCase.getJSONObject(CaseKeys.CASE_VARIABLES);
        BindVariable.evalVariable(testCase.getJSONObject(CaseKeys.CASE_QUERY), variables);
        BindVariable.evalVariable(testCase.getJSONObject(CaseKeys.CASE_DATA), variables);
        BindVariable.evalVariable(testCase.getJSONObject(CaseKeys.CASE_BODY), variables);
        this.request = builder.query(testCase.getJSONObject(CaseKeys.CASE_QUERY))
                .body(testCase.getJSONObject(CaseKeys.CASE_BODY))
                .data(testCase.getJSONObject(CaseKeys.CASE_DATA))
                .build();
    }

    private void assertThat(JSONObject jsonCase, Response response, TestCase testCase) throws AssertionFailure, ExecuteError {
        Object validate = jsonCase.get(CaseKeys.VALIDATE);
        if (!(validate instanceof JSON)){
            validate = JSON.parse(validate.toString());
        }
        caseResult.validates((JSONArray) validate);
        Validator.validation(response, (JSON) validate, jsonCase.getJSONObject(CaseKeys.CASE_VARIABLES));
    }

    private void addLog(String title, Response response){
        log.info(" ------------------------------------------ TEST INFO ------------------------------------------ ");
        log.info(" test.title       : " + title);
        log.info(" request.url      : " + request.url());
        log.info(" request.method   : " + request.method());
        log.info(" request.headers  : " + request.headers());
        if (request.body() != null){
            log.info(" request.body     : " + request.body());
        }
        if (request.data() != null){
            log.info(" request.data     : " + request.data());
        }
        if (request.query() != null){
            log.info(" request.query     : " + request.query());
        }
        log.info(" response.status  : " + response.statusCode());
        log.info(" response.body    : " + response.body());
    }

}
