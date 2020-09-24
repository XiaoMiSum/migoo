/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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


package core.xyz.migoo.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import xyz.migoo.simplehttp.*;

import java.net.URI;
import java.util.ArrayList;

/**
 * @author xiaomi
 * @date 2019/9/16 20:03
 */
public class MiGooRequest extends Request {

    public static MiGooRequest get(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new HttpRequest("GET", URI.create(url)));
    }

    public static MiGooRequest post(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new EntityEnclosingHttpRequest("POST", URI.create(url)));
    }

    public static MiGooRequest put(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new EntityEnclosingHttpRequest("PUT", URI.create(url)));
    }

    public static MiGooRequest delete(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new HttpRequest("DELETE", URI.create(url)));
    }

    public static MiGooRequest head(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new HttpRequest("HEAD", URI.create(url)));
    }

    public static MiGooRequest patch(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new HttpRequest("PATCH", URI.create(url)));
    }

    public static MiGooRequest trace(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new HttpRequest("TRACE", URI.create(url)));
    }

    public static MiGooRequest options(String url) {
        MiGooRequest request = new MiGooRequest();
        return (MiGooRequest) request.request(new HttpRequest("OPTIONS", URI.create(url)));
    }

    public JSONObject jsonHeaders() {
        Header[] headers = headers();
        if (headers != null) {
            JSONObject jsonHeaders = new JSONObject();
            for (Header header : headers) {
                jsonHeaders.put(header.getName(), header.getValue());
            }
            return jsonHeaders;
        }
        return null;
    }

    public JSONArray cookies() {
        return context() != null && context().getCookieStore() != null ?
                new JSONArray(new ArrayList<>(context().getCookieStore().getCookies())) : null;
    }

    public static class Builder {

        private String method;

        private String protocol;

        private String host;

        private Integer port;

        private String api;

        private JSONObject headers;

        private JSON cookie;

        private JSON body;

        private JSONObject data;

        private JSONObject query;

        public Builder protocol(String protocol) {
            this.protocol = StringUtils.isBlank(protocol) ? "http" : protocol;
            return this;
        }

        public Builder method(String method) {
            this.method = StringUtils.isBlank(method) ? "GET" : method.toUpperCase();
            return this;
        }

        public Builder host(String host) {
            this.host = StringUtils.isBlank(host) ? "127.0.0.1" : host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder api(String api) {
            this.api = StringUtils.isBlank(api) ? "" : api.startsWith("/") ? api : "/" + api;
            return this;
        }

        public Builder body(Object body) {
            if (body instanceof JSON) {
                this.body = (JSON) body;
            }
            return this;
        }

        public Builder data(JSONObject data) {
            if (data != null && !data.isEmpty()) {
                this.data = data;
            }
            return this;
        }

        public Builder query(JSONObject query) {
            if (query != null && !query.isEmpty()) {
                this.query = query;
            }
            return this;
        }

        public Builder cookies(Object cookie) {
            if (cookie instanceof JSON) {
                this.cookie = (JSON) cookie;
            }
            return this;
        }

        public Builder headers(JSONObject headers) {
            this.headers = headers;
            return this;
        }

        private void setCookie(JSONObject cookie, CookieStore cookieStore) {
            BasicClientCookie clientCookie = new BasicClientCookie(cookie.getString("name"), cookie.getString("value"));
            clientCookie.setPath(cookie.getString("path"));
            clientCookie.setDomain(cookie.getString("domain"));
            cookieStore.addCookie(clientCookie);
        }

        private void setCookie(JSONArray cookies, CookieStore cookieStore) {
            for (int i = 0; i < cookies.size(); i++) {
                setCookie(cookies.getJSONObject(i), cookieStore);
            }
        }

        public MiGooRequest build() {
            String url = String.format("%s://%s%s%s", protocol, host, port != null ? ":" + port : "", api);
            MiGooRequest request = HttpPost.METHOD_NAME.equals(method) ? MiGooRequest.post(url)
                    : HttpGet.METHOD_NAME.equals(method) ? MiGooRequest.get(url)
                    : HttpPut.METHOD_NAME.equals(method) ? MiGooRequest.put(url)
                    : HttpDelete.METHOD_NAME.equals(method) ? MiGooRequest.delete(url)
                    : HttpHead.METHOD_NAME.equals(method) ? MiGooRequest.head(url)
                    : HttpOptions.METHOD_NAME.equals(method) ? MiGooRequest.options(url)
                    : HttpPatch.METHOD_NAME.equals(method) ? MiGooRequest.patch(url)
                    : HttpTrace.METHOD_NAME.equals(method) ? MiGooRequest.trace(url)
                    : MiGooRequest.get(url);
            if (headers != null && !headers.isEmpty()) {
                headers.forEach((k, v) -> request.addHeader(k, v == null ? null : v.toString()));
            }
            if (query != null && !query.isEmpty()) {
                Form form = new Form();
                query.forEach((k, v) -> form.add(k, v == null || v == "" ? null : v.toString()));
                request.query(form);
            }
            if (data != null && !data.isEmpty()) {
                Form form = new Form();
                data.forEach((k, v) -> form.add(k, v == null || v == "" ? null : v.toString()));
                request.data(form);
            }
            if (body != null) {
                request.bodyJson(body.toJSONString());
            }
            if (cookie != null) {
                CookieStore cookieStore = new BasicCookieStore();
                if (cookie instanceof JSONObject) {
                    setCookie((JSONObject) cookie, cookieStore);
                } else if (cookie instanceof JSONArray) {
                    setCookie((JSONArray) cookie, cookieStore);
                }
                request.cookies(cookieStore);
            }
            return request;
        }
    }
}
