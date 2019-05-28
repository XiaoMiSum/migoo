package xyz.migoo.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import xyz.migoo.exception.RequestException;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static xyz.migoo.http.Request.UTF8;

/**
 * 封装了HttpClient提供的api 简化测试脚本开发
 *
 * @author xiaomi
 * @date 2018/10/10 20:35
 */
public class Client {

    private static final PoolingHttpClientConnectionManager POOL = new PoolingHttpClientConnectionManager();
    private static final Log log = new Log(Client.class);
    private static CloseableHttpClient httpClient;

    private Client(){
    }

    /**
     * 执行请求，返回 Response 对象
     *
     * @param request 请求信息
     * @return Response
     */
    public Response execute(Request request) {
        switch (request.method()){
/*            case HttpGet.METHOD_NAME:
                return this.doGet(request);*/
            case HttpPost.METHOD_NAME:
                return this.doPost(request);
            case HttpPut.METHOD_NAME:
                return this.doPut(request);
            case HttpDelete.METHOD_NAME:
                return this.doDelete(request);
            default:
                return this.doGet(request);
        }
    }

    /**
     * doGet ，发起一个 get 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doGet(Request request) {
        HttpRequestBase httpGet = new HttpGet(this.setParameters(request));
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
        HttpPost httpPost = new HttpPost(this.setParameters(request));
        this.setEntity(request, httpPost);
        this.setHeader(request, httpPost);
        return this.doExecute(httpPost, request);
    }

    /**
     * doPut ，发起一个 put 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doPut(Request request) {
        HttpPut httpPut = new HttpPut(this.setParameters(request));
        this.setEntity(request, httpPut);
        this.setHeader(request, httpPut);
        return this.doExecute(httpPut, request);
    }

    /**
     * doDelete ，发起一个 delete 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doDelete(Request request) {
        HttpDelete httpDelete = new HttpDelete(this.setParameters(request));
        this.setEntity(request, httpDelete);
        this.setHeader(request, httpDelete);
        return this.doExecute(httpDelete, request);
    }

    private URI setParameters(Request request){
        try {
            URIBuilder uri = new URIBuilder(request.url());
            if (request.query() != null){
                request.query().forEach((k, value) -> uri.addParameter(k, StringUtil.valueOf(value)));
            }
            return uri.build();
        } catch (URISyntaxException e) {
            log.error("setting parameters exception", e);
        }
        return null;
    }

    /**
     * 设置 请求 entity
     *
     * @param request     请求实体 参数信息
     * @param httpMethods 请求方法
     */
    private void setEntity(Request request, HttpEntityEnclosingRequestBase httpMethods) {
        if (request.data() == null && request.body() == null){
            return;
        }
        StringEntity entity = null;
        if (request.body() != null){
            JSONObject body = request.body();
            if (request.encode()){
                for (String key: request.body().keySet()){
                    String str = StringUtil.valueOf(body.get(key));
                    try {
                        body.put(key, URLEncoder.encode(str, UTF8));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            entity = new StringEntity(body.toJSONString(), Charset.forName(UTF8));
        }
        if (request.data() != null){
            if (HttpDelete.METHOD_NAME.equals(request.method())){
                return;
            }
            List<NameValuePair> pairList = new ArrayList<>(request.data().size());
            request.data().forEach((key, value) -> pairList.add(new BasicNameValuePair(key, StringUtil.valueOf(value))));
            entity = new UrlEncodedFormEntity(pairList, Charset.forName(UTF8));
        }
        if (entity != null){
            entity.setContentEncoding(UTF8);
            httpMethods.setEntity(entity);
        }
    }

    /**
     * 设置 请求 Header
     *
     * @param request     请求参数信息
     * @param httpMethods 请求方法
     */
    private void setHeader(Request request, HttpRequestBase httpMethods) {
        if (request.headers() == null || request.headers().isEmpty()) {
            return;
        }
        request.headers().forEach(httpMethods::addHeader);
    }

    /**
     * 设置 请求上下文（Cookie 主要用于设置 Cookie）
     *
     * @param request     请求参数信息
     * @return HttpClientContext 请求上下文对象
     */
    private HttpClientContext setCookieStore(Request request) {
        HttpClientContext context = HttpClientContext.create();
        if (request.cookies() == null || request.cookies().isEmpty()) {
            return context;
        }
        CookieStore cookieStore = new BasicCookieStore();
        for (int i = 0; i < request.cookies().size(); i++){
            JSONObject json = request.cookies().getJSONObject(i);
            BasicClientCookie cookie = new BasicClientCookie(json.getString("name"), json.getString("value"));
            cookie.setDomain(json.getString("domain"));
            cookie.setPath(json.getString("path"));
            cookieStore.addCookie(cookie);
        }
        context.setCookieStore(cookieStore);
        return context;
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
        response.startTime(System.currentTimeMillis());
        HttpClientContext context = this.setCookieStore(request);
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpMethods, context)){
            response.setContext(context);
            response.cookieStore(context.getCookieStore());
            response.endTime(System.currentTimeMillis());
            response.locale(httpResponse.getLocale());
            response.headers(httpResponse.getAllHeaders());
            response.body(EntityUtils.toString(httpResponse.getEntity(), UTF8).trim());
            response.statusCode(httpResponse.getStatusLine().getStatusCode());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.error(e.getMessage());
            response.headers(null);
            response.body(null);
            response.statusCode(0);
        }
        return response;
    }

    /**
     * @param crt https 的证书
     * @return 返回一个 安全的 HTTP CLIENT
     */
    private static SSLContext getSslContext(File crt) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509Trust(crt)}, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            log.error("set ssl exception", e);
        }
        return null;
    }

    public static class Config{

        private int maxTotal = 20;

        private int maxPerRoute = 2;

        private int timeout = 20;

        private boolean https = false;

        private boolean redirects = true;

        private HttpHost proxy;

        private File certificate;

        private JSONArray cookies;

        private static Client client;

        public Config() {
        }

        public Config maxTotal(int maxTotal){
            if (maxTotal > this.maxTotal) {
                this.maxTotal = maxTotal;
            }
            return this;
        }

        public Config maxPerRoute(int maxPerRoute){
            if (maxPerRoute > this.maxPerRoute) {
                this.maxPerRoute = maxPerRoute;
            }
            return this;
        }

        public Config timeout(int timeout) {
            if (timeout > this.timeout) {
                this.timeout = timeout;
            }
            return this;
        }

        public Config https(Object value){
            Boolean boo = TypeUtil.booleanOf(value);
            if (boo != null){
                this.https =  boo;
            }
            return this;
        }

        public Config redirects(Object value){
            Boolean boo = TypeUtil.booleanOf(value);
            if (boo != null){
                this.redirects =  boo;
            }
            return this;
        }

        public Config certificate(String certificate) {
            if (StringUtil.isNotBlank(certificate)) {
                this.certificate = new File(certificate);
            }
            return this;
        }

        public Config proxy(String proxy) {
            HttpHost httpHost = null;
            try {
                JSONObject json = JSONObject.parseObject(proxy);
                httpHost = new HttpHost(json.getString("host"), json.getIntValue("port"));
            } catch (Exception e) {
                String regex = ":";
                if (StringUtil.containsIgnoreCase(proxy, regex)) {
                    String[] host = proxy.split(regex);
                    httpHost = new HttpHost(host[0], Integer.valueOf(host[1]));
                }
            }
            this.proxy = httpHost;
            return this;
        }

        public Config cookies(Object cookies){
            if (cookies instanceof JSONObject){
                this.cookies = new JSONArray(1);
                this.cookies.add(cookies);
                return this;
            }
            if (cookies instanceof JSONArray){
                this.cookies = (JSONArray)cookies;
                return this;
            }
            if (cookies instanceof String){
                try {
                    this.cookies = JSON.parseArray((String) cookies);
                }catch (Exception e){
                    log.debug("cookies parse exception, skip");
                }
            }
            return this;
        }

        public Client build(){
            if (client == null){
                synchronized (Client.class){
                    if (client == null){
                        client = new Client();
                    }
                }
            }
            POOL.setMaxTotal(maxTotal);
            POOL.setDefaultMaxPerRoute(maxPerRoute);
            RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 2000)
                    .setRedirectsEnabled(redirects).build();
            HttpClientBuilder builder = HttpClientBuilder.create();
            if (https) {
                if (certificate != null && certificate.isDirectory()) {
                    throw new RequestException("certificate can not be directory . certificate path : " + certificate);
                }
                builder.setSSLContext(getSslContext(certificate));
            }
            if (redirects){
                builder.setRedirectStrategy(new LaxRedirectStrategy());
            }
            if (proxy != null) {
                builder.setProxy(proxy);
            }
            if (cookies != null){
                CookieStore cookieStore = new BasicCookieStore();
                for (int i = 0; i < cookies.size(); i++){
                    JSONObject json = cookies.getJSONObject(i);
                    BasicClientCookie cookie = new BasicClientCookie(json.getString("name"), json.getString("value"));
                    cookie.setDomain(json.getString("domain"));
                    cookie.setPath(json.getString("path"));
                    cookieStore.addCookie(cookie);
                }
                builder.setDefaultCookieStore(cookieStore);
            }
            builder.setConnectionManager(POOL).setDefaultRequestConfig(config);
            httpClient = builder.build();
            return client;
        }
    }
}