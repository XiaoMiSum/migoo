package xyz.migoo.framework.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * HttpClient HttpDelete 不支持设置 body
 * 重新实现一个支持 设置body 的HttpDelete
 * @author xiaomi
 * @date 2018/10/10 20:35
 */
public class HttpDelete extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDelete(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDelete() {
        super();
    }
}
