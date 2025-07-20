/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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
 *
 */

package protocol.xyz.migoo.http.config;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.config.ConfigureItem;
import protocol.xyz.migoo.http.HTTPConstantsInterface;

import java.util.Map;

/**
 * @author xiaomi
 * Created at 2025/7/19 20:17
 */
public class HttpConfigItem implements ConfigureItem<HttpConfigItem>, HTTPConstantsInterface {

    @JSONField(name = PROTOCOL)
    private String protocol = "http";

    @JSONField(name = HOST, ordinal = 1)
    private String host;

    @JSONField(name = PORT, ordinal = 2)
    private int port;

    @JSONField(name = PATH, ordinal = 3)
    private String path;

    @JSONField(name = REQUEST_METHOD, ordinal = 4)
    private String method = "GET";

    @JSONField(name = HTTP2, ordinal = 5)
    private boolean http2;

    @JSONField(name = HEADERS, ordinal = 6)
    private Map<String, String> headers;

    @JSONField(name = COOKIES, ordinal = 7)
    private Map<String, String> cookies;

    @JSONField(name = QUERY, ordinal = 5)
    private Map<String, String> query;

    @JSONField(name = DATA, ordinal = 5)
    private Map<String, String> data;

    @JSONField(name = BODY, ordinal = 5)
    private Object body;

    public HttpConfigItem() {

    }
    
    @Override
    public HttpConfigItem merge(HttpConfigItem other) {
        //  todo 合并时，使用当前对象的属性覆盖other对象的属性，返回新对象
        var self = other.copy();
        return self;
    }

    @Override
    public HttpConfigItem copy() {
        // todo 拷贝对象
        return new HttpConfigItem();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isHttp2() {
        return http2;
    }

    public void setHttp2(boolean http2) {
        this.http2 = http2;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
