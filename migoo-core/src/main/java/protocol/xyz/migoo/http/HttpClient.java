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

package protocol.xyz.migoo.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import xyz.migoo.simplehttp.Form;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.RequestEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaomi
 * Created at 2025/7/20 23:01
 */
public class HttpClient extends Request implements HTTPConstantsInterface {

    protected HttpClient(String method, String url) {
        super(method, url);
    }

    public static HttpClient getInstance(String method, String url) {
        return new HttpClient(method, url);
    }


    public HttpClient query(Map<String, Object> query) {
        if (query != null) {
            super.query(Form.create(query));
        }
        return this;
    }

    public HttpClient bytes(byte[] bytes, String contentType) {
        if (Objects.nonNull(bytes)) {
            super.body(RequestEntity.bytes(bytes, contentType));
        }
        return this;
    }

    public HttpClient body(Object body) {
        if (body instanceof Map<?, ?> || body instanceof List<?>) {
            super.body(RequestEntity.json(JSON.toJSONString(body)));
        } else if (Objects.nonNull(body)) {
            super.body(RequestEntity.text(body.toString()));
        }
        return this;
    }

    public HttpClient body(JSONObject binary, JSONObject data) {
        if (Objects.nonNull(binary) && !binary.isEmpty()) {
            var files = new ArrayList<NameValuePair>();
            binary.forEach((key, value) -> {
                if (value instanceof String path) {
                    files.add(new BasicNameValuePair(key, path));
                } else if (value instanceof List<?> paths) {
                    paths.forEach(path -> files.add(new BasicNameValuePair(key, (String) path)));
                }
            });
            // 如果 binary 非空，则认为 data 为 binary 中的消息
            super.body(RequestEntity.binary(files, data));
        } else if (Objects.nonNull(data)) {
            super.body(RequestEntity.form(data));
        }
        return this;
    }

    public HttpClient headers(JSONObject headers) {
        if (headers != null) {
            headers.forEach((name, value) -> this.addHeader(name, value == null ? "" : value.toString()));
        }
        return this;
    }

    public HttpClient cookie(Object cookie) {
        if (cookie instanceof JSONObject object) {
            addCookie(toCookie(object));
        } else if (cookie instanceof JSONArray objects) {
            addCookie(toCookie(objects));
        }
        return this;
    }

    private Cookie toCookie(JSONObject object) {
        var cookie = new BasicClientCookie(object.getString(COOKIE_NAME), object.getString(COOKIE_VALUE));
        cookie.setPath(object.getString(COOKIE_PATH));
        cookie.setDomain(object.getString(COOKIE_DOMAIN));
        return cookie;
    }

    private Cookie[] toCookie(JSONArray objects) {
        List<Cookie> cookies = new ArrayList<>(objects.size());
        for (int i = 0; i < objects.size(); i++) {
            cookies.add(toCookie(objects.getJSONObject(i)));
        }
        return cookies.toArray(new Cookie[0]);
    }
}
