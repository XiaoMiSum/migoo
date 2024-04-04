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

public class Processor extends El {


    Processor(String testClass, El protocol, Extractor... extractors) {
        super(testClass);
        p(protocol.customize());
        if (Objects.nonNull(extractors) && extractors.length > 0) {
            List<Map<String, Object>> extractor = Lists.newArrayList();
            for (Extractor item : extractors) {
                extractor.add(item.customize());
            }
            p("extractors", extractor);
        }
    }

    public static Processor withHttpPreprocessor(Protocol.HTTP config, Extractor... extractors) {
        return with("HttpPreProcessor", config, extractors);
    }

    public static Processor withHttpPostprocessor(Protocol.HTTP config, Extractor... extractors) {
        return with("HttpPostProcessor", config, extractors);
    }

    public static Processor withRedisPreprocessor(Protocol.Redis config, Extractor... extractors) {
        return with("RedisPreProcessor", config, extractors);
    }

    public static Processor withRedisPostprocessor(Protocol.Redis config, Extractor... extractors) {
        return with("RedisPostProcessor", config, extractors);
    }

    public static Processor withJDBCPreProcessor(Protocol.JDBC config, Extractor... extractors) {
        return with("JDBCPreProcessor", config, extractors);
    }

    public static Processor withJDBCPostProcessor(Protocol.JDBC config, Extractor... extractors) {
        return with("JDBCPostProcessor", config, extractors);
    }

    private static Processor with(String testClass, El config, Extractor... extractors) {
        return new Processor(testClass, config, extractors);
    }

}
