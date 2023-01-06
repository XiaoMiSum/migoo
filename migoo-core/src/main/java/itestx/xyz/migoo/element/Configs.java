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

package itestx.xyz.migoo.element;

import itestx.xyz.migoo.logic.ITestx;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiaomi
 * Created at 2022/8/23 19:44
 */
public class Configs {

    private static final Builder builder = new Builder();

    public static Builder builder() {
        return builder;
    }

    public static class Builder {

        public HttpDefault httpDefault() {
            return new HttpDefault();
        }

        public JDBCDataSource jdbcDataSource(String name) {
            return new JDBCDataSource(name);
        }

        public RedisDataSource redisDataSource(String name) {
            return new RedisDataSource(name);
        }
    }

    public static class HttpDefault extends Configs implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private HttpDefault() {
            props.put("testclass", "HttpDefault");
            props.put("protocol", "http");
            props.put("headers", new HashMap<>(16));
            props.put("method", "get");
        }

        public HttpDefault https() {
            props.put("protocol", "https");
            return this;
        }

        public HttpDefault method(String method) {
            props.put("method", method);
            return this;
        }

        public HttpDefault host(String host) {
            props.put("host", host);
            return this;
        }

        public HttpDefault port(int port) {
            props.put("port", port);
            return this;
        }

        public HttpDefault addHeader(String name, String value) {
            Map<String, Object> header = (Map<String, Object>) props.get("headers");
            header.put(name, value);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class JDBCDataSource extends Configs implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private JDBCDataSource(String name) {
            props.put("testclass", "jdbcdatasource");
            props.put("max_active", 5);
            props.put("max_wait", 1);
            props.put("variable_name", name);
        }

        public JDBCDataSource url(String url) {
            props.put("url", url);
            return this;
        }

        public JDBCDataSource username(String username) {
            props.put("username", username);
            return this;
        }

        public JDBCDataSource password(String password) {
            props.put("password", password);
            return this;
        }

        public JDBCDataSource maxActive(int maxActive) {
            props.put("max_active", maxActive);
            return this;
        }

        public JDBCDataSource maxWait(int maxWait) {
            props.put("max_wait", maxWait);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class RedisDataSource extends Configs implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private RedisDataSource(String name) {
            props.put("testclass", "RedisDataSource");
            props.put("time_out", 60 * 1000);
            props.put("max_total", 5);
            props.put("max_idle", 5);
            props.put("min_idle", 1);
            props.put("database", 0);
            props.put("port", 6379);
            props.put("variable_name", name);
        }

        public RedisDataSource host(String host) {
            props.put("host", host);
            return this;
        }

        public RedisDataSource port(int port) {
            props.put("port", port);
            return this;
        }

        public RedisDataSource database(int database) {
            props.put("database", database);
            return this;
        }

        public RedisDataSource password(String password) {
            props.put("password", password);
            return this;
        }

        public RedisDataSource timeOut(int seconds) {
            props.put("time_out", seconds * 1000);
            return this;
        }

        public RedisDataSource maxTotal(int maxTotal) {
            props.put("max_total", maxTotal);
            return this;
        }

        public RedisDataSource maxIdle(int maxIdle) {
            props.put("max_idle", maxIdle);
            return this;
        }

        public RedisDataSource minIdle(int minIdle) {
            props.put("min_idle", minIdle);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }
}
