/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;
import com.google.common.collect.Maps;

import java.util.Map;

public class Protocol {

    public static class HTTP extends El {

        public HTTP(HTTP def) {
            p(def.customize());
        }

        public HTTP(String host) {
            this("http", host, 80, false);
        }

        public HTTP(String protocol, String host) {
            this(protocol, host, 80, false);
        }

        public HTTP(String protocol, String host, int port) {
            this(protocol, host, port, false);
        }

        public HTTP(String protocol, String host, int port, boolean v2) {
            p("protocol", host);
            p("host", host);
            p("port", port);
            p("http/2", v2);
        }

        public static HTTP copy(HTTP def) {
            return new HTTP(def);
        }

        public HTTP withGet(String path) {
            return withGet(path, null, Maps.newHashMap());
        }

        public HTTP withGet(String path, Map<String, Object> query) {
            return withGet(path, query, Maps.newHashMap());
        }

        public HTTP withGet(String path, Map<String, Object> query, Map<String, Object> headers) {
            return withApi("get", path, query, null, null, null, headers);
        }

        public HTTP withPostData(String path, Map<String, Object> data) {
            return withPostData(path, data, Maps.newHashMap());
        }

        public HTTP withPostData(String path, Map<String, Object> data, Map<String, Object> headers) {
            return withPost(path, headers, data, null, null);
        }

        public HTTP withPostBody(String path, Map<String, Object> body) {
            return withPostBody(path, body, Maps.newHashMap());
        }

        public HTTP withPostBody(String path, Map<String, Object> body, Map<String, Object> headers) {
            return withPost(path, null, body, null, headers);
        }

        public HTTP withPostBytes(String path, byte[] bytes) {
            return withPostBytes(path, bytes, Maps.newHashMap());
        }

        public HTTP withPostBytes(String path, byte[] bytes, Map<String, Object> headers) {
            return withPost(path, null, null, bytes, headers);
        }

        HTTP withPost(String path, Map<String, Object> data, Map<String, Object> body, byte[] bytes, Map<String, Object> headers) {
            return withApi("post", path, null, data, body, bytes, headers);
        }

        HTTP withApi(String method, String path, Map<String, Object> query,
                     Map<String, Object> data, Map<String, Object> body, byte[] bytes, Map<String, Object> headers) {
            p("method", method);
            p("path", path);
            p("headers", headers);
            p("query", query);
            p("data", data);
            p("body", body);
            p("bytes", bytes);
            return this;
        }
    }


    public static class Redis extends El {
        public Redis(Redis def) {
            p(def.customize());
        }

        public Redis(String datasource, String command, String send) {
            p("datasource", datasource);
            p("command", command);
            p("send", send);
        }

        public Redis(String name, String host) {
            this(name, host, 6379, null);
        }

        public Redis(String name, String host, int port, String password) {
            this(name, host, port, 0, password);
        }


        public Redis(String name, String host, int port, int database, String password) {
            this(name, host, port, database, password, 60, 1, 5, 5);
        }

        public Redis(String name, String host, int port, int database, String password, int timeout, int minIdle,
                     int maxIdle, int maxTotal) {
            p("variable_name", name);
            p("host", host);
            p("port", port);
            p("database", database);
            p("password", password);
            p("time_out", timeout * 1000);
            p("min_idle", minIdle);
            p("max_idle", maxIdle);
            p("max_total", maxTotal);
        }

        public static Redis copy(Redis def) {
            return new Redis(def);
        }
    }


    public static class JDBC extends El {
        public JDBC(JDBC def) {
            p(def.customize());
        }

        public JDBC(String datasource, String queryType, String statement) {
            p("datasource", datasource);
            p("queryType", queryType);
            p("statement", statement);
        }

        public JDBC(String name, String url, String username, String password) {
            this(name, url, username, password, 1, 5);
        }

        public JDBC(String name, String url, String username, String password, int maxWait, int maxActive) {
            p("variable_name", name);
            p("url", url);
            p("username", username);
            p("password", password);
            p("max_wait", maxWait);
            p("max_active", maxActive);
        }

        public static JDBC copy(JDBC def) {
            return new JDBC(def);
        }
    }
}
