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

package io.github.xiaomisum.ryze.protocol.http;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.xiaomisum.ryze.protocol.http.config.HTTPConfigureItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
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

import static org.apache.hc.core5.http.HttpVersion.HTTP_1_1;
import static org.apache.hc.core5.http.HttpVersion.HTTP_2;

/**
 * HTTP客户端实现类
 * <p>
 * 该类继承自 {@link Request}类，提供了构建和配置HTTP请求的高级功能。
 * 支持设置请求头、Cookie、查询参数、请求体等多种HTTP请求元素。
 * </p>
 *
 * <p>主要功能：
 * <ul>
 *   <li>构建完整的HTTP请求URL</li>
 *   <li>设置请求头和Cookie</li>
 *   <li>处理多种请求体格式（JSON、表单、二进制文件等）</li>
 *   <li>支持HTTP/1.1和HTTP/2协议</li>
 * </ul>
 * </p>
 *
 * @author xiaomi
 * @since 2025/7/20 23:01
 */
@SuppressWarnings({"unchecked"})
public class HTTPClient extends Request implements HTTPConstantsInterface {

    /**
     * 构造HTTP客户端实例
     *
     * @param method HTTP方法（GET、POST等）
     * @param url    请求URL
     */
    protected HTTPClient(String method, String url) {
        super(method, url);
    }

    /**
     * 根据HTTP配置项构建HTTP请求对象
     * <p>
     * 该方法会根据配置项中的协议、主机、端口和路径构建完整的请求URL，
     * 并设置请求头、Cookie、查询参数和请求体等信息。
     * </p>
     *
     * @param config HTTP配置项
     * @return HTTP请求对象
     */
    public static Request build(HTTPConfigureItem config) {
        var port = StringUtils.isNotBlank(config.getPort()) ? ":" + config.getPort() : "";
        var path = Strings.CS.startsWith(config.getPath(), "/") ? config.getPath() : "/" + config.getPath();
        var url = "%s://%s%s%s".formatted(config.getProtocol(), config.getHost(), port, path);
        // bytes body data(binary) 不会同时出现
        return new HTTPClient(config.getMethod(), url)
                .headers(config.getHeaders())
                .cookie(config.getCookie())
                .query(config.getQuery())
                // bytes body data(binary) 不会同时出现
                .bytes(config.getBytes(), config.getHeaders())
                .body(config.getBody())
                .body(config.getBinary(), config.getData())
                .version(config.isHttp2() ? HTTP_2 : HTTP_1_1);
    }

    /**
     * 设置查询参数
     *
     * @param query 查询参数映射
     * @return HTTP客户端实例
     */
    public HTTPClient query(Map<String, ?> query) {
        if (query != null && !query.isEmpty()) {
            super.query(Form.create((Map<String, Object>) query));
        }
        return this;
    }

    /**
     * 设置字节数据请求体
     * <p>
     * 当使用字节数据作为请求体时，必须在请求头中指定Content-Type。
     * </p>
     *
     * @param bytes   字节数据
     * @param headers 请求头映射
     * @return HTTP客户端实例
     * @throws IllegalArgumentException 当使用字节数据但未指定Content-Type时抛出
     */
    public HTTPClient bytes(byte[] bytes, Map<String, String> headers) {
        if (Objects.nonNull(bytes) && bytes.length > 0) {
            if (Objects.isNull(headers) || headers.isEmpty() || StringUtils.isBlank(headers.get("Content-Type"))) {
                throw new IllegalArgumentException("使用bytes作为请求数据时， 请求头必须添加 Content-Type");
            }
            super.body(RequestEntity.bytes(bytes, headers.get("Content-Type")));
        }
        return this;
    }

    /**
     * 设置请求体
     * <p>
     * 根据请求体对象的类型自动选择合适的格式：
     * <ul>
     *   <li>Map或List类型：转换为JSON格式</li>
     *   <li>其他类型：转换为文本格式</li>
     * </ul>
     * </p>
     *
     * @param body 请求体对象
     * @return HTTP客户端实例
     */
    public HTTPClient body(Object body) {
        if (body instanceof Map<?, ?> || body instanceof List<?>) {
            super.body(RequestEntity.json(JSON.toJSONString(body)));
        } else if (Objects.nonNull(body)) {
            super.body(RequestEntity.text(body.toString()));
        }
        return this;
    }

    /**
     * 设置二进制文件请求体或表单数据
     * <p>
     * 支持两种场景：
     * <ul>
     *   <li>binary参数：上传文件列表</li>
     *   <li>data参数：表单数据</li>
     * </ul>
     * </p>
     *
     * @param binary 上传文件数据
     * @param data   表单数据
     * @return HTTP客户端实例
     */
    public HTTPClient body(Object binary, Map<String, ?> data) {
        if (Objects.nonNull(binary)) {
            var files = new ArrayList<NameValuePair>();
            if (binary instanceof List<?> objects) {
                var jsonArray = new JSONArray(objects);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    for (String key : item.keySet()) {
                        files.add(new BasicNameValuePair(key, item.getString(key)));
                    }
                }
            }
            if (binary instanceof Map<?, ?> map) {
                var json = new JSONObject(map);
                for (Map.Entry<String, Object> en : json.entrySet()) {
                    Object value = en.getValue();
                    if (value instanceof String path) {
                        files.add(new BasicNameValuePair(en.getKey(), path));
                    } else if (value instanceof List<?> paths) {
                        paths.forEach(path -> files.add(new BasicNameValuePair(en.getKey(), (String) path)));
                    }
                }
            }
            // 如果 binary 非空，则认为 data 为 binary 中的消息
            super.body(RequestEntity.binary(files, (Map<String, Object>) data));
        } else if (Objects.nonNull(data)) {
            super.body(RequestEntity.form((Map<String, Object>) data));
        }
        return this;
    }

    /**
     * 设置请求头
     *
     * @param headers 请求头映射
     * @return HTTP客户端实例
     */
    public HTTPClient headers(Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return this;
        }
        headers.entrySet().stream().filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .forEach(entry -> addHeader(entry.getKey(), entry.getValue()));
        return this;
    }

    /**
     * 设置Cookie
     *
     * @param cookie Cookie映射，包含name、value、domain、path等信息
     * @return HTTP客户端实例
     */
    public HTTPClient cookie(Map<String, String> cookie) {
        if (cookie == null || cookie.isEmpty()) {
            return this;
        }
        var localCookie = new BasicClientCookie(cookie.get(COOKIE_NAME), cookie.get(COOKIE_VALUE));
        localCookie.setPath(cookie.get(COOKIE_PATH));
        localCookie.setDomain(cookie.get(COOKIE_DOMAIN));
        addCookie(localCookie);
        return this;
    }

}