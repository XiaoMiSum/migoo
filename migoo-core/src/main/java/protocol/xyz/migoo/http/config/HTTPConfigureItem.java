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
import core.xyz.migoo.context.ContextWrapper;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.http.HTTPConstantsInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static core.xyz.migoo.testelement.TestElementConstantsInterface.DATASOURCE;

/**
 * @author xiaomi
 * Created at 2025/7/19 20:17
 */
public class HTTPConfigureItem implements ConfigureItem<HTTPConfigureItem>, HTTPConstantsInterface {

    @JSONField(name = DATASOURCE)
    protected String datasource;
    @JSONField(name = PROTOCOL, ordinal = 1)
    private String protocol;

    @JSONField(name = HOST, ordinal = 2)
    private String host;

    @JSONField(name = PORT, ordinal = 2)
    private String port;

    @JSONField(name = PATH, ordinal = 3)
    private String path;

    @JSONField(name = REQUEST_METHOD, ordinal = 4)
    private String method;

    @JSONField(name = HTTP2, ordinal = 5)
    private Boolean http2;

    @JSONField(name = HEADERS, ordinal = 6)
    private Map<String, String> headers;

    @JSONField(name = COOKIE, ordinal = 7)
    private Map<String, String> cookie;

    @JSONField(name = QUERY, ordinal = 5)
    private Map<String, String> query;

    @JSONField(name = DATA, ordinal = 5)
    private Map<String, String> data;

    @JSONField(name = BODY, ordinal = 5)
    private Object body;

    @JSONField(name = BYTES, ordinal = 5)
    private byte[] bytes;
    @JSONField(name = BINARY, ordinal = 5)
    private Object binary;


    public HTTPConfigureItem() {

    }

    /**
     * 合并属性，如果当前对象的属性值为空，则以 other 对象的属性值替换
     *
     * @param other 其他对象
     * @return 合并后的新对象
     */
    @Override
    public HTTPConfigureItem merge(HTTPConfigureItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.protocol = StringUtils.isBlank(self.protocol) ? localOther.protocol : self.protocol;
        self.host = StringUtils.isBlank(self.host) ? localOther.host : self.host;
        self.port = StringUtils.isBlank(self.port) ? localOther.port : self.port;
        self.path = StringUtils.isBlank(self.path) ? localOther.path : self.path;
        self.method = StringUtils.isBlank(self.method) ? localOther.method : self.method;
        self.http2 = self.http2 == null ? localOther.http2 : self.http2;
        self.headers = handleMap(other.headers, headers);
        self.cookie = handleMap(other.cookie, cookie);
        self.query = self.query == null ? localOther.query : self.query;
        self.data = self.data == null ? localOther.data : self.data;
        self.body = self.body == null ? localOther.body : self.body;
        return self;
    }

    @Override
    public HTTPConfigureItem calc(ContextWrapper context) {
        // todo 这里要实现 变量替换
        return this;
    }

    private Map<String, String> handleMap(Map<String, String> other, Map<String, String> self) {
        var resp = new HashMap<String, String>();
        if (other != null) {
            resp.putAll(other);
        }
        if (self != null) {
            resp.putAll(self);
        }
        return resp;
    }

    public String getDatasource() {
        return StringUtils.isBlank(datasource) ? DEF_REF_NAME_KEY : datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getProtocol() {
        return StringUtils.isBlank(protocol) ? PROTOCOL_HTTP : protocol;
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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath() {
        return StringUtils.isBlank(path) ? "/" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return StringUtils.isBlank(method) ? GET : method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isHttp2() {
        return Objects.nonNull(http2) && http2;
    }

    public void setHttp2(Boolean http2) {
        this.http2 = http2;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }

    public void setCookie(Map<String, String> cookie) {
        this.cookie = cookie;
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

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Object getBinary() {
        return binary;
    }

    public void setBinary(Object binary) {
        this.binary = binary;
    }

}
