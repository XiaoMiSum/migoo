package xyz.migoo.http;

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
import xyz.migoo.utils.Log;

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
    private static final String HTTPS = "https";
    private static Client client;
    private static CloseableHttpClient httpClient;

    /**
     * 初始化 Http Client
     * @param request
     * @return
     */
    public static Client create(Request request){
        if (client == null && httpClient == null){
            synchronized (Client.class){
                if (client == null){
                    client = new Client();
                }
                if (httpClient == null) {
                    RequestConfig config = RequestConfig.custom()
                            .setConnectTimeout(request.timeOut() * 1000)
                            .setConnectionRequestTimeout(request.timeOut() * 1000)
                            .build();
                    HttpClientBuilder builder = HttpClientBuilder.create();
                    if (request.url().toLowerCase().startsWith(HTTPS)) {
                        builder.setSSLContext(getSslContext(request.certificate()));
                    }
                    if (request.proxy() != null) {
                        builder.setProxy(request.proxy());
                    }
                    builder.setConnectionManager(POOLING).setDefaultRequestConfig(config);
                    httpClient = builder.build();
                }
            }
        }
        return client;
    }

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
        if (request.restful()){
            entity = new StringEntity(request.body().toJSONString(), Charset.forName(UTF8));
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
}
