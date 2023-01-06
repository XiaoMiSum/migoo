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

import com.alibaba.fastjson2.JSON;
import core.xyz.migoo.sampler.SampleResult;
import org.apache.hc.core5.http.Header;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 */
public class HTTPSampleResult extends SampleResult {

    private static final String OK_CODE = Integer.toString(HttpURLConnection.HTTP_OK);
    private static final String OK_MSG = "OK";

    private String method;

    private String queryString = "";

    private String cookies = "";

    private List<Map<String, String>> requestHeaders;

    private List<Map<String, String>> responseHeaders;

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

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public List<Map<String, String>> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(List<Map<String, String>> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public List<Map<String, String>> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(List<Map<String, String>> responseHeaders) {
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
        setCookies(response.cookies() != null ? JSON.toJSONString(response.cookies()) : cookies);
    }

    private List<Map<String, String>> convertHeaders(Header[] headers) {
        if (headers.length == 0) {
            return null;
        }
        List<Map<String, String>> ls = new ArrayList<>(headers.length);
        for (Header h : headers) {
            Map<String, String> header = new HashMap<>(2);
            header.put(h.getName(), h.getValue());
            ls.add(header);
        }
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
            setCookies(getCookies());
            setRequestHeaders(hResult.getRequestHeaders());
            setResponseHeaders(hResult.getResponseHeaders());
            setResponseCode(hResult.getResponseCode());
            if (hResult.isResponseCodeOK()) {
                setResponseMessageOK();
            }
        }
    }
}