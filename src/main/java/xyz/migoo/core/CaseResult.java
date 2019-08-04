package xyz.migoo.core;

import com.alibaba.fastjson.JSONArray;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;

/**
 * @author xiaomi
 * @date 2019-08-04 22:19
 */
public class CaseResult {

    static final String SUCCESS = "success";
    static final String ERROR = "error";
    static final String FAILURE = "failure";
    static final String SKIPPED = "skipped";

    private String name;

    private String status;

    private Throwable throwable;

    private JSONArray validates;

    private Request request;

    private Response response;

    public String name() {
        return name;
    }

    public CaseResult name(String name) {
        this.name = name;
        return this;
    }

    String status() {
        return status;
    }

    CaseResult success() {
        this.status = SUCCESS;
        return this;
    }

    public CaseResult error() {
        this.status = ERROR;
        return this;
    }

    CaseResult failure() {
        this.status = FAILURE;
        return this;
    }

    CaseResult skipped() {
        this.status = SKIPPED;
        return this;
    }

    public Throwable throwable() {
        return throwable;
    }

    public CaseResult throwable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    JSONArray validates() {
        return validates;
    }

    CaseResult validates(JSONArray validates) {
        this.validates = validates;
        return this;
    }

    public Request request() {
        return request;
    }

    public CaseResult request(Request request) {
        this.request = request;
        return this;
    }

    public Response response() {
        return response;
    }

    public CaseResult response(Response response) {
        this.response = response;
        return this;
    }
}
