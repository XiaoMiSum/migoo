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

package coder.xyz.migoo.protocol

import coder.xyz.migoo.El
import com.google.common.collect.Maps

class Protocol {

    static class HTTP extends El {

        private HTTP(HTTP _def) {
            p(_def.customize())
        }

        HTTP(String protocol = "http", String host, int port = 80, boolean v2 = false) {
            p("protocol", protocol)
            p("host", host)
            p("port", port)
            p("http/2", v2)
        }

        static HTTP copy(HTTP _def) {
            return new HTTP(_def)
        }

        HTTP withGet(String path, Map<String, Object> query = null, Map<String, Object> headers = null) {
            return withApi("get", path, query, null, null, null, headers)
        }

        HTTP withPostData(String path, Map<String, Object> data, Map<String, Object> headers = null) {
            return withPost(path, data, null, null, headers)
        }

        HTTP withPostBody(String path, Map<String, Object> body = null, Map<String, Object> headers = null) {
            return withPost(path, null, body, null, headers)
        }

        HTTP withPostBytes(String path, byte[] bytes = null, Map<String, Object> headers = null) {
            return withPost(path, null, null, bytes, headers)
        }

        HTTP withPost(String path, Map<String, Object> data = null, Map<String, Object> body = null, byte[] bytes = null,
                      Map<String, Object> headers = Maps.newHashMap()) {
            return withApi("post", path, null, data, body, bytes, headers)
        }

        private HTTP withApi(String method, String path, Map<String, Object> query,
                             Map<String, Object> data, Map<String, Object> body, byte[] bytes, Map<String, Object> headers) {
            p("method", method)
            p("path", path)
            p("headers", headers)
            p("query", query)
            p("data", data)
            p("body", body)
            p("bytes", bytes)
            return this
        }
    }

    static class Redis extends El {

        private Redis(Redis _def) {
            p(_def.customize())
        }

        Redis(String datasource, String command, String send) {
            p("datasource", datasource)
            p("command", command)
            p("send", send)
        }

        Redis(String name, String host, int port = 6379, int database = 0, String password = null, int timeout = 60, int minIdle = 1,
              int maxIdle = 5, int maxTotal = 5) {
            p("variable_name", name)
            p("host", host)
            p("port", port)
            p("database", database)
            p("password", password)
            p("time_out", timeout * 1000)
            p("min_idle", minIdle)
            p("max_idle", maxIdle)
            p("max_total", maxTotal)
        }

        static Redis copy(Redis _def) {
            return new Redis(_def)
        }
    }

    static class JDBC extends El {

        JDBC(JDBC _def) {
            p(_def.customize())
        }

        JDBC(String datasource, String queryType, String statement) {
            p("datasource", datasource)
            p("queryType", queryType)
            p("statement", statement)
        }

        JDBC(String name, String url, String username, String password, int maxWait = 1, int maxActive = 5) {
            p("variable_name", name)
            p("url", url)
            p("username", username)
            p("password", password)
            p("max_wait", maxWait)
            p("max_active", maxActive)
        }

        static JDBC copy(JDBC _def) {
            return new JDBC(_def)
        }
    }
}
