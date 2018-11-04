package xyz.migoo.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicHeader;
import xyz.migoo.exception.RequestException;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
public class Request {

    public static final String UTF8 = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";

    private String url;
    private String method;
    private File certificate;
    private JSONObject body;
    private JSONObject query;
    private JSONObject cookie;
    private List<Header> headers;
    private HttpHost httpHost;
    private String title;
    private Header contentType;
    private int timeOut;
    private boolean restful;

    private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
        this.query = builder.query;
        this.cookie = builder.cookie;
        this.certificate = builder.certificate;
        this.headers = builder.headers;
        this.httpHost = builder.httpHost;
        this.title = builder.title;
        this.timeOut = builder.timeOut;
        this.restful = builder.restful;
    }

    public HttpHost proxy() {
        return this.httpHost;
    }

    public String url() {
        return this.url;
    }

    public String method() {
        return this.method;
    }

    public String title() {
        return title;
    }

    public File certificate() {
        return this.certificate;
    }

    public List<Header> headers() {
        return this.headers;
    }

    public JSONObject query() {
        return this.query;
    }

    public JSONObject body() {
        return this.body;
    }

    public int timeOut() {
        return timeOut;
    }

    public boolean restful() {
        return restful;
    }

    @Deprecated
    public JSONObject cookie() {
        return this.cookie;
    }

    public Header contentType() {
        if (this.contentType != null) {
            return contentType;
        }
        headers.forEach(header -> {
            if (header.getName().toLowerCase().equals(CONTENT_TYPE.toLowerCase())) {
                this.contentType = header;
            }
        });
        return contentType;
    }

    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile(
                "^http[s]*://[\\w\\.\\-]+(:\\d*)*(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);

        private String url;
        private String method;
        private File certificate;
        private JSONObject body;
        private JSONObject query;
        private JSONObject cookie;
        private List<Header> headers;
        private HttpHost httpHost;
        private String title;
        private int timeOut = 20;
        private boolean restful = false;

        public Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder proxy(String proxy) {
            HttpHost httpHost = null;
            try {
                JSONObject json = JSONObject.parseObject(proxy);
                httpHost = new HttpHost(json.getString("host"), json.getIntValue("port"));
            } catch (Exception e) {
                String regex = ":";
                if (StringUtil.containsIgnoreCase(proxy, regex)) {
                    String[] host = proxy.split(regex);
                    httpHost = new HttpHost(host[0], Integer.valueOf(host[1]));
                }
            }
            this.httpHost = httpHost;
            return this;
        }

        public Builder url(String url) {
            if (StringUtil.isBlank(url)) {
                throw new RequestException("url == null");
            }
            this.url = url;
            return this;
        }

        public Builder headers(List<Header> headers) {
            if (headers == null || headers.size() == 0) {
                return this;
            }
            this.headers = headers;
            return this;
        }

        public Builder headers(JSONObject headers) {
            if (headers == null || headers.size() == 0) {
                return this;
            }
            this.headers = new ArrayList<>(headers.size());
            headers.forEach((k, v) -> this.headers.add(new BasicHeader(k, String.valueOf(v))));
            return this;
        }

        public Builder headers(String headers) {
            try {
                JSONObject json = JSONObject.parseObject(headers);
                this.headers(json);
            } catch (Exception e) {
                this.headers = null;
            }
            return this;
        }

        public Builder certificate(String certificate) {
            if (StringUtil.isNotBlank(certificate)) {
                this.certificate = new File(certificate);
            }
            return this;
        }

        public Builder get() {
            this.method = HttpGet.METHOD_NAME;
            return this;
        }

        public Builder get(Object body) {
            return this.method(HttpGet.METHOD_NAME).body(body);
        }

        public Builder post() {
            this.method = HttpPost.METHOD_NAME;
            return this;
        }

        public Builder post(Object body) {
            return this.method(HttpPost.METHOD_NAME).body(body);
        }

        public Builder put() {
            this.method = HttpPut.METHOD_NAME;
            return this;
        }

        public Builder put(Object body) {
            return this.method(HttpPut.METHOD_NAME).body(body);
        }

        public Builder delete() {
            this.method = HttpDelete.METHOD_NAME;
            return this;
        }

        public Builder delete(Object body) {
            return this.method(HttpDelete.METHOD_NAME).body(body);
        }

        public Builder method(String method) {
            this.method = method.toUpperCase();
            return this;
        }

        public Builder timeOut(int timeOut) {
            if (timeOut > this.timeOut) {
                this.timeOut = timeOut;
            }
            return this;
        }

        public Builder restful(Object value) {
            Boolean b = TypeUtil.booleanOf(value);
            if (b != null) {
                restful = b.booleanValue();
            }
            return this;
        }

        public Builder body(Object body) {
            if (body == null) {
                this.body = new JSONObject(0);
                return this;
            }
            if (body instanceof JSONObject) {
                this.body = (JSONObject) body;
                return this;
            }
            if (body instanceof String) {
                this.body = JSONObject.parseObject((String) body);
                return this;
            }
            this.body = (JSONObject)JSONObject.toJSON(body);
            return this;
        }

        public Builder query(Object query) {
            if (query == null) {
                this.query = new JSONObject(0);
                return this;
            }
            if (query instanceof JSONObject) {
                this.query = (JSONObject) query;
                return this;
            }
            if (query instanceof String) {
                this.query = JSONObject.parseObject((String) query);
                return this;
            }
            this.query = (JSONObject)JSONObject.toJSON(query);
            return this;
        }

        @Deprecated
        public Builder cookie(Object cookie) {
            if (cookie == null) {
                this.cookie = new JSONObject(0);
                return this;
            }
            if (cookie instanceof JSONObject) {
                this.cookie = (JSONObject) cookie;
                return this;
            }
            if (cookie instanceof String) {
                this.cookie = JSONObject.parseObject((String) cookie);
                return this;
            }
            this.cookie = (JSONObject) JSONObject.toJSON(cookie);
            return this;
        }

        public Request build() {
            if (StringUtil.isBlank(this.url) || !this.urlCheck()) {
                throw new RequestException("url == null or not a url :  " + url);
            }
            if (StringUtil.isBlank(method)) {
                throw new RequestException("method == null || method.length() == 0");
            }
            if (!this.methodCheck()) {
                throw new RequestException("unknown method ' " + method + " '");
            }
            if (certificate != null && certificate.isDirectory()) {
                throw new RequestException("certificate can not be directory . certificate path : " + certificate);
            }
            return new Request(this);
        }

        private boolean urlCheck() {
            return PATTERN.matcher(url).find();
        }

        private boolean methodCheck() {
            return HttpGet.METHOD_NAME.equals(method)
                    || HttpPost.METHOD_NAME.equals(method)
                    || HttpPut.METHOD_NAME.equals(method)
                    || HttpDelete.METHOD_NAME.equals(method);
        }
    }
}
