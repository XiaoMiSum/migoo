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

        private HTTP() {}

        private HTTP(String protocol = "http", String host, int port = 80, boolean v2 = false) {
            p("protocol", protocol)
            p("host", host)
            p("port", port)
            p("http/2", v2)
        }

        static HTTP copy(HTTP _def) {
            return new HTTP(_def)
        }

        static HTTP withConfig(String protocol = "http", String host, int port = 80, boolean v2 = false) {
            return new HTTP(protocol, host, port, v2)
        }

        static HTTP withGet(HTTP config = null, String path, Map<String, Object> query = null, Map<String, Object> headers = null) {
            return withApi(config, "get", path, query, null, null, null, headers)
        }

        static HTTP withPostData(HTTP config = null, String path, Map<String, Object> data, Map<String, Object> headers = null) {
            return withPost(config, path, data, null, null, headers)
        }

        static HTTP withPostBody(HTTP config = null, String path, Map<String, Object> body = null, Map<String, Object> headers = null) {
            return withPost(config, path, null, body, null, headers)
        }

        static HTTP withPostBytes(HTTP config = null, String path, byte[] bytes = null, Map<String, Object> headers = null) {
            return withPost(config, path, null, null, bytes, headers)
        }

        static HTTP withPost(HTTP config = null, String path, Map<String, Object> data = null, Map<String, Object> body = null,
                             byte[] bytes = null, Map<String, Object> headers = Maps.newHashMap()) {
            return withApi(config, "post", path, null, data, body, bytes, headers)
        }

        private static HTTP withApi(HTTP config = null, String method, String path, Map<String, Object> query, Map<String, Object> data,
                                    Map<String, Object> body, byte[] bytes, Map<String, Object> headers) {
            HTTP http = new HTTP()
            if (Objects.nonNull(config)) {
                http.p(config.customize())
            }
            http.p("method", method)
            http.p("path", path)
            http.p("headers", headers)
            http.p("query", query)
            http.p("data", data)
            http.p("body", body)
            http.p("bytes", bytes)
            return http
        }
    }

    static class Redis extends El {

        private Redis(Redis _def) {
            p(_def.customize())
        }

        private Redis(String datasource, String command, String send) {
            p("datasource", datasource)
            p("command", command)
            p("send", send)
        }

        private Redis(String name, String host, int port = 6379, int database = 0, String password = null,
                      int timeout = 60, int minIdle = 1, int maxIdle = 5, int maxTotal = 5) {
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

        /**
         * 适用于配置Redis默认配置
         *
         * @param name 数据源名称
         * @param host 连接地址
         * @param port 端口
         * @param database 数据库
         * @param password 密码
         * @param timeout 超时时间
         * @param minIdle
         * @param maxIdle
         * @param maxTotal
         * @return
         */
        static Redis withConfig(String name, String host, int port = 6379, int database = 0, String password = null,
                                int timeout = 60, int minIdle = 1, int maxIdle = 5, int maxTotal = 5) {
            return new Redis(name, host, port, database, password, timeout, minIdle, maxIdle, maxTotal)
        }

        /**
         * 适用于 处理器、取样器执行配置
         *
         * @param datasource 数据源名称
         * @param command 执行方法
         * @param send 语句
         * @return
         */
        static Redis withApi(String datasource, String command, String send) {
            return new Redis(datasource, command, send)
        }

        static Redis copy(Redis _def) {
            return new Redis(_def)
        }
    }

    static class JDBC extends El {

        private JDBC(JDBC _def) {
            p(_def.customize())
        }

        private JDBC(String datasource, String queryType, String statement) {
            p("datasource", datasource)
            p("queryType", queryType)
            p("statement", statement)
        }

        private JDBC(String name, String url, String username, String password, int maxWait = 1, int maxActive = 5) {
            p("variable_name", name)
            p("url", url)
            p("username", username)
            p("password", password)
            p("max_wait", maxWait)
            p("max_active", maxActive)
        }

        /**
         * 适用于配置JDBC默认配置
         *
         * @param name 数据源名称
         * @param url 连接地址
         * @param username 用户名
         * @param password 密码
         * @param maxWait 最大等待
         * @param maxActive 最大活动
         * @return
         */
        static JDBC withConfig(String name, String url, String username, String password, int maxWait = 1, int maxActive = 5) {
            return new JDBC(name, url, username, password, maxWait, maxActive)
        }

        /**
         * 适用于 处理器、取样器执行配置
         *
         * @param datasource 数据源名称
         * @param queryType 执行方法
         * @param statement sql语句
         * @return
         */
        static JDBC withApi(String datasource, String queryType, String statement) {
            return new JDBC(datasource, queryType, statement)
        }

        static JDBC copy(JDBC _def) {
            return new JDBC(_def)
        }
    }

    static class Dubbo extends El {

        private Dubbo(Dubbo _def) {
            p(_def.customize())
        }

        private Dubbo(Map registry, Map reference, String interfaceClass, String method, List parameterTypes,
                      List parameters, Map attachmentArgs) {
            p("registry", registry)
            p("reference", reference)
            p("interface", interfaceClass)
            p("method", method)
            p("parameter_types", parameterTypes)
            p("parameters", parameters)
            p("attachment_args", attachmentArgs)
        }

        /**
         * 适用于配置Dubbo默认配置
         *
         * @param registry 注册中心配置：protocol、address、username、password、version
         * @param reference reference配置：version、retries、group、timeout、async、load_balance
         * @return
         */
        static Dubbo withConfig(Map registry, Map reference) {
            return new Dubbo(registry, reference, null, null, null, null, null)
        }

        /**
         * 适用于 处理器、取样器执行配置
         *
         * @param registry 注册中心配置，可空
         * @param reference reference配置，可空
         * @param interfaceClass 接口类名全称
         * @param method 接口方法
         * @param parameterTypes 方法参数类型，根据接口定义
         * @param parameters 接口方法参数名称
         * @param attachmentArgs 附加参数 keyword形式，可空
         * @return
         */
        static Dubbo withApi(Map registry = null, Map reference = null, String interfaceClass, String method,
                             List parameterTypes = null, List parameters = null, Map attachmentArgs = null) {
            return new Dubbo(registry, reference, interfaceClass, method, parameterTypes, parameters, attachmentArgs)
        }

        static Dubbo copy(Dubbo _def) {
            return new Dubbo(_def)
        }
    }
}
