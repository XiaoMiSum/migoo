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

package io.github.xiaomisum.ryze.core.testelement;

import io.github.xiaomisum.ryze.core.builder.DefaultChildrenBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultConfigureElementsBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultPostprocessorsBuilder;
import io.github.xiaomisum.ryze.core.builder.DefaultPreprocessorsBuilder;
import io.github.xiaomisum.ryze.core.config.EmptyConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;

/**
 * 测试集合执行类
 *
 * @author xiaomi
 */
@KW("__testsuite__")
public class TestSuite extends TestContainerExecutable<TestSuite, EmptyConfigureItem, TestSuiteResult> {

    public TestSuite(Builder builder) {
        super(builder);
    }

    public TestSuite() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult(runtime.id, runtime.title);
    }

    @Override
    protected void execute(ContextWrapper context, TestSuiteResult result) {
        executeChildren(context);
    }

    /**
     * 容器基础构建器
     */
    public static class Builder extends TestContainerExecutable.Builder<TestSuite, Builder, EmptyConfigureItem, EmptyConfigureItem.Builder,
            DefaultConfigureElementsBuilder, DefaultPreprocessorsBuilder, DefaultPostprocessorsBuilder, DefaultChildrenBuilder, TestSuiteResult> {

        @Override
        protected DefaultConfigureElementsBuilder getConfiguresBuilder() {
            return DefaultConfigureElementsBuilder.builder();
        }

        @Override
        protected DefaultChildrenBuilder getChildrenBuilder() {
            return DefaultChildrenBuilder.builder();
        }

        @Override
        protected DefaultPreprocessorsBuilder getPreprocessorsBuilder() {
            return DefaultPreprocessorsBuilder.builder();
        }

        @Override
        protected DefaultPostprocessorsBuilder getPostprocessorsBuilder() {
            return DefaultPostprocessorsBuilder.builder();
        }

        @Override
        public TestSuite build() {
            return new TestSuite(this);
        }

        @Override
        protected EmptyConfigureItem.Builder getConfigureItemBuilder() {
            return EmptyConfigureItem.builder();
        }
    }
}
