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

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.samplers.SampleResult;
import org.apache.http.Header;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.util.Arrays;

public class HTTPSampleResult extends SampleResult {

    private String method;

    private String queryString = "";

    private String cookies;

    public HTTPSampleResult(String title) {
        super(title, 1);
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

    public void setRequestData(Request request){
        setUrl(request.uriNotContainsParam());
        setMethod(request.method());
        setRequestHeaders(Arrays.toString(request.headers()));
        setQueryString(request.query());
        setSamplerData(request.data().isEmpty() ? request.body() : request.data());
    }

    public void setResponseData(Response response){
        setResponseData(response.text());
        setResponseCode(String.valueOf(response.statusCode()));
        JSONObject header = new JSONObject(response.headers().length);
        for (Header h : response.headers()) {
            header.put(h.getName(), h.getValue());
        }
        setResponseHeaders(header.toJSONString());
        if (isResponseCodeOK()) {
            setResponseMessageOK();
        }
    }
}