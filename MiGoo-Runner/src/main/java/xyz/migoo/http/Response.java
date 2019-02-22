package xyz.migoo.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import xyz.migoo.utils.StringUtil;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
public class Response {

    private static final int SUCCESS = 200;
    private static final int ERROR = 0;

    private int statusCode;
    private Header[] headers;
    private String body;
    private long startTime;
    private long endTime;
    private Locale locale;
    private Request request;
    private String error;

    protected void statusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    protected void headers(Header[] headers) {
        this.headers = headers;
    }

    public void body(String body) {
        this.body = body;
    }

    protected void startTime(long startTime){
        this.startTime = startTime;
    }

    protected void endTime(long endTime){
        this.endTime = endTime;
    }

    protected void locale(Locale locale){
        this.locale = locale;
    }

    protected void request(Request request) {
        this.request = request;
    }

    protected void error(String error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return this.statusCode == SUCCESS;
    }

    public boolean isError() {
        return this.statusCode == ERROR;
    }

    public int statusCode() {
        return statusCode;
    }

    public Header[] headers() {
        return headers;
    }

    public String body() {
        return body;
    }

    public JSON json() {
        try {
            return JSON.parseObject(body);
        }catch (JSONException e){
            try {
                return JSON.parseArray(body);
            }catch (Exception ex){
                return null;
            }
        }
    }

    public long duration() {
        return endTime - startTime;
    }

    public Locale locale(){
        return locale;
    }

    public Request request() {
        return request;
    }

    public String error() {
        return error;
    }
}
