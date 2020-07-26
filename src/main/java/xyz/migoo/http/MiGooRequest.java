/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package xyz.migoo.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
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

    private String protocol;

    private String host;

    private Integer port;

    private String api = "";

    public static MiGooRequest create(String method) {
        return new MiGooRequest(method);
    }

    private MiGooRequest(String method){
        super();
        this.method = StringUtils.isNotEmpty(method) ? method.toUpperCase() : HttpGet.METHOD_NAME;
    }

    private MiGooRequest(HttpRequest request) {
        super(request);
    }

    public MiGooRequest uri(String url){
        switch (method){
            case HttpPost.METHOD_NAME:
            case HttpPut.METHOD_NAME:
                request(new EntityEnclosingHttpRequest(method, URI.create(url)));
                break;
            default:
                request(new HttpRequest(method, URI.create(url)));
        }
        return this;
    }

    public MiGooRequest build(){
        return uri(String.format( "%s://%s:%s%s", protocol, host, port, api));
    }

    public MiGooRequest(){

    }

    public MiGooRequest protocol(String protocol){
        this.protocol = StringUtils.isBlank(protocol) ? "http" : protocol;
        return this;
    }

    public MiGooRequest host(String host){
        this.host = StringUtils.isBlank(host) ?  "127.0.0.1" : host;
        return this;
    }

    public MiGooRequest port(Integer port){
        this.port = port == null || port <= 0 ? 80 : port;
        return this;
    }

    public MiGooRequest api(String api){
        this.api = StringUtils.isBlank(api) ?  "" : api;
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
            data.forEach((k, v) -> form.add(k, v == null ? "" : String.valueOf(v)));
            data(form);
        }
        return this;
    }

    public MiGooRequest query(JSONObject query) {
        if (query != null && !query.isEmpty()){
            Form form = Form.form();
            query.forEach((k, v) -> form.add(k, v == null ? "" : String.valueOf(v)));
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
