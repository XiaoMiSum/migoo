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
import xyz.migoo.utils.Hook;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.Variable;

/**
 * @author xiaomi
 * @date 2018/7/24 20:34
 */
public class Task {

    private Client client;
    private Request request;
    private Response response;
    private Request.Builder builder;
    private Object validate;
    private static Log log = new Log(Task.class);

    Task(Client client, Request.Builder builder){
        this.client = client;
        this.builder = builder;
    }

    synchronized void run(JSONObject testCase) throws AssertionException, Exception {
        try {
            this.buildRequest(testCase);
            Object before = testCase.get(CaseKeys.CASE_BEFORE);
            if (before instanceof String){
                before = JSON.parseArray(before.toString());
            }
            Hook.hook((JSONArray) before);
            response = client.execute(request);
            this.addLog(request.title(), response);
            Object after = testCase.get(CaseKeys.CASE_AFTER);
            if (after instanceof String){
                after = JSON.parseArray(after.toString());
            }
            Hook.hook((JSONArray) after);
            validate = testCase.get(CaseKeys.VALIDATE);
            if (!(validate instanceof JSON)){
                validate = JSON.parse(validate.toString());
            }
            Validator.validation(response, (JSON) validate);
        } catch (AssertionException e) {
            log.error(e.getMessage(), e);
            throw new AssertionException(e.getMessage().replaceAll("\n", "</br>"));
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new Exception(StringUtil.getStackTrace(e));
        }
    }

    private void buildRequest(JSONObject testCase) throws InvokeException {
        testCase.put(CaseKeys.CASE_QUERY, Variable.evalVariable(testCase.getJSONObject(CaseKeys.CASE_QUERY)));
        testCase.put(CaseKeys.CASE_DATA, Variable.evalVariable(testCase.getJSONObject(CaseKeys.CASE_DATA)));
        testCase.put(CaseKeys.CASE_BODY, Variable.evalVariable(testCase.getJSONObject(CaseKeys.CASE_BODY)));
        this.request = builder.query(testCase.getJSONObject(CaseKeys.CASE_QUERY))
                .body(testCase.getJSONObject(CaseKeys.CASE_BODY))
                .data(testCase.getJSONObject(CaseKeys.CASE_DATA))
                .title(testCase.getString(CaseKeys.CASE_TITLE))
                .build();
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

    Response response(){
        return response;
    }

    Request request(){
        return request;
    }

    Object validate(){
        return validate;
    }

}
