package xyz.migoo.http;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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

import java.io.File;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
public class Request {

    public static final String UTF8 = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";

    private static Log log = new Log(Request.class);

    private String url;
    private String method;
    private File certificate;
    private JSONObject body;
    private JSONObject cookie;
    private List<Header> header;
    private HttpHost httpHost;
    private String title;
    private Header contentType;
    private int timeOut;

    private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
        this.cookie = builder.cookie;
        this.certificate = builder.certificate;
        this.header = builder.headers;
        this.httpHost = builder.httpHost;
        this.title = builder.title;
        this.timeOut = builder.timeOut;
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
        return this.header;
    }

    public JSONObject body() {
        return this.body;
    }
    public JSONObject cookie() {
        return this.cookie;
    }

    public Header contentType() {
        if (this.contentType != null) {
            return contentType;
        }
        for (Header header : header) {
            if (header.getName().toLowerCase().equals(CONTENT_TYPE.toLowerCase())) {
                this.contentType = header;
            }
        }
        return contentType;
    }

    protected boolean isFrom(){
        Header header = this.contentType();
        return header == null || !StringUtil.containsIgnoreCase(header.getValue(),"json");
    }

    public int timeOut() {
        return timeOut;
    }


    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile(
                "^http[s]*://[\\w\\.\\-]+(:\\d*)*(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);

        private String url;
        private String method;
        private File certificate;
        private JSONObject body;
        private JSONObject cookie;
        private List<Header> headers;
        private HttpHost httpHost;
        private String title;
        private int timeOut = 20;

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
            Set<Map.Entry<String, Object>> set = headers.entrySet();
            for (Map.Entry entry : set) {
                this.headers.add(new BasicHeader(String.valueOf(entry.getKey()),
                        String.valueOf(entry.getValue())));
            }
            return this;
        }

        public Builder headers(String headers) {
            try {
                JSONObject json = JSONObject.parseObject(headers);
                this.headers(json);
            }catch (Exception e){
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
            if (timeOut > this.timeOut){
                this.timeOut = timeOut;
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
            if (body instanceof String){
                this.body = JSONObject.parseObject((String) body);
                return this;
            }
            this.body = JSONObject.parseObject(JSONObject.toJSONString(body, SerializerFeature.SortField));
            return this;
        }

        public Builder cookie(Object cookie) {
            if (cookie == null) {
                this.cookie = new JSONObject(0);
                return this;
            }
            if (cookie instanceof JSONObject) {
                this.cookie = (JSONObject) cookie;
                return this;
            }
            if (cookie instanceof String){
                this.cookie = JSONObject.parseObject((String) cookie);
                return this;
            }
            this.cookie = JSONObject.parseObject(JSONObject.toJSONString(cookie, SerializerFeature.SortField));
            return this;
        }

        public Request build() {
            try {
                if (StringUtil.isBlank(this.url) || !this.urlCheck(this.url)) {
                    throw new RequestException("url == null or not a url :  " + url);
                }
                if (StringUtil.isBlank(method)) {
                    throw new RequestException("method == null || method.length() == 0");
                }
                if (certificate != null && certificate.isDirectory()) {
                    throw new RequestException("certificate can not be directory . certificate path : " + certificate);
                }
                if (body == null || body.size() == 0) {
                    if (this.noneRequestBody(method)) {
                        StringBuilder sb = new StringBuilder();
                        for (String key : body.keySet()) {
                            sb.append(key)
                                    .append("=")
                                    .append(URLEncoder.encode(body.getString(key), UTF8))
                                    .append("&");
                        }
                        url = url + "?" + sb.toString().substring(0, sb.length() - 1);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RequestException(e.getMessage());
            }
            return new Request(this);
        }

        private boolean permitsRequestBody(String method) {
            return method.equals(HttpPost.METHOD_NAME)
                    || method.equals(HttpPut.METHOD_NAME)
                    || method.equals(HttpDelete.METHOD_NAME);
        }

        private boolean noneRequestBody(String method) {
            return method.equals(HttpGet.METHOD_NAME);
        }

        private boolean urlCheck(String url) {
            return PATTERN.matcher(url).find();
        }
    }
}
