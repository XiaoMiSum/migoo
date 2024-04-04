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

package coder.xyz.migoo;

import coder.xyz.migoo.protocol.Protocol;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Sampler extends El {

    Sampler(String title, String testClass, Map<String, Object> variables, El config, Validator[] validators,
            Processor[] preprocessors, Processor[] postprocessors, Extractor[] extractors) {
        super(testClass);
        p("title", title);
        p("variables", variables);
        p("config", config.customize());
        el("validators", validators);
        el("preprocessors", preprocessors);
        el("postprocessors", postprocessors);
        el("extractors", extractors);
    }

    public static Sampler withHttpSampler(String title, Protocol.HTTP config) {
        return withHttpSampler(title, null, config);
    }

    public static Sampler withHttpSampler(String title, Map<String, Object> variables, Protocol.HTTP config) {
        return withHttpSampler(title, variables, config, null);
    }

    public static Sampler withHttpSampler(String title, Map<String, Object> variables, Protocol.HTTP config, Validator[] validators) {
        return withHttpSampler(title, variables, config, validators, null);
    }

    public static Sampler withHttpSampler(String title, Map<String, Object> variables, Protocol.HTTP config, Validator[] validators,
                                          Extractor[] extractors) {
        return withHttpSampler(title, variables, config, validators, null, null, extractors);
    }

    public static Sampler withHttpSampler(String title, Map<String, Object> variables, Protocol.HTTP config, Validator[] validators,
                                          Processor[] preprocessors, Processor[] postprocessors, Extractor[] extractors) {
        return new Sampler(title, "httpSampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    public static Sampler withJDBCSampler(String title, Map<String, Object> variables, Protocol.JDBC config) {
        return withJDBCSampler(title, variables, config, null);
    }

    public static Sampler withJDBCSampler(String title, Map<String, Object> variables, Protocol.JDBC config, Validator[] validators) {
        return withJDBCSampler(title, variables, config, validators, null);
    }

    public static Sampler withJDBCSampler(String title, Map<String, Object> variables, Protocol.JDBC config, Validator[] validators,
                                          Extractor[] extractors) {
        return withJDBCSampler(title, variables, config, validators, null, null, extractors);
    }

    public static Sampler withJDBCSampler(String title, Map<String, Object> variables, Protocol.JDBC config, Validator[] validators,
                                          Processor[] preprocessors, Processor[] postprocessors, Extractor[] extractors) {
        return new Sampler(title, "JDBCSampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    public static Sampler withRedisSampler(String title, Map<String, Object> variables, Protocol.Redis config) {
        return withRedisSampler(title, variables, config, null);
    }

    public static Sampler withRedisSampler(String title, Map<String, Object> variables, Protocol.Redis config, Validator[] validators) {
        return withRedisSampler(title, variables, config, validators, null);
    }

    public static Sampler withRedisSampler(String title, Map<String, Object> variables, Protocol.Redis config, Validator[] validators,
                                           Extractor[] extractors) {
        return withRedisSampler(title, variables, config, validators, null, null, extractors);
    }

    public static Sampler withRedisSampler(String title, Map<String, Object> variables, Protocol.Redis config, Validator[] validators,
                                           Processor[] preprocessors, Processor[] postprocessors, Extractor[] extractors) {
        return new Sampler(title, "RedisSampler", variables, config, validators, preprocessors, postprocessors, extractors);
    }

    private void el(String key, El[] els) {
        if (Objects.nonNull(els) && els.length > 0) {
            List<Map<String, Object>> v = Lists.newArrayList();
            for (El el : els) {
                v.add(el.customize());
            }
            p(key, v);
        }
    }

}
