/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package protocol.xyz.migoo.http.sampler;

import com.alibaba.fastjson.JSONArray;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.MiGooProperty;
import org.apache.hc.core5.http.Header;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.net.HttpURLConnection;
import java.util.Arrays;

/**
 * @author xiaomi
 */
public class HTTPSampleResult extends SampleResult {

    private static final String OK_CODE = Integer.toString(HttpURLConnection.HTTP_OK);
    private static final String OK_MSG = "OK";
    private static final long serialVersionUID = -101758235270552554L;

    private String method;

    private String queryString = "";

    private String cookies = "";

    private String requestHeaders = "";

    private String responseHeaders = "";

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

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }


    public void setRequestData(Request request) {
        setUrl(request.uriNotContainsParam());
        setMethod(request.method());
        setRequestHeaders(request.headers().length == 0 ? requestHeaders : Arrays.toString(request.headers()));
        setQueryString(request.query());
        setSamplerData(request.body());
    }

    public void setResponseData(Response response) {
        setResponseData(response.text());
        setResponseCode(String.valueOf(response.statusCode()));
        JSONArray headers = new JSONArray(response.headers().length);
        for (Header h : response.headers()) {
            headers.add(new MiGooProperty(h.getName(), h.getValue()));
        }
        setResponseHeaders(headers.toJSONString());
        if (isResponseCodeOK()) {
            setResponseMessageOK();
        }
        setCookies(response.cookies() != null ? JSONArray.toJSONString(response.cookies()) : cookies);
    }

    @Override
    public String getUrl() {
        if (super.getUrl() != null && !super.getUrl().isEmpty()) {
            return super.getUrl() + " " + responseCode + " " + responseMessage;
        }
        return super.getUrl();
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
}