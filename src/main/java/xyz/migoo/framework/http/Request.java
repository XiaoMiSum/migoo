package xyz.migoo.framework.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicHeader;
import xyz.migoo.exception.RequestException;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2018/10/10 20:34
 */
public class Request {

    public static final String UTF8 = "UTF-8";
    private static final String CONTENT_TYPE = "Content-Type";

    private boolean encode;
    private String url;
    private String method;
    private JSONObject body;
    private JSONObject query;
    private JSONObject data;
    private List<Header> header;
    private String title;
    private Header contentType;
    private JSONArray cookies;

    private Request(Builder builder) {
        this.url = builder.url.toString();
        this.method = builder.method;
        this.encode = builder.encode;
        this.body = builder.body;
        this.query = builder.query;
        this.data = builder.data;
        this.header = builder.headers;
        this.cookies = builder.cookies;
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

    public JSONArray cookies() {
        return this.cookies;
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
            if (CONTENT_TYPE.equalsIgnoreCase(header.getName())){
                this.contentType = header;
            }
        });
        return this.contentType;
    }

    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile(
                "^http[s]*://[\\w.\\-]+(:\\d*)*(?:/|(?:/[\\w.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);

        private StringBuilder url;
        private String method;
        private JSONObject body;
        private JSONObject query;
        private JSONObject data;
        private JSONArray cookies;
        private List<Header> headers = new ArrayList<>();
        private String title;
        private boolean encode = false;

        public Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder url(String url) {
            if (StringUtil.isEmpty(url)) {
                throw new RequestException("url == null");
            }
            this.url = new StringBuilder(url);
            return this;
        }

        public Builder api(String api) {
            if (!StringUtil.isEmpty(api)) {
                this.url.append(api);
            }
            return this;
        }

        private Builder headers(List<Header> headers) {
            if (headers == null || headers.isEmpty()) {
                return this;
            }
            this.headers.addAll(headers);
            return this;
        }

        private Builder headers(JSONObject headers) {
            if (headers == null || headers.isEmpty()) {
                return this;
            }
            headers.forEach((k,v) -> this.headers.add(new BasicHeader(k, String.valueOf(v))));
            return this;
        }

        public Builder headers(Object headers) {
            if (headers instanceof String){
                return this.headers(JSONObject.parseObject(headers.toString()));
            }
            if (headers instanceof JSONObject){
                return this.headers((JSONObject) headers);
            }
            if (headers instanceof List){
                return this.headers((List<Header>) headers);
            }
            return this;
        }

        public Builder cookies(Object cookies){
            if (cookies instanceof JSONObject){
                this.cookies = new JSONArray(1);
                this.cookies.add(cookies);
            }else if (cookies instanceof JSONArray){
                this.cookies = (JSONArray)cookies;
            }else if (cookies instanceof String){
                try {
                    this.cookies = JSON.parseArray((String) cookies);
                }catch (Exception e){
                    MiGooLog.log("cookies parse exception, skip");
                }
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
            this.encode = TypeUtil.booleanOf(encode);
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
                MiGooLog.log(e.getMessage(), e);
                throw new RequestException(e.getMessage());
            }
            return new Request(this);
        }

        private boolean urlCheck() {
            return !StringUtil.isEmpty(this.url.toString()) && (this.url.toString().startsWith("http://")
                    || this.url.toString().startsWith("https://"));
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