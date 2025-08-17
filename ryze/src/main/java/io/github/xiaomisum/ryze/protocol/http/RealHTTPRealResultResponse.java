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
import xyz.migoo.simplehttp.Response;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author xiaomi
 */
public class RealHTTPRealResultResponse extends HTTPRealResult {

    private int statusCode;

    private String message;


    public RealHTTPRealResultResponse(Response response) {
        super(Objects.isNull(response) ? new byte[0] : response.bytes());
        if (Objects.isNull(response)) {
            return;
        }
        statusCode = response.statusCode();
        version = response.version();
        message = response.message();
        headers = Arrays.stream(response.headers()).toList();
    }


    /**
     * 设置 HTTP响应信息，格式如下
     * <p>
     * HTTP/1.1 200 OK
     * Content-Type: text/html; charset=utf-8
     * Content-Length: 55743
     * Connection: keep-alive
     * Cache-Control: s-maxage=300, public, max-age=0
     * Content-Language: en-US
     * Date: Thu, 06 Dec 2018 17:37:18 GMT
     * ETag: "2e77ad1dc6ab0b53a2996dfd4653c1c3"
     * Server: meinheld/0.6.1
     * Strict-Transport-Security: max-age=63072000
     * X-Content-Type-Options: nosniff
     * X-Frame-Options: DENY
     * X-XSS-Protection: 1; mode=block
     * Vary: Accept-Encoding,Cookie
     * Age: 7
     * <p>
     * <!DOCTYPE html>
     * <html lang="en">
     * <head>
     * <meta charset="utf-8">
     * <title>A simple webpage</title>
     * </head>
     * <body>
     * <h1>Simple HTML webpage</h1>
     * <p>Hello, world!</p>
     * </body>
     * </html>
     */
    @Override
    public String format() {
        var buf = new StringBuilder();
        buf.append(version).append(" ").append(statusCode).append(" ").append(message);
        header(buf);
        if (StringUtils.isNotBlank(bytesAsString())) {
            buf.append("\n").append("Response body: ").append(bytesAsString());
        }
        return buf.toString();
    }

    public int statusCode() {
        return statusCode;
    }
}
