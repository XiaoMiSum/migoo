package xyz.migoo.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.Args;
import xyz.migoo.simplehttp.*;

import java.net.URI;
import java.util.ArrayList;

/**
 * @author xiaomi
 * @date 2019/9/16 20:03
 */
public class MiGooRequest extends Request {

    private String method;

    public static MiGooRequest method(String method) {
        return new MiGooRequest(method);
    }

    private MiGooRequest(String method){
        super();
        this.method = method.toUpperCase();
    }

    private MiGooRequest(HttpRequest request) {
        super(request);
    }


    public MiGooRequest uri(String uri){
        switch (method){
            case HttpPost.METHOD_NAME:
            case HttpPut.METHOD_NAME:
                request(new EntityEnclosingHttpRequest(method, URI.create(uri)));
                break;
            default:
                request(new HttpRequest(method, URI.create(uri)));
        }
        return this;
    }

    public MiGooRequest body(JSONObject body) {
        if (body == null || body.isEmpty()){
            return this;
        }
        return (MiGooRequest) bodyJson(body.toString());
    }

    public MiGooRequest body(JSONArray body) {
        if (body == null || body.isEmpty()){
            return this;
        }
        return (MiGooRequest) bodyJson(body.toString());
    }

    public MiGooRequest data(JSONObject data) {
        if (data != null && !data.isEmpty()){
            Form form = Form.form();
            data.forEach((k, v) -> {
                if (v == null){
                    form.add(k, null);
                }else {
                    form.add(k, String.valueOf(v));
                }
            });
            data(form);
        }
        return this;
    }

    public MiGooRequest query(JSONObject query) {
        if (query != null && !query.isEmpty()){
            Form form = Form.form();
            query.forEach((k, v) -> {
                if (v == null){
                    form.add(k, null);
                }else {
                    form.add(k, String.valueOf(v));
                }
            });
            query(form);
        }
        return this;
    }

    public MiGooRequest cookies(JSONObject cookie) {
        Args.notNull(cookie, "cookie");
        CookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie clientCookie = new BasicClientCookie(cookie.getString("name"), cookie.getString("value"));
        clientCookie.setPath(cookie.getString("path"));
        clientCookie.setDomain(cookie.getString("domain"));
        return (MiGooRequest) super.cookies(cookieStore);
    }

    public MiGooRequest headers(JSONObject headers){
        if (headers != null && !headers.isEmpty()){
            headers.forEach((k, v) -> addHeader(k, String.valueOf(v)));
        }
        return this;
    }

    public JSONObject jsonHeaders(){
        Header[] headers = headers();
        if (headers != null){
            JSONObject jsonHeaders = new JSONObject();
            for (Header header : headers){
                jsonHeaders.put(header.getName(), header.getValue());
            }
            return jsonHeaders;
        }
        return null;
    }

    public JSONArray cookies(){
        return context() != null && context().getCookieStore() != null ?
                new JSONArray(new ArrayList<>(context().getCookieStore().getCookies())) : null;
    }

    @Override
    public String uri(){
        String uri = super.uri();
        return uri.substring(0, !uri.contains("?") ? uri.length() : uri.indexOf("?"));
    }
}
