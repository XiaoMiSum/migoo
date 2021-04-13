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

package protocol.xyz.migoo.http;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.MiGooProperty;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import protocol.xyz.migoo.http.config.HttpDefaults;
import protocol.xyz.migoo.http.sampler.HTTPHCImpl;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import protocol.xyz.migoo.http.util.HTTPConstantsInterface;
import xyz.migoo.simplehttp.Form;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

public abstract class AbstractHttpTestElement extends AbstractTestElement implements HTTPConstantsInterface {

    private static final String URL_FORMAT = "%s://%s%s";

    protected final String name = this.getClass().getSimpleName().toLowerCase();

    public void testStarted() {
        super.convertVariable();
        HttpDefaults other = (HttpDefaults) getVariables().get(HTTP_DEFAULT);
        if (other != null) {
            setProperty(BASE_PATH, other.getPropertyAsString(BASE_PATH));
            setProperty(PROTOCOL, other.getPropertyAsString(PROTOCOL));
            setProperty(REQUEST_METHOD, other.getPropertyAsString(REQUEST_METHOD));
            setProperty(HOST, other.getPropertyAsString(HOST));
            setProperty(PORT, other.getPropertyAsInt(PORT));
            setProperty(API_PATH, other.getPropertyAsString(API_PATH));
            setProperty(COOKIE, other.get(COOKIE));
            this.setHeader(other.getProperty());
        }
        getVariables().put("migoo.protocol.http.request.body", get(BODY));
        getVariables().put("migoo.protocol.http.request.data", get(DATA));
        getVariables().put("migoo.protocol.http.request.query", get(QUERY));
    }

    public void testEnded() {
        getVariables().remove("migoo.protocol.http.request.body");
        getVariables().remove("migoo.protocol.http.request.data");
        getVariables().remove("migoo.protocol.http.request.query");
    }

    private void setHeader(MiGooProperty other) {
        JSONObject otherHeaders = other.getJSONObject(HEADERS);
        if (otherHeaders != null && otherHeaders.size() > 0) {
            MiGooProperty thisHeaders = getPropertyAsMGooProperty(HEADERS);
            if (thisHeaders == null) {
                thisHeaders = new MiGooProperty();
                otherHeaders.forEach(thisHeaders::put);
            } else {
                MiGooProperty headers = new MiGooProperty();
                headers.putAll(otherHeaders);
                headers.putAll(thisHeaders);
                setProperty(HEADERS, headers);
            }
        }
    }

    protected SampleResult execute(SampleResult sample) throws Exception {
        HTTPSampleResult result = (HTTPSampleResult) sample;
        result.setTestClass(this.getClass());
        try {
            Request request = getRequest();
            result.setRequestData(request);
            result.sampleStart();
            Response response = request.execute();
            result.setResponseData(response);
            result.setSuccessful(true);
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    private Request getRequest() {
        Request request = new HTTPHCImpl(getPropertyAsString(REQUEST_METHOD), getUrl());
        if (get(BODY) != null) {
            request.bodyJson(getPropertyAsString(BODY));
        } else if (get(DATA) != null) {
            request.data(getForm(DATA));
        }
        if (get(QUERY) != null) {
            request.query(getForm(QUERY));
        }
        if (get(HEADERS) != null) {
            JSONObject headers = getPropertyAsJSONObject(HEADERS);
            headers.forEach((name, value) -> request.addHeader(name, value == null ? "" : value.toString()));
        }
        if (get(COOKIE) != null) {
            JSONObject cookie = getPropertyAsJSONObject(COOKIE);
            if (cookie.size() > 0) {
                CookieStore cookieStore = new BasicCookieStore();
                BasicClientCookie clientCookie = new BasicClientCookie(cookie.getString(COOKIE_NAME), cookie.getString(COOKIE_VALUE));
                clientCookie.setPath(cookie.getString(COOKIE_PATH));
                clientCookie.setDomain(cookie.getString(COOKIE_DOMAIN));
                cookieStore.addCookie(clientCookie);
                request.cookies(cookieStore);
            }
        }
        return request;
    }

    private String getUrl() {
        String path = getPropertyAsString(BASE_PATH);
        if (path == null || path.isEmpty()) {
            path = get(PORT) == null ? String.format(URL_FORMAT, get(PROTOCOL), get(HOST), "") :
                    String.format(URL_FORMAT, get(PROTOCOL), get(HOST), ":" + get(PORT));
        } else if (path.endsWith(SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        String api = getPropertyAsString(API_PATH);
        api = api == null ? "" : !api.startsWith(SEPARATOR) ? SEPARATOR + api : api;
        return path + api;
    }

    private Form getForm(String key) {
        JSONObject data = getPropertyAsJSONObject(key);
        Form form = Form.form();
        data.forEach((name, value) -> form.add(name, value == null ? "" : value.toString()));
        return form;
    }

}