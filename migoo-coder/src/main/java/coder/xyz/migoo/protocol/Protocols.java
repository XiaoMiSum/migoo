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

public class Protocols {

    public static HTTP http() {
        return new HTTP("http");
    }

    public static HTTP https() {
        return new HTTP("https");
    }

    public static HTTP copy(HTTP copy) {
        HTTP http = new HTTP();
        http.properties().putAll(copy.properties());
        return http;
    }

    public static JDBC jdbc(String name, String url, String username, String password) {
        return new JDBC(name).url(url).username(username).password(password);
    }

    public static JDBC jdbc(String datasource, String statement) {
        return new JDBC().datasource(datasource).statement(statement);
    }

    public static Redis redis2(String name, String host, String password) {
        return new Redis(name).host(host).password(password);
    }

    public static Redis redis(String name, String host, int port, String password) {
        return new Redis(name).host(host).port(port).password(password);
    }

    public static Redis redis(String datasource, String command, String send) {
        return new Redis().datasource(datasource).command(command).send(send);
    }

    public static Dubbo dubbo() {
        return new Dubbo();
    }

    public static Dubbo copy(Dubbo copy) {
        Dubbo dubbo = new Dubbo();
        dubbo.properties().putAll(copy.properties());
        return dubbo;
    }

    public static ActiveMQ activeMQ() {
        return new ActiveMQ();
    }

    public static ActiveMQ copy(ActiveMQ copy) {
        ActiveMQ active = new ActiveMQ();
        active.properties().putAll(copy.properties());
        return active;
    }

    public static Kafka kafka() {
        return new Kafka();
    }

    public static Kafka kafka(Kafka copy) {
        Kafka kafka = new Kafka();
        kafka.properties().putAll(copy.properties());
        return kafka;
    }

}
