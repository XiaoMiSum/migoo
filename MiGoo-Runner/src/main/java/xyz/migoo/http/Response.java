package xyz.migoo.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import xyz.migoo.utils.StringUtil;

import java.util.Locale;

/**
 * @author xiaomi
 */
public class Response {

    private static final int SUCCESS = 200;
    private static final int ERROR = 0;
    private static final int NOT_FOUND = 404;

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

    protected void body(String body) {
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

    public boolean isNotFound() {
        return this.statusCode == NOT_FOUND;
    }

    public int statusCode() {
        return statusCode;
    }

    public Header[] headers() {
        return headers;
    }

    public JSONObject cookie(){
        if (headers == null || headers.length == 0){
            return null;
        }
        String[] tmp = null;
        for (Header header : headers){
            if (StringUtil.containsIgnoreCase(header.getName(), "cookie")){
                tmp = header.getValue().split(";");
            }
        }
        JSONObject cookie = null;
        if (tmp != null && tmp.length != 0){
            cookie = new JSONObject(tmp.length);
            for (String s : tmp){
                String[] ss = s.split("=");
                cookie.put(ss[0], ss[1]);
            }
        }
        return cookie;
    }

    public String body() {
        return body;
    }

    public JSONObject json() {
        try {
            return JSONObject.parseObject(body);
        }catch (Exception e){
            return null;
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
