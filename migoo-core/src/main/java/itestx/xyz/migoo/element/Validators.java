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
 * Created at 2022/8/23 19:44
 */
public class Validators {

    private static final Builder builder = new Builder();

    public static Builder builder() {
        return builder;
    }

    public static class Builder {

        public HTTPResponseValidator httpResponse() {
            return new HTTPResponseValidator();
        }

        public JSONValidator json() {
            return new JSONValidator();
        }

        public ResultValidator result() {
            return new ResultValidator();
        }
    }


    public static class HTTPResponseValidator extends Validators implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private HTTPResponseValidator() {
            props.put("testclass", "HTTPAssertion");
            props.put("rule", "==");
            props.put("field", "status");
        }

        public HTTPResponseValidator field(String field) {
            props.put("field", field);
            return this;
        }

        public HTTPResponseValidator rule(String rule) {
            props.put("rule", rule);
            return this;
        }

        public HTTPResponseValidator expected(Object... expected) {
            props.put("expected", expected);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class JSONValidator extends Validators implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private JSONValidator() {
            props.put("testclass", "JSONAssertion");
            props.put("rule", "==");
        }

        public JSONValidator jsonPath(String jsonPath) {
            props.put("field", jsonPath);
            return this;
        }

        public JSONValidator rule(String rule) {
            props.put("rule", rule);
            return this;
        }

        public JSONValidator expected(Object... expected) {
            props.put("expected", expected);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }

    public static class ResultValidator extends Validators implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16);

        private ResultValidator() {
            props.put("testclass", "ResultAssertion");
            props.put("rule", "==");
        }

        public ResultValidator rule(String rule) {
            props.put("rule", rule);
            return this;
        }

        public ResultValidator expected(Object... expected) {
            props.put("expected", expected);
            return this;
        }

        @Override
        public Map<String, Object> get() {
            return props;
        }
    }
}
