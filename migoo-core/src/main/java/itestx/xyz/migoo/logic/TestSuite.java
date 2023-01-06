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

package itestx.xyz.migoo.logic;

import core.xyz.migoo.variable.MiGooVariables;
import itestx.xyz.migoo.element.Configs;
import itestx.xyz.migoo.element.Processors;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author xiaomi
 * Created at 2022/8/23 19:47
 */
public class TestSuite implements ITestx {

    private final Map<String, Object> props = new LinkedHashMap<>(16);

    private TestSuite(String title, MiGooVariables variables, Vector<Configs> configs,
                      Vector<Processors> preprocessors, Vector<Processors> postprocessors, Vector<TestSets> children) {
        props.put("title", title);
        props.put("variables", variables);
        props.put("configelements", configs);
        props.put("preprocessors", preprocessors);
        props.put("postprocessors", postprocessors);
        props.put("child", children);
    }

    public static Builder builder(String title) {
        return new Builder(title);
    }

    @Override
    public Map<String, Object> get() {
        return props;
    }


    public static class Builder {

        private final MiGooVariables variables = new MiGooVariables();
        private final Vector<Configs> configs = new Vector<>(10);
        private final Vector<Processors> preprocessors = new Vector<>(10);
        private final Vector<Processors> postprocessors = new Vector<>(10);
        private final Vector<TestSets> children = new Vector<>(10);
        private final String title;

        private Builder(String title) {
            this.title = title;
        }

        public Builder addVariable(String name, Object value) {
            variables.put(name, value);
            return this;
        }

        public Builder addConfig(Configs... config) {
            configs.addAll(Arrays.asList(config));
            return this;
        }

        public Builder addPreprocessor(Processors... processor) {
            preprocessors.addAll(Arrays.asList(processor));
            return this;
        }

        public Builder addPostprocessor(Processors... processor) {
            postprocessors.addAll(Arrays.asList(processor));
            return this;
        }

        public Builder addTestSet(TestSets... testSets) {
            children.addAll(Arrays.asList(testSets));
            return this;
        }

        public TestSuite build() {
            return new TestSuite(title, variables, configs, preprocessors, postprocessors, children);
        }
    }
}
