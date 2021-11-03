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
import protocol.xyz.migoo.http.config.HttpDefaults;
import protocol.xyz.migoo.http.sampler.HTTPHCImpl;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import protocol.xyz.migoo.http.util.HTTPConstantsInterface;
import xyz.migoo.simplehttp.*;

/**
 * @author xiaomi
 */
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

    private void setHeader(MiGooProperty other) {
        JSONObject otherHeaders = other.getJSONObject(HEADERS);
        if (otherHeaders != null && otherHeaders.size() > 0) {
            MiGooProperty thisHeaders = getPropertyAsMiGooProperty(HEADERS);
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
            Request request = new HTTPHCImpl(getPropertyAsString(REQUEST_METHOD), buildUrl())
                    .headers(getPropertyAsJSONObject(HEADERS))
                    .cookie(getPropertyAsJSONObject(COOKIE))
                    .query(get(QUERY))
                    .body(get(BODY), get(DATA));
            result.setRequestData(request);
            result.sampleStart();
            Response response = request.execute();
            result.setResponseData(response);
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    private String buildUrl() {
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
}