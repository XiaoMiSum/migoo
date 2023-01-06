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
 * Created at 2022/8/23 19:44
 */
public class Samplers {

    private static final Builder builder = new Builder();

    public static Builder builder() {
        return builder;
    }

    private static void addProps(Map<String, Object> props) {
        props.put("variables", new LinkedHashMap<>(16));
        props.put("PreProcessors", new Vector<>());
        props.put("extractors", new Vector<>());
        props.put("validators", new Vector<>());
        props.put("PostProcessors", new Vector<>());
    }

    public static class Builder {

        public HTTPSampler http(String title) {
            return new HTTPSampler("http", title);
        }

        public HTTPSampler https(String title) {
            return new HTTPSampler("https", title);
        }

        public JDBCSampler jdbc(String title) {
            return new JDBCSampler(title);
        }

        public RedisSampler redis(String title) {
            return new RedisSampler(title);
        }
    }

    public static class HTTPSampler extends Samplers implements ITestx {
        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private HTTPSampler(String protocol, String title) {
            props.put("title", title);
            props.put("testclass", "httpsampler");
            Map<String, Object> config = new LinkedHashMap<>(16);
            config.put("protocol", protocol);
            config.put("method", "get");
            config.put("headers", new LinkedHashMap<>(16));
            props.put("config", config);
            Samplers.addProps(props);
        }

        public HTTPSampler method(String method) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("method", method);
            return this;
        }

        public HTTPSampler host(String host) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("host", host);
            return this;
        }

        public HTTPSampler port(int port) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("port", port);
            return this;
        }

        public HTTPSampler api(String api) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("api", api);
            return this;
        }

        public HTTPSampler addHeader(String name, String value) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            Map<String, Object> header = (Map<String, Object>) config.get("headers");
            header.put(name, value);
            return this;
        }

        public HTTPSampler bytes(byte[] bytes) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("bytes", bytes);
            return this;
        }

        public HTTPSampler body(Map<String, Object> body) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("body", body);
            return this;
        }

        public HTTPSampler data(Map<String, Object> data) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("data", data);
            return this;
        }

        public HTTPSampler query(Map<String, Object> query) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("query", query);
            return this;
        }

        public HTTPSampler addVariable(String name, String value) {
            Map<String, Object> variables = (Map<String, Object>) props.get("variables");
            variables.put(name, value);
            return this;
        }

        public HTTPSampler addExtractor(Extractors... extractor) {
            Vector<Extractors> vector = (Vector<Extractors>) props.get("extractors");
            vector.addAll(Arrays.asList(extractor));
            return this;
        }

        public HTTPSampler addPreprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PreProcessors");
            vector.addAll(Arrays.asList(processor));
            return this;
        }

        public HTTPSampler addPostprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PostProcessors");
            vector.addAll(Arrays.asList(processor));
            return this;
        }

        public HTTPSampler addValidator(Validators... assertion) {
            Vector<Validators> vector = (Vector<Validators>) props.get("validators");
            vector.addAll(Arrays.asList(assertion));
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class JDBCSampler extends Samplers implements ITestx {
        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private JDBCSampler(String title) {
            props.put("title", title);
            props.put("testclass", "JDBCSampler");
            props.put("config", new LinkedHashMap<>(16));
            Samplers.addProps(props);
        }

        public JDBCSampler datasource(String datasource) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("datasource", datasource);
            return this;
        }

        public JDBCSampler queryType(String queryType) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("query_type", queryType);
            return this;
        }

        public JDBCSampler statement(String statement) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("statement", statement);
            return this;
        }

        public JDBCSampler addVariable(String name, String value) {
            Map<String, Object> variables = (Map<String, Object>) props.get("variables");
            variables.put(name, value);
            return this;
        }

        public JDBCSampler addExtractor(Extractors... extractor) {
            Vector<Extractors> vector = (Vector<Extractors>) props.get("extractors");
            vector.addAll(Arrays.asList(extractor));
            return this;
        }

        public JDBCSampler addPreprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PreProcessors");
            vector.addAll(Arrays.asList(processor));
            return this;
        }

        public JDBCSampler addPostprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PostProcessors");
            vector.addAll(Arrays.asList(processor));
            return this;
        }

        public JDBCSampler addValidator(Validators... assertion) {
            Vector<Validators> vector = (Vector<Validators>) props.get("validators");
            vector.addAll(Arrays.asList(assertion));
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class RedisSampler extends Samplers implements ITestx {
        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private RedisSampler(String title) {
            props.put("title", title);
            props.put("testclass", "RedisSampler");
            props.put("config", new LinkedHashMap<>(16));
            Samplers.addProps(props);
        }

        public RedisSampler datasource(String datasource) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("datasource", datasource);
            return this;
        }

        public RedisSampler command(String command) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("command", command);
            return this;
        }

        public RedisSampler send(String send) {
            Map<String, Object> config = (Map<String, Object>) props.get("config");
            config.put("send", send);
            return this;
        }

        public RedisSampler addVariable(String name, String value) {
            Map<String, Object> variables = (Map<String, Object>) props.get("variables");
            variables.put(name, value);
            return this;
        }

        public RedisSampler addExtractor(Extractors... extractor) {
            Vector<Extractors> vector = (Vector<Extractors>) props.get("extractors");
            vector.addAll(Arrays.asList(extractor));
            return this;
        }

        public RedisSampler addPreprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PreProcessors");
            vector.addAll(Arrays.asList(processor));
            return this;
        }

        public RedisSampler addPostprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PostProcessors");
            vector.addAll(Arrays.asList(processor));
            return this;
        }

        public RedisSampler addValidator(Validators... assertion) {
            Vector<Validators> vector = (Vector<Validators>) props.get("validators");
            vector.addAll(Arrays.asList(assertion));
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

}
