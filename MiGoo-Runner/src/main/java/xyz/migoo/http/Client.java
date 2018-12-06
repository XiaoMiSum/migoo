package xyz.migoo.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import xyz.migoo.exception.RequestException;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static xyz.migoo.http.Request.UTF8;


/**
 * 封装了HttpClient提供的api 简化测试脚本开发
 *
 * @author xiaomi
 */
public class Client {

    private static final PoolingHttpClientConnectionManager POOLING = new PoolingHttpClientConnectionManager();
    private static Log log = new Log(Client.class);
    private static CloseableHttpClient httpClient;

    private Client(){}

    /**
     * 执行请求，返回 Response 对象
     *
     * @param request
     * @return Response
     */
    public Response execute(Request request) {
        switch (request.method()) {
            case HttpGet.METHOD_NAME:
                return doGet(request);
            case HttpPost.METHOD_NAME:
                return doPost(request);
            case HttpPut.METHOD_NAME:
                return doPut(request);
            case HttpDelete.METHOD_NAME:
                return doDelete(request);
            default:
                return doGet(request);
        }
    }

    /**
     * doGet ，发起一个 get 请求
     *
     * @param request 请求参数信息
     * @return 返回一个包含头部信息的响应
     */
    private Response doGet(Request request) {
        HttpGet httpGet = new HttpGet(this.setParameters(request));
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
        HttpDelete httpDelete = new HttpDelete(this.setParameters(request));
        this.setHeader(request, httpDelete);
        this.setEntity(request, httpDelete);
        return this.doExecute(httpDelete, request);
    }

    private URI setParameters(Request request){
        try {
            URIBuilder builder = new URIBuilder(request.url());
            if (request.query() != null) {
                request.query().forEach((k, v) -> builder.addParameter(k, String.valueOf(v)));
            }
            return builder.build();
        } catch (URISyntaxException e) {
            log.error("setting query parameters exception", e);
        }
        return null;
    }

    /**
     * 设置 请求 entity
     * 如果是 restful风格的 api，则从 body中获取请求体
     * 否则，从 query中获取请求参数
     *
     * @param request     请求实体 参数信息
     * @param httpMethods 请求方法
     */
    private void setEntity(Request request, HttpEntityEnclosingRequestBase httpMethods) {
        if (request.body() == null && request.query() == null){
            return;
        }
        StringEntity entity;
        if (request.body() != null){
            entity = new StringEntity(request.body().toJSONString(), Charset.forName(UTF8));
        }else if (request.restful() && request.query() != null){
            entity = new StringEntity(request.query().toJSONString(), Charset.forName(UTF8));
        }else {
            if (HttpDelete.METHOD_NAME.equals(request.method())){
                return;
            }
            List<NameValuePair> pairList = new ArrayList<>(request.query().size());
            request.query().forEach((key, value) -> pairList.add(new BasicNameValuePair(key, String.valueOf(value))));
            entity = new UrlEncodedFormEntity(pairList, Charset.forName(UTF8));
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
        request.headers().forEach(header -> httpMethods.addHeader(header));
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
            System.out.println(response.body());
            httpResponse.close();
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
     * @param serverCer https 服务器证书
     * @param clientCer https 客户端证书
     * @return 证书对象
     */
    private static SSLContext sslContext(File serverCer, File clientCer) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509Trust(serverCer, clientCer)}, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            log.error("set ssl exception", e);
        }
        return null;
    }

    public static class Builder{
        private int maxTotal = 20;

        private int maxPerRoute = 2;

        private int timeout = 20;

        private HttpHost proxy;

        private File serverCer;

        private File clientCer;

        private boolean https = false;

        private static Client client;

        public Builder(){}

        public Builder maxTotal(int maxTotal){
            if (maxTotal > this.maxTotal) {
                this.maxTotal = maxTotal;
            }
            return this;
        }

        public Builder maxPerRoute(int maxPerRoute){
            if (maxPerRoute > this.maxPerRoute) {
                this.maxPerRoute = maxPerRoute;
            }
            return this;
        }

        public Builder timeout(int timeout) {
            if (timeout > this.timeout) {
                this.timeout = timeout;
            }
            return this;
        }

        public Builder https(boolean value){
            this.https =  value;
            return this;
        }

        public Builder serverCer(String certificate) {
            if (StringUtil.isNotBlank(certificate)) {
                this.serverCer = new File(certificate);
            }
            return this;
        }

        public Builder clientCer(String certificate) {
            if (StringUtil.isNotBlank(certificate)) {
                this.clientCer = new File(certificate);
            }
            return this;
        }

        public Builder proxy(String proxy) {
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

        public Client build(){
            if (client == null && httpClient == null){
                synchronized (Client.class){
                    POOLING.setMaxTotal(maxTotal);
                    POOLING.setDefaultMaxPerRoute(maxPerRoute);
                    if (client == null){
                        client = new Client();
                    }
                    if (httpClient == null){
                        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
                                .setConnectionRequestTimeout(timeout * 2000).build();
                        HttpClientBuilder builder = HttpClientBuilder.create();
                        if (https){
                            if (serverCer != null && serverCer.isDirectory()) {
                                throw new RequestException("certificate can not be directory. certificate path : " + serverCer.getPath());
                            }
                            if (clientCer != null && clientCer.isDirectory()) {
                                throw new RequestException("certificate can not be directory. certificate path : " + clientCer.getPath());
                            }
                            builder.setSSLContext(sslContext(serverCer, clientCer));
                        }
                        if (proxy != null) {
                            builder.setProxy(proxy);
                        }
                        builder.setConnectionManager(POOLING).setDefaultRequestConfig(config);
                        httpClient = builder.build();
                    }
                }
            }
            return client;
        }
    }
}
