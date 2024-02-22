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

package protocol.xyz.migoo.http;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.MiGooProperty;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.http.config.HttpDefaults;
import protocol.xyz.migoo.http.sampler.HTTPHCImpl;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import protocol.xyz.migoo.http.util.HTTPConstantsInterface;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.util.Objects;
import java.util.Optional;

/**
 * @author xiaomi
 */
public abstract class AbstractHttpTestElement extends AbstractTestElement implements HTTPConstantsInterface {

    private static final String URL_FORMAT = "%s://%s%s";

    protected final String name = this.getClass().getSimpleName().toLowerCase();

    public void testStarted() {
        super.convertVariable();
        HttpDefaults other = (HttpDefaults) getVariables().get(HTTP_DEFAULT);
        if (Objects.nonNull(other)) {
            setProperty(BASE_PATH, other.getPropertyAsString(BASE_PATH));
            setProperty(PROTOCOL, other.getPropertyAsString(PROTOCOL));
            setProperty(REQUEST_METHOD, other.getPropertyAsString(REQUEST_METHOD));
            setProperty(HOST, other.getPropertyAsString(HOST));
            setProperty(PORT, Objects.nonNull(other.get(PORT)) ? other.getPropertyAsInt(PORT) : null);
            setProperty(PATH,
                    Optional.ofNullable(other.getPropertyAsString(API)).orElse(other.getPropertyAsString(PATH)));
            setProperty(COOKIE, other.get(COOKIE));
            this.setHeader(other.getProperty());
        }
        if (getProperty().containsKey(HEADERS)) {
            getPropertyAsMiGooProperty(HEADERS).remove(VARIABLES);
        }
        getVariables().put("migoo_protocol_http_request_body", get(BODY));
        getVariables().put("migoo_protocol_http_request_data", get(DATA));
        getVariables().put("migoo_protocol_http_request_query", get(QUERY));
    }

    private void setHeader(MiGooProperty other) {
        JSONObject otherHeaders = other.getJSONObject(HEADERS);
        if (otherHeaders != null && !otherHeaders.isEmpty()) {
            MiGooProperty thisHeaders = getPropertyAsMiGooProperty(HEADERS);
            if (thisHeaders == null) {
                thisHeaders = new MiGooProperty();
                thisHeaders.putAll(otherHeaders);
                getProperty().put(HEADERS, thisHeaders);
            } else {
                MiGooProperty headers = new MiGooProperty();
                headers.putAll(otherHeaders);
                headers.putAll(thisHeaders);
                getProperty().put(HEADERS, headers);
            }
        }
    }

    protected SampleResult execute(SampleResult sample) throws Exception {
        HTTPSampleResult result = (HTTPSampleResult) sample;
        result.setTestClass(this.getClass());
        try {
            JSONObject headers = Optional.ofNullable(getPropertyAsJSONObject(HEADERS)).orElse(new JSONObject());
            Request request = new HTTPHCImpl(getPropertyAsString(REQUEST_METHOD), buildUrl())
                    .headers(headers)
                    .cookie(getPropertyAsJSONObject(COOKIE))
                    .query(get(QUERY))
                    .body(getPropertyAsByteArray(BYTES), get(BODY), get(DATA), headers.getString(CONTENT_TYPE));
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
        if (StringUtils.isBlank(path)) {
            path = get(PORT) == null ? String.format(URL_FORMAT, get(PROTOCOL), get(HOST), "") :
                    String.format(URL_FORMAT, get(PROTOCOL), get(HOST), ":" + get(PORT));
        } else if (path.endsWith(SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        String api = Optional.ofNullable(getPropertyAsString(API)).orElse(getPropertyAsString(PATH));
        api = api == null ? "" : !api.startsWith(SEPARATOR) ? SEPARATOR + api : api;
        return path + api;
    }
}