package xyz.migoo.framework.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;

import java.util.*;

/**
 * @author xiaomi
 * @date 2018/10/10 20:35
 */
public class Response {

    private static final int SUCCESS = 200;
    private static final int ERROR = 0;

    private int statusCode;
    private Header[] headers;
    private String body;
    private JSON json;
    private long startTime;
    private long endTime;
    private Locale locale;
    private Request request;
    private String error;
    private CookieStore cookieStore;
    private HttpClientContext context;

    void statusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    void headers(Header[] headers) {
        this.headers = headers;
    }

    void cookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    void body(String body) {
        this.body = body;
    }

    void startTime(long startTime){
        this.startTime = startTime;
    }

    void endTime(long endTime){
        this.endTime = endTime;
    }

    void locale(Locale locale){
        this.locale = locale;
    }

    void request(Request request) {
        this.request = request;
    }

    void error(String error) {
        this.error = error;
    }

    void setContext(HttpClientContext context) {
        this.context = context;
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

    public JSONArray cookies(){
        if (cookieStore == null){
            return null;
        }
        JSONArray cookies = new JSONArray();
        for (Cookie cookie : cookieStore.getCookies()){
            JSONObject c = new JSONObject(4);
            c.put("name", cookie.getName());
            c.put("value", cookie.getValue());
            c.put("domain", cookie.getDomain());
            c.put("path", cookie.getPath());
            cookies.add(c);
        }
        return cookies;
    }

    public String body() {
        return body;
    }

    public JSON json() {
        if (json == null){
            try {
                json = JSON.parseObject(body);
            }catch (JSONException e){
            }
        }
        return json;
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

    public HttpClientContext getContext() {
        return context;
    }
}