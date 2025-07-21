package protocol.xyz.migoo.http;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;
import xyz.migoo.simplehttp.Response;

import java.util.Arrays;

public class RealHTTPResponse extends HTTP {

    private final int statusCode;

    private final String message;


    public RealHTTPResponse(Response response) {
        super(response.bytes());
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
        buf.append(version).append(" ").append(statusCode).append(" ").append(message).append("\n");
        header(buf);
        if (StringUtils.isNotBlank(bytesAsString())) {
            buf.append("BodyParams in JSON:").append(JSON.toJSONString(bytesAsString()));
        }
        return buf.toString();
    }

    public int statusCode() {
        return statusCode;
    }
}
