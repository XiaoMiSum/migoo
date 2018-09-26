package xyz.migoo.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static xyz.migoo.http.Request.UTF8;


/**
 * 封装了HttpClient提供的api 简化测试脚本开发
 *
 * @author xiaomi
 */
public class Client {

    private CloseableHttpClient httpClient;
    private static Log log = new Log(Client.class);
    protected static final String HTTPS = "https";
    private Request request;

    public Client(){}

    public Client(Request request){
        this.request = request;
    }

    /**
     * 执行请求，返回 Response 对象
     *
     * @param request
     * @return Response
     */
    public Response execute(Request request) {
        if (HttpGet.METHOD_NAME.equals(request.method())) {
            return doGet(request);
        }
        if (HttpPost.METHOD_NAME.equals(request.method())) {
            return doPost(request);
        }
        if (HttpPut.METHOD_NAME.equals(request.method())) {
            return doPut(request);
        }
        if (HttpDelete.METHOD_NAME.equals(request.method())) {
            return doDelete(request);
        }
        return doGet(request);
    }

    public Response execute(){
        return execute(this.request);
    }

    /**
     * doGet ，发起一个 get 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doGet(Request request) {
        this.init(request);
        HttpRequestBase httpGet = new HttpGet(request.url());
        this.setHeader(request, httpGet);
        return this.doExecute(httpGet, request);
    }

    /**
     * doPost ，发起一个 post 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doPost(Request request) {
        this.init(request);
        HttpPost httpPost = new HttpPost(request.url());
        this.setHeader(request, httpPost);
        this.setEntity(request, httpPost);
        return this.doExecute(httpPost, request);
    }

    /**
     * doPut ，发起一个 put 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doPut(Request request) {
        this.init(request);
        HttpPut httpPut = new HttpPut(request.url());
        this.setHeader(request, httpPut);
        this.setEntity(request, httpPut);
        return this.doExecute(httpPut, request);
    }

    /**
     * doDelete ，发起一个 delete 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doDelete(Request request) {
        this.init(request);
        HttpRequestBase httpDelete = new HttpDelete(request.url());
        this.setHeader(request, httpDelete);
        return this.doExecute(httpDelete, request);
    }

    /**
     * 初始化 HTTP CLIENT
     *
     * @param request 用于判断是否 https
     */
    private void init(Request request) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (request.url().toLowerCase().startsWith(HTTPS)) {
            SSLContext sslContext = this.getSslContext(request.certificate());
            builder.setSSLContext(sslContext);
        }
        if (request.proxy() != null) {
            builder.setProxy(request.proxy());
        }
        if (request.cookie() != null && !request.cookie().isEmpty()){
            JSONObject jsonCookie = request.cookie();
            CookieStore cookieStore = new BasicCookieStore();
            String name = null, value = null;
            for (String key : jsonCookie.keySet()){
                if (StringUtil.containsIgnoreCase(key, "SESSID")){
                    name = key; value = jsonCookie.getString(key);
                }
            }
            BasicClientCookie cookie = new BasicClientCookie(name, value);
            cookie.setDomain(jsonCookie.getString("domain"));
            cookie.setPath(jsonCookie.getString("path"));
            cookieStore.addCookie(cookie);
            builder.setDefaultCookieStore(cookieStore);
        }
        httpClient = builder.setConnectionTimeToLive(request.timeOut(), TimeUnit.SECONDS).build();
    }

    /**
     * 设置 请求 entity
     *
     * @param request     请求实体 参数信息
     * @param httpMethods 请求方法
     */
    private void setEntity(Request request, HttpEntityEnclosingRequestBase httpMethods) {
        JSONObject body = request.body();
        if (body.isEmpty()){
            return;
        }
        StringEntity entity;
        if (request.isFrom()){
            List<NameValuePair> pairList = new ArrayList<>(body.size());
            for (String key : body.keySet()) {
                pairList.add(new BasicNameValuePair(key, body.getString(key)));
            }
            entity = new UrlEncodedFormEntity(pairList, Charset.forName(UTF8));
        }else {
            entity = new StringEntity(body.toJSONString(), Charset.forName(UTF8));
        }
        entity.setContentEncoding(UTF8);
        if (request.contentType() != null) {
            entity.setContentType(request.contentType());
        }
        httpMethods.setEntity(entity);
    }

    /**
     * 设置 请求 Header
     *
     * @param request     请求参数信息
     * @param httpMethods 请求方法
     */
    private void setHeader(Request request, HttpRequestBase httpMethods) {
        if (request.headers() == null || request.headers().size() == 0) {
            return;
        }
        for (Header header : request.headers()) {
            httpMethods.setHeader(header);
        }
    }

    /**
     * 发起一个请求
     *
     * @param httpMethods 请求方法
     * @return 响应信息 包含响应头
     */
    private Response doExecute(HttpRequestBase httpMethods, Request request) {
        Response response = new Response();
        response.request(request);
        try {
            response.startTime(System.currentTimeMillis());
            CloseableHttpResponse httpResponse = httpClient.execute(httpMethods);
            response.endTime(System.currentTimeMillis());
            response.locale(httpResponse.getLocale());
            response.headers(httpResponse.getAllHeaders());
            response.body(EntityUtils.toString(httpResponse.getEntity(), UTF8).trim().toLowerCase());
            response.statusCode(httpResponse.getStatusLine().getStatusCode());
            httpResponse.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.error(e.getMessage());
            response.headers(null);
            response.body(null);
            response.statusCode(0);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
            }
        }
        return response;
    }

    /**
     * @param crt https 的证书
     * @return 返回一个 安全的 HTTP CLIENT
     */
    private SSLContext getSslContext(File crt) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509Trust(crt)}, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            log.error("set ssl exception", e);
        }
        return null;
    }
}
