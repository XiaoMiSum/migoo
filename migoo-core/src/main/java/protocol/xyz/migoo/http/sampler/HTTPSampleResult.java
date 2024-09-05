/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package protocol.xyz.migoo.http.sampler;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.sampler.SampleResult;
import org.apache.hc.core5.http.Header;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author xiaomi
 */
public class HTTPSampleResult extends SampleResult {

    private static final String OK_CODE = Integer.toString(HttpURLConnection.HTTP_OK);
    private static final String OK_MSG = "OK";

    private String method;

    private String queryString = "";

    private JSONArray cookies;

    private JSONArray requestHeaders;

    private JSONArray responseHeaders;

    private String responseCode = "";

    private String responseMessage = "";

    public HTTPSampleResult(String title) {
        super(title);
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONArray getCookies() {
        return cookies;
    }

    public void setCookies(JSONArray cookies) {
        this.cookies = cookies;
    }

    public JSONArray getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(JSONArray requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public JSONArray getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(JSONArray responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setRequestData(Request request) {
        setUrl(request.uri());
        setMethod(request.method());
        setRequestHeaders(convertHeaders(request.headers()));
        setQueryString(request.query());
        setSamplerData(new String(request.body(), StandardCharsets.UTF_8));
    }

    public void setResponseData(Response response) {
        setResponseData(response.text());
        setResponseCode(String.valueOf(response.statusCode()));
        setResponseHeaders(convertHeaders(response.headers()));
        if (isResponseCodeOK()) {
            setResponseMessageOK();
        }
        if (response.cookies() != null) {
            JSONArray cookies = new JSONArray();
            response.cookies().forEach(item -> cookies.add(JSONObject.of(item.getName(), item.getValue())));
            setCookies(cookies);
        }
    }

    private JSONArray convertHeaders(Header[] headers) {
        if (headers.length == 0) {
            return null;
        }
        JSONArray ls = new JSONArray(headers.length);
        Arrays.stream(headers).toList().forEach(header -> ls.add(JSONObject.of(header.getName(), header.getValue())));
        return ls;
    }

    @Override
    public String getUrl() {
        String url = super.getUrl().trim();
        if (url.endsWith(responseMessage) || url.endsWith(responseCode)) {
            return url;
        }
        if (!url.isEmpty()) {
            return url + " " + responseCode + " " + responseMessage;
        }
        return url;
    }

    public void setResponseCodeOK() {
        responseCode = OK_CODE;
    }

    public boolean isResponseCodeOK() {
        return responseCode.equals(OK_CODE);
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseMessageOK() {
        responseMessage = OK_MSG;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public void setSamplerData(SampleResult result) {
        super.setSamplerData(result);
        if (result instanceof HTTPSampleResult hResult) {
            setQueryString(hResult.getQueryString());
            setMethod(hResult.getMethod());
            setCookies(hResult.getCookies());
            setRequestHeaders(hResult.getRequestHeaders());
            setResponseHeaders(hResult.getResponseHeaders());
            setResponseCode(hResult.getResponseCode());
            setResponseMessage(hResult.getResponseMessage());
        }
    }
}