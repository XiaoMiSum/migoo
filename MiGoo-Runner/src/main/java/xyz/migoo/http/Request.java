package xyz.migoo.http;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicHeader;
import org.apache.poi.ss.formula.functions.T;
import xyz.migoo.exception.RequestException;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

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
    private boolean encode;
    private JSONObject body;
    private JSONObject query;
    private JSONObject data;
    private List<Header> header;
    private String title;
    private Header contentType;

    private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.encode = builder.encode;
        this.body = builder.body;
        this.query = builder.query;
        this.data = builder.data;
        this.header = builder.headers;
        this.title = builder.title;
    }

    public String url() {
        return this.url;
    }

    public String method() {
        return this.method;
    }

    public String title() {
        return this.title;
    }

    public List<Header> headers() {
        return this.header;
    }

    public JSONObject body() {
        return this.body;
    }

    public JSONObject query() {
        return this.query;
    }

    public JSONObject data() {
        return this.data;
    }

    public boolean encode() {
        return encode;
    }

    public Header contentType() {
        if (this.contentType != null) {
            return contentType;
        }
        if (header == null) {
            return null;
        }
        header.forEach(header -> {
            if (StringUtil.equalsIgnoreCase(header.getName(), CONTENT_TYPE)){
                this.contentType = header;
            }
        });
        return this.contentType;
    }

    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile(
                "^http[s]*://[\\w.\\-]+(:\\d*)*(?:/|(?:/[\\w.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);

        private String url;
        private String method;
        private JSONObject body;
        private JSONObject query;
        private JSONObject data;
        private List<Header> headers;
        private String title;
        private boolean encode = false;

        public Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder url(String url) {
            if (StringUtil.isBlank(url)) {
                throw new RequestException("url == null");
            }
            this.url = url;
            return this;
        }

        private Builder headers(List<Header> headers) {
            if (headers == null || headers.isEmpty()) {
                return this;
            }
            this.headers = headers;
            return this;
        }

        private Builder headers(JSONObject headers) {
            if (headers == null || headers.isEmpty()) {
                return this;
            }
            this.headers = new ArrayList<>(headers.size());
            headers.forEach((k,v) -> this.headers.add(new BasicHeader(k, String.valueOf(v))));
            return this;
        }

        private Builder headers(String headers) {
            try {
                JSONObject json = JSONObject.parseObject(headers);
                this.headers(json);
            } catch (Exception e) {
                this.headers = null;
            }
            return this;
        }

        public Builder headers(Object headers) {
            if (headers instanceof String){
                return this.headers((String) headers);
            }
            if (headers instanceof JSONObject){
                return this.headers((JSONObject) headers);
            }
            if (headers instanceof List){
                return this.headers(headers);
            }
            return this;
        }

        public Builder get() {
            this.method = HttpGet.METHOD_NAME;
            return this;
        }

        public Builder post() {
            this.method = HttpPost.METHOD_NAME;
            return this;
        }

        public Builder put() {
            this.method = HttpPut.METHOD_NAME;
            return this;
        }

        public Builder delete() {
            this.method = HttpDelete.METHOD_NAME;
            return this;
        }

        public Builder method(String method) {
            this.method = method.toUpperCase();
            return this;
        }

        public Builder encode(Object encode){
            Boolean b = TypeUtils.castToBoolean(encode);
            if (b != null) {
                this.encode = b;
            }
            return this;
        }

        public Builder body(Object body) {
            if (body instanceof JSONObject) {
                this.body = (JSONObject) body;
                return this;
            }
            if (body instanceof String){
                this.body = JSONObject.parseObject((String) body);
                return this;
            }
            this.body = (JSONObject)JSONObject.toJSON(body);
            return this;
        }

        public Builder data(Object data) {
            if (data instanceof JSONObject) {
                this.data = (JSONObject) data;
                return this;
            }
            if (data instanceof String){
                this.data = JSONObject.parseObject((String) data);
                return this;
            }
            this.data = (JSONObject)JSONObject.toJSON(data);
            return this;
        }

        public Builder query(Object query) {
            if (query instanceof JSONObject) {
                this.query = (JSONObject) query;
                return this;
            }
            if (query instanceof String){
                this.query = JSONObject.parseObject((String) query);
                return this;
            }
            this.query = (JSONObject)JSONObject.toJSON(query);
            return this;
        }

        public Request build() {
            try {
                if (!this.urlCheck()) {
                    throw new RequestException("url == null or not a url :  " + url);
                }
                if (!this.methodCheck(method)){
                    throw new RequestException("unknown method ' " + method +" '");
                }
                if (HttpGet.METHOD_NAME.equals(method) && !checkQuery()){
                    if (checkData()){
                        query = data;
                    }
                    if (checkBody()){
                        query = body;
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new RequestException(e.getMessage());
            }
            return new Request(this);
        }

        private boolean urlCheck() {
            return StringUtil.isNotBlank(this.url) && PATTERN.matcher(url).find();
        }

        private boolean methodCheck(String method) {
            return HttpGet.METHOD_NAME.equals(method)
                    || HttpPost.METHOD_NAME.equals(method)
                    || HttpDelete.METHOD_NAME.equals(method)
                    || HttpPut.METHOD_NAME.equals(method);
        }

        private boolean checkBody(){
            return  body != null && !body.isEmpty();
        }

        private boolean checkData(){
            return  data != null && !data.isEmpty();
        }

        private boolean checkQuery(){
            return  query != null && !query.isEmpty();
        }
    }
}
