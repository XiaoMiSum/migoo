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

package io.github.xiaomisum.ryze.protocol.http.config;

import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.protocol.http.HTTPConstantsInterface;
import io.github.xiaomisum.ryze.support.Collections;
import io.github.xiaomisum.ryze.support.Customizer;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface.REF;
import static io.github.xiaomisum.ryze.support.groovy.Groovy.call;

/**
 * @author xiaomi
 * Created at 2025/7/19 20:17
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class HTTPConfigureItem implements ConfigureItem<HTTPConfigureItem>, HTTPConstantsInterface {

    @JSONField(name = REF)
    protected String ref;
    @JSONField(name = PROTOCOL, ordinal = 1)
    protected String protocol;
    @JSONField(name = HOST, ordinal = 2)
    protected String host;
    @JSONField(name = PORT, ordinal = 2)
    protected String port;
    @JSONField(name = PATH, ordinal = 3)
    protected String path;
    @JSONField(name = REQUEST_METHOD, ordinal = 4)
    protected String method;
    @JSONField(name = HTTP2, ordinal = 5)
    protected Boolean http2;
    @JSONField(name = HEADERS, ordinal = 6)
    protected Map<String, String> headers;
    @JSONField(name = COOKIE, ordinal = 7)
    protected Map<String, String> cookie;
    @JSONField(name = QUERY, ordinal = 5)
    protected Map<String, Object> query;
    @JSONField(name = DATA, ordinal = 5)
    protected Map<String, Object> data;
    @JSONField(name = BODY, ordinal = 5)
    protected Object body;
    @JSONField(name = BYTES, ordinal = 5)
    protected byte[] bytes;
    @JSONField(name = BINARY, ordinal = 5)
    protected Object binary;

    public HTTPConfigureItem() {

    }

    public static Builder builder() {
        return new Builder();
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
        self.bytes = self.bytes == null ? localOther.bytes : self.bytes;
        self.binary = self.binary == null ? localOther.binary : self.binary;
        return self;
    }

    @Override
    public HTTPConfigureItem evaluate(ContextWrapper context) {
        protocol = (String) context.evaluate(protocol);
        host = (String) context.evaluate(host);
        port = (String) context.evaluate(port);
        path = (String) context.evaluate(path);
        method = (String) context.evaluate(method);
        headers = (Map<String, String>) context.evaluate(headers);
        cookie = (Map<String, String>) context.evaluate(cookie);
        query = (Map<String, Object>) context.evaluate(query);
        data = (Map<String, Object>) context.evaluate(data);
        body = context.evaluate(body);
        binary = context.evaluate(binary);
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

    public String getRef() {
        return StringUtils.isBlank(ref) ? DEF_REF_NAME_KEY : ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
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

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
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

    /**
     * HTTP协议配置项构建类
     */
    public static class Builder extends AbstractTestElement.ConfigureBuilder<HTTPConfigureItem.Builder, HTTPConfigureItem> {

        private HTTPConfigureItem configure = new HTTPConfigureItem();

        public Builder() {
        }

        public Builder ref(String ref) {
            configure.ref = ref;
            return self;
        }

        public Builder protocol(String protocol) {
            configure.protocol = protocol;
            return self;
        }

        public Builder host(String host) {
            configure.host = host;
            return self;
        }

        public Builder port(String port) {
            configure.port = port;
            return self;
        }

        public Builder path(String path) {
            configure.path = path;
            return self;
        }

        public Builder method(String method) {
            configure.method = method;
            return self;
        }

        public Builder http2() {
            configure.http2 = true;
            return self;
        }

        public Builder headers(Map<String, String> headers) {
            configure.headers = Collections.putAllIfNonNull(configure.headers, headers);
            return self;
        }

        public Builder headers(Customizer<Map<String, String>> customizer) {
            Map<String, String> headers = new HashMap<>();
            customizer.customize(headers);
            configure.headers = Collections.putAllIfNonNull(configure.headers, headers);
            return self;
        }


        public Builder cookie(Map<String, String> cookie) {
            configure.cookie = Collections.putAllIfNonNull(configure.cookie, cookie);
            return self;
        }

        public Builder cookie(Customizer<Map<String, String>> customizer) {
            Map<String, String> cookie = new HashMap<>();
            customizer.customize(cookie);
            configure.cookie = Collections.putAllIfNonNull(configure.cookie, cookie);
            return self;
        }

        public Builder query(Map<String, Object> query) {
            configure.query = Collections.putAllIfNonNull(configure.query, query);
            return self;
        }

        public Builder query(Customizer<Map<String, Object>> customizer) {
            Map<String, Object> query = new HashMap<>();
            customizer.customize(query);
            configure.query = Collections.putAllIfNonNull(configure.query, query);
            return self;
        }

        public Builder data(Map<String, Object> data) {
            configure.data = Collections.putAllIfNonNull(configure.data, data);
            return self;
        }

        public Builder data(Customizer<Map<String, Object>> customizer) {
            var data = new HashMap<String, Object>();
            customizer.customize(data);
            configure.data = Collections.putAllIfNonNull(configure.data, data);
            return self;
        }

        public Builder body(Customizer<Map<String, Object>> customizer) {
            if (configure.body != null && !(configure.body instanceof Map)) {
                return self;
            }
            var body = new HashMap<String, Object>();
            customizer.customize(body);
            configure.body = Collections.putAllIfNonNull((Map) configure.body, body);
            return self;
        }

        public Builder body(Map<String, Object> body) {
            if (configure.body != null && !(configure.body instanceof Map)) {
                return self;
            }
            configure.body = Collections.putAllIfNonNull((Map) configure.body, body);
            return self;
        }

        public Builder body(Object body) {
            configure.body = body;
            return self;
        }

        public Builder bytes(byte[] bytes) {
            configure.bytes = bytes;
            return self;
        }

        public Builder binary(Object binary) {
            configure.binary = binary;
            return self;
        }

        public Builder config(Consumer<HTTPConfigureItem.Builder> consumer) {
            var builder = HTTPConfigureItem.builder();
            consumer.accept(builder);
            configure = configure.merge(builder.build());
            return self;
        }

        public Builder config(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPConfigureItem.Builder.class) Closure<?> closure) {
            var builder = HTTPConfigureItem.builder();
            call(closure, builder);
            configure = configure.merge(builder.build());
            return self;
        }

        public Builder config(HTTPConfigureItem.Builder builder) {
            configure = configure.merge(builder.build());
            return self;
        }

        public Builder config(HTTPConfigureItem config) {
            configure = configure.merge(config);
            return self;
        }

        @Override
        public HTTPConfigureItem build() {
            return configure;
        }
    }
}
