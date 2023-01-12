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

package protocol.xyz.migoo.http.sampler;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import protocol.xyz.migoo.http.util.HTTPConstantsInterface;
import xyz.migoo.simplehttp.Form;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.RequestEntity;

import java.util.Objects;

/**
 * @author xiaomi
 */
public class HTTPHCImpl extends Request implements HTTPConstantsInterface {

    public HTTPHCImpl(String method, String url) {
        super(StringUtils.isBlank(method) ? GET : method.trim().toUpperCase(), url);
    }

    public HTTPHCImpl query(Object query) {
        if (query != null) {
            super.query(Form.create((JSONObject) query));
        }
        return this;
    }

    public HTTPHCImpl body(byte[] bytes, Object json, Object data, String contentType) {
        RequestEntity entity = Objects.nonNull(bytes) ? RequestEntity.bytes(bytes, contentType) :
                Objects.nonNull(json) ? RequestEntity.json((JSONObject) json) :
                        Objects.nonNull(data) ? RequestEntity.form((JSONObject) data) : null;
        if (entity != null) {
            super.body(entity);
        }
        return this;
    }

    public HTTPHCImpl headers(JSONObject headers) {
        if (headers != null) {
            headers.forEach((name, value) -> this.addHeader(name, value == null ? "" : value.toString()));
        }
        return this;
    }

    public HTTPHCImpl cookie(JSONObject cookie) {
        if (cookie != null && cookie.size() > 0) {
            CookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie clientCookie = new BasicClientCookie(cookie.getString(COOKIE_NAME), cookie.getString(COOKIE_VALUE));
            clientCookie.setPath(cookie.getString(COOKIE_PATH));
            clientCookie.setDomain(cookie.getString(COOKIE_DOMAIN));
            cookieStore.addCookie(clientCookie);
            this.cookies(cookieStore);
        }
        return this;
    }
}
