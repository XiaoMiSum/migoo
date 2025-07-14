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
import protocol.xyz.migoo.http.sampler.HC;
import protocol.xyz.migoo.http.sampler.HTTPSampleResult;
import protocol.xyz.migoo.http.util.HTTPConstantsInterface;

import java.util.Objects;
import java.util.Optional;

import static org.apache.hc.core5.http.HttpVersion.HTTP_1_1;
import static org.apache.hc.core5.http.HttpVersion.HTTP_2;

/**
 * @author xiaomi
 */
public abstract class AbstractHttpTestElement extends AbstractTestElement implements HTTPConstantsInterface {

    private static final String URL_FORMAT = "%s://%s%s";

    protected final String name = this.getClass().getSimpleName().toLowerCase();

    public void testStarted() {
        var other = (HttpDefaults) getVariables().get(HTTP_DEFAULT);
        if (Objects.nonNull(other)) {
            setProperty(HTTP2, other.getPropertyAsBoolean(HTTP2));
            setProperty(BASE_PATH, other.getPropertyAsString(BASE_PATH));
            setProperty(PROTOCOL, other.getPropertyAsString(PROTOCOL));
            setProperty(REQUEST_METHOD, other.getPropertyAsString(REQUEST_METHOD));
            setProperty(HOST, other.getPropertyAsString(HOST));
            setProperty(PORT, Optional.ofNullable(other.get(PORT)).orElse(""));
            setProperty(PATH, Optional.ofNullable(other.getPropertyAsString(API)).orElse(other.getPropertyAsString(PATH)));
            setProperty(COOKIE, other.get(COOKIE));
            this.setHeader(other.getProperty());
        }
        if (getProperty().containsKey(HEADERS)) {
            getPropertyAsMiGooProperty(HEADERS).remove(VARIABLES);
        }
    }

    private void setHeader(MiGooProperty other) {
        var otherHeaders = other.getJSONObject(HEADERS);
        if (otherHeaders != null && !otherHeaders.isEmpty()) {
            var thisHeaders = getPropertyAsMiGooProperty(HEADERS);
            if (thisHeaders == null) {
                thisHeaders = new MiGooProperty();
                thisHeaders.putAll(otherHeaders);
                getProperty().put(HEADERS, thisHeaders);
            } else {
                var headers = new MiGooProperty();
                headers.putAll(otherHeaders);
                headers.putAll(thisHeaders);
                getProperty().put(HEADERS, headers);
            }
        }
    }

    public SampleResult execute() {
        return execute(new HTTPSampleResult(getPropertyAsString(TITLE)));
    }

    protected SampleResult execute(SampleResult sample) {
        var result = (HTTPSampleResult) sample;
        result.setTestClass(this.getClass());
        try {
            var headers = Optional.ofNullable(getPropertyAsJSONObject(HEADERS)).orElse(new JSONObject());
            var request = HC.getInstance(getPropertyAsString(REQUEST_METHOD), buildUrl())
                    .headers(headers).cookie(get(COOKIE))
                    .query(getPropertyAsJSONObject(QUERY))
                    // bytes body data(binary) 不会同时出现
                    .bytes(getPropertyAsByteArray(BYTES), headers.getString(HEADER_CONTENT_TYPE))
                    .body(get(BODY))
                    .body(getPropertyAsJSONObject(BINARY), getPropertyAsJSONObject(DATA))
                    .version(getPropertyAsBoolean(HTTP2) ? HTTP_2 : HTTP_1_1);
            result.setRequestData(request);
            result.sampleStart();
            var response = request.execute();
            result.setResponseData(response);
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    private String buildUrl() {
        var path = getPropertyAsString(BASE_PATH);
        if (StringUtils.isBlank(path)) {
            var port = Optional.ofNullable(getPropertyAsString(PORT)).orElse("");
            var protocol = Optional.ofNullable(getPropertyAsString(PROTOCOL)).orElse("http");
            path = String.format(URL_FORMAT, protocol, get(HOST), port.isEmpty() ? "" : (":" + port));
        } else if (path.endsWith(SEPARATOR)) {
            path = path.substring(0, path.length() - 1);
        }
        var api = Optional.ofNullable(getPropertyAsString(API)).orElse(getPropertyAsString(PATH));
        api = Objects.isNull(api) ? "" : !api.startsWith(SEPARATOR) ? SEPARATOR + api : api;
        return path + api;
    }
}