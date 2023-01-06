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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xiaomi
 * Created at 2022/8/23 19:46
 */
public class Extractors {

    private static final Builder builder = new Builder();

    public static Builder builder() {
        return builder;
    }

    public static class Builder {

        public JSONExtractor json(String name) {
            return new JSONExtractor(name);
        }

        public RegexExtractor regex(String name) {
            return new RegexExtractor(name);
        }

        public ResultExtractor result(String name) {
            return new ResultExtractor(name);
        }
    }

    public static class JSONExtractor extends Extractors implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private JSONExtractor(String name) {
            props.put("testclass", "JSONExtractor");
            props.put("variable_name", name);
        }

        public JSONExtractor jsonPath(String field) {
            props.put("field", field);
            return this;
        }

        public JSONExtractor matchNo(int matchNo) {
            props.put("match_num", matchNo);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class RegexExtractor extends Extractors implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private RegexExtractor(String name) {
            props.put("testclass", "RegexExtractor");
            props.put("match_num", 0);
            props.put("variable_name", name);
        }

        public RegexExtractor regex(String field) {
            props.put("field", field);
            return this;
        }

        public RegexExtractor matchNo(int matchNo) {
            props.put("match_num", matchNo);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class ResultExtractor extends Extractors implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private ResultExtractor(String name) {
            props.put("testclass", "RegexExtractor");
            props.put("variable_name", name);
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }
}
