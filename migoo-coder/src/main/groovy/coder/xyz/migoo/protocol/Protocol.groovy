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

        private HTTP(String protocol = "http", String method, String host, int port = 80, boolean v2 = false,
                     String path = null, Map headers = null) {
            p("protocol", protocol)
            p("host", host)
            p("port", port)
            p("http/2", v2)
            p("path", path)
            p("method", method)
            p("headers", headers)
        }

        static HTTP copy(HTTP _def) {
            return new HTTP(_def)
        }

        static HTTP withConfig(String protocol = "http", String method, String host, int port = 80, boolean v2 = false,
                               String path = null, Map headers = null) {
            return new HTTP(protocol, method, host, port, v2, path, headers)
        }

        static HTTP withGet(HTTP config = null, String path, Map query = null, Map headers = null) {
            return withApi(config, "get", path, query, null, null, null, headers)
        }

        static HTTP withPostData(HTTP config = null, String path, Map data, Map headers = null) {
            return withPost(config, path, data, null, null, headers)
        }

        static HTTP withPostBody(HTTP config = null, String path, Map body = null, Map headers = null) {
            return withPost(config, path, null, body, null, headers)
        }

        static HTTP withPostBytes(HTTP config = null, String path, byte[] bytes = null, Map headers = null) {
            return withPost(config, path, null, null, bytes, headers)
        }

        static HTTP withPost(HTTP config = null, String path, Map data = null, Map body = null,
                             byte[] bytes = null, Map headers = Maps.newHashMap()) {
            return withApi(config, "post", path, null, data, body, bytes, headers)
        }

        private static HTTP withApi(HTTP config = null, String method, String path, Map query, Map data,
                                    Map body, byte[] bytes, Map headers) {
            def http = new HTTP()
            def header = config.customize().get("headers") as Map
            if (Objects.nonNull(config)) {
                http.p(config.customize())
            }
            if (Objects.nonNull(headers)) {
                if (Objects.nonNull(header)) {
                    headers.putAll(header)
                }
                http.p("headers", headers)
            }
            http.p("method", method)
            http.p("path", path)
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

    static class ActiveMQ extends El {

        private ActiveMQ(ActiveMQ _def) {
            p(_def.customize())
        }

        private ActiveMQ(String username, String password, String brokerUrl, String queue, String topic, def message) {
            p("username", username)
            p("password", password)
            p("broker.url", brokerUrl)
            p("queue", queue)
            p("topic", topic)
            p("message", message)
        }

        /**
         * 适用于配置ActiveMQ默认配置
         *
         * @param username 用户名
         * @param password 密码
         * @param brokerUrl broker url
         * @param queue 订阅模式 优先级高于 topic，根据实际选择使用
         * @param topic 广播模式
         * @return
         */
        static ActiveMQ withConfig(String username, String password, String brokerUrl, String queue = null,
                                   String topic = null) {
            return new ActiveMQ(username, password, brokerUrl, queue, topic, null)
        }

        /**
         * 适用于 处理器、取样器执行配置
         *
         * @param username 用户名
         * @param password 密码
         * @param brokerUrl broker url
         * @param queue 订阅模式 优先级高于 topic，根据实际选择使用
         * @param topic 广播模式
         * @param message 发送的消息，可以是任意json对象、字符串、数字等
         * @return
         */
        static ActiveMQ withApi(String username, String password, String brokerUrl, String queue, String topic, def message) {
            return new ActiveMQ(username, password, brokerUrl, queue, topic, message)
        }

        static ActiveMQ copy(ActiveMQ _def) {
            return new ActiveMQ(_def)
        }
    }

    static class Kafka extends El {

        private Kafka(Kafka _def) {
            p(_def.customize())
        }

        private Kafka(String bootstrapServers, String topic, String key, def message, String keySerializer,
                      String valueSerializer, Integer acks, Integer retries, Integer lingerMs) {
            p("bootstrap.servers", bootstrapServers)
            p("topic", topic)
            p("key", key)
            p("keySerializer", keySerializer)
            p("valueSerializer", valueSerializer)
            p("message", message)
            p("acks", acks)
            p("retries", retries)
            p("lingerMs", lingerMs)
        }

        /**
         * 适用于配置Kafka默认配置
         *
         * @param bootstrapServers 服务器地址
         * @param topic
         * @param key
         * @param keySerializer
         * @param valueSerializer
         * @param acks
         * @param retries
         * @param lingerMs
         * @return
         */
        static Kafka withConfig(String bootstrapServers, String topic = null, String key = null, String keySerializer = null,
                                String valueSerializer = null, Integer acks = null, Integer retries = null, Integer lingerMs = null) {
            return new Kafka(bootstrapServers, topic, key, null, keySerializer, valueSerializer, acks, retries, lingerMs)
        }

        /**
         * 适用于 处理器、取样器执行配置
         *
         * @param bootstrapServers 服务器地址
         * @param topic
         * @param key
         * @param keySerializer
         * @param valueSerializer
         * @param acks
         * @param retries
         * @param lingerMs
         * @param message 消息体
         * @return
         */
        static Kafka withApi(String bootstrapServers = null, String topic = null, String key = null, String keySerializer = null,
                             String valueSerializer = null, Integer acks = null, Integer retries = null, Integer lingerMs = null,
                             def message) {
            return new Kafka(bootstrapServers, topic, key, message, keySerializer, valueSerializer, acks, retries, lingerMs)
        }

        static Kafka copy(Kafka _def) {
            return new Kafka(_def)
        }
    }
}
