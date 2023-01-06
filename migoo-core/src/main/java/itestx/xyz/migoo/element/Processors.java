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

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author xiaomi
 * Created at 2022/8/23 19:46
 */
public class Processors {

    public static final Pre pre = new Pre();

    public static final Post post = new Post();

    public static class Pre {

        private Pre() {
        }

        public HttpProcessor httpProcessor() {
            return new HttpProcessor("HttpPreProcessor");
        }

        public JDBCProcessor jdbcProcessor() {
            return new JDBCProcessor("JDBCPreProcessor");
        }

        public RedisProcessor redisProcessor() {
            return new RedisProcessor("RedisPreProcessor");
        }
    }

    public static class Post {

        private Post() {
        }

        public HttpProcessor httpProcessor() {
            return new HttpProcessor("HttpPostProcessor");
        }

        public JDBCProcessor jdbcProcessor() {
            return new JDBCProcessor("JDBCPostProcessor");
        }

        public RedisProcessor redisProcessor() {
            return new RedisProcessor("RedisPostProcessor");
        }
    }

    public static class HttpProcessor extends Processors implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private HttpProcessor(String clazz) {
            props.put("testclass", clazz);
            props.put("protocol", "http");
            props.put("method", "get");
            props.put("headers", new LinkedHashMap<>(16));
            props.put("extractors", new Vector<>());
        }

        public HttpProcessor https() {
            props.put("protocol", "https");
            return this;
        }

        public HttpProcessor method(String method) {
            props.put("method", method);
            return this;
        }

        public HttpProcessor host(String host) {
            props.put("host", host);
            return this;
        }

        public HttpProcessor port(int port) {
            props.put("port", port);
            return this;
        }

        public HttpProcessor api(String api) {
            props.put("api", api);
            return this;
        }

        public HttpProcessor addHeader(String name, String value) {
            Map<String, Object> header = (Map<String, Object>) props.get("headers");
            header.put(name, value);
            return this;
        }

        public HttpProcessor bytes(byte[] bytes) {
            props.put("bytes", bytes);
            return this;
        }

        public HttpProcessor body(Map<String, Object> body) {
            props.put("body", body);
            return this;
        }

        public HttpProcessor data(Map<String, Object> data) {
            props.put("data", data);
            return this;
        }

        public HttpProcessor query(Map<String, Object> query) {
            props.put("query", query);
            return this;
        }

        public HttpProcessor addExtractor(Extractors... extractor) {
            Vector<Extractors> vector = (Vector<Extractors>) props.get("extractors");
            vector.addAll(Arrays.asList(extractor));
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class JDBCProcessor extends Processors implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private JDBCProcessor(String clazz) {
            props.put("testclass", clazz);
            props.put("extractors", new Vector<>());
        }

        public JDBCProcessor datasource(String datasource) {
            props.put("datasource", datasource);
            return this;
        }

        public JDBCProcessor queryType(String queryType) {
            props.put("query_type", queryType);
            return this;
        }

        public JDBCProcessor statement(String statement) {
            props.put("statement", statement);
            return this;
        }

        public JDBCProcessor addExtractor(Extractors... extractor) {
            Vector<Extractors> vector = (Vector<Extractors>) props.get("extractors");
            vector.addAll(Arrays.asList(extractor));
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class RedisProcessor extends Processors implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private RedisProcessor(String clazz) {
            props.put("testclass", clazz);
            props.put("extractors", new Vector<>());
        }

        public RedisProcessor datasource(String datasource) {
            props.put("datasource", datasource);
            return this;
        }

        public RedisProcessor command(String command) {
            props.put("command", command);
            return this;
        }

        public RedisProcessor send(String send) {
            props.put("send", send);
            return this;
        }

        public RedisProcessor addExtractor(Extractors... extractor) {
            Vector<Extractors> vector = (Vector<Extractors>) props.get("extractors");
            vector.addAll(Arrays.asList(extractor));
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }
}
