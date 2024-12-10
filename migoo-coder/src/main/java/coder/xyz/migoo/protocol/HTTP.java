package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

import java.util.Map;

public class HTTP extends El {
    HTTP() {
    }

    HTTP(String protocol) {
        p("protocol", protocol);
    }

    public HTTP protocol(String protocol) {
        p("protocol", protocol);
        return this;
    }

    public HTTP host(String host) {
        p("host", host);
        return this;
    }

    public HTTP port(String port) {
        p("port", port);
        return this;
    }

    public HTTP port(int port) {
        p("port", port);
        return this;
    }

    public HTTP version2() {
        p("http/2", true);
        return this;
    }

    public HTTP headers(Map<String, Object> headers) {
        p("headers", headers);
        return this;
    }

    public HTTP method(String method) {
        p("method", method);
        return this;
    }

    public HTTP get() {
        p("method", "get");
        return this;
    }

    public HTTP post() {
        p("method", "post");
        return this;
    }

    public HTTP put() {
        p("method", "put");
        return this;
    }

    public HTTP delete() {
        p("method", "delete");
        return this;
    }

    public HTTP path(String path) {
        p("path", path);
        return this;
    }

    public HTTP query(Map<String, Object> query) {
        p("query", query);
        return this;
    }

    public HTTP data(Map<String, Object> data) {
        p("data", data);
        return this;
    }

    public HTTP body(Map<String, Object> body) {
        p("body", body);
        return this;
    }

    public HTTP bytes(byte[] bytes) {
        p("bytes", bytes);
        return this;
    }
}
