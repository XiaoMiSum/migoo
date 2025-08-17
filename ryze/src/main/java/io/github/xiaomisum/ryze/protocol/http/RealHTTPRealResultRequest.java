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

package io.github.xiaomisum.ryze.protocol.http;

import org.apache.commons.lang3.StringUtils;
import xyz.migoo.simplehttp.Request;

import java.util.Arrays;

/**
 * @author xiaomi
 */
public class RealHTTPRealResultRequest extends HTTPRealResult {

    private final String url;

    private final String method;

    private final String query;

    private final byte[] body;


    public RealHTTPRealResultRequest(Request request) {
        super(new byte[0]);
        url = request.uri();
        method = request.method();
        query = request.query();
        body = request.body();
        version = request.version();
        headers = Arrays.stream(request.headers()).toList();
    }


    @Override
    public byte[] bytes() {
        return body != null && body.length > 0 ? body : query != null ? query.getBytes() : super.bytes();
    }

    @Override
    public String bytesAsString() {
        return new String(bytes());
    }

    /**
     * 设置 HTTP请求信息，格式如下
     * <p>
     * POST /contact_form.php HTTP/1.1
     * Host: developer.mozilla.org
     * Content-Length: 64
     * Content-Type: application/x-www-form-urlencoded
     */
    @Override
    public String format() {
        var buf = new StringBuilder();
        buf.append(method).append(" ").append(url).append(" ").append(version);
        header(buf);
        if (StringUtils.isNotBlank(query)) {
            buf.append("\n").append("Request Query:").append(query);
        }
        if (body != null && body.length > 0) {
            buf.append("\n").append("Request Body:").append(bytesAsString());
        }
        return buf.toString();
    }
}
