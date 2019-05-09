package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.assertions.Validator;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.AssertionException;
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
public class Task {

    private Client client;
    private Request request;
    private Request.Builder builder;
    private static Log log = new Log(Task.class);

    public Task(Client client, Request.Builder builder){
        this.client = client;
        this.builder = builder;
    }

    public synchronized void run(JSONObject jsonCase, TestCase testCase) throws AssertionException, Exception {
        try {
            this.buildRequest(jsonCase);
            testCase.request(request);
            Response response = client.execute(request);
            testCase.response(response);
            this.addLog(request.title(), response);
            this.assertThat(jsonCase.get(CaseKeys.VALIDATE), response, testCase);
        } catch (AssertionException e) {
            log.error(e.getMessage(), e);
            throw new AssertionException(e.getMessage().replaceAll("\n", "</br>"));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new Exception(StringUtil.getStackTrace(e));
        }
    }

    private void buildRequest(JSONObject testCase) throws InvokeException {
        JSONObject variables = testCase.getJSONObject(CaseKeys.CASE_VARIABLES);
        BindVariable.evalVariable(variables, testCase.getJSONObject(CaseKeys.CASE_QUERY));
        BindVariable.evalVariable(variables, testCase.getJSONObject(CaseKeys.CASE_DATA));
        BindVariable.evalVariable(variables, testCase.getJSONObject(CaseKeys.CASE_BODY));
        this.request = builder.query(testCase.getJSONObject(CaseKeys.CASE_QUERY))
                .body(testCase.getJSONObject(CaseKeys.CASE_BODY))
                .data(testCase.getJSONObject(CaseKeys.CASE_DATA))
                .title(testCase.getString(CaseKeys.CASE_TITLE))
                .build();
    }

    private void assertThat(Object validate, Response response, TestCase testCase){
        if (!(validate instanceof JSON)){
            validate = JSON.parse(validate.toString());
        }
        testCase.validate((JSONArray) validate);
        Validator.validation(response, (JSON) validate);
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
