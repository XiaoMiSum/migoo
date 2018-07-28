package xyz.migoo.http;

import org.apache.http.Header;
import java.util.Locale;

/**
 * @author xiaomi
 */
public class Response {

    public static final int SUCCESS = 200;
    private final String COOKIE = "cookie";

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

    public int statusCode() {
        return statusCode;
    }

    public Header[] headers() {
        return headers;
    }

    public String body() {
        return body;
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
