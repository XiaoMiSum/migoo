/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package io.github.xiaomisum.ryze;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.Result;
import io.github.xiaomisum.ryze.core.SessionRunner;
import io.github.xiaomisum.ryze.core.builder.DefaultChildrenBuilder;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.core.testelement.TestSuite;
import io.github.xiaomisum.ryze.core.testelement.TestSuiteResult;
import io.github.xiaomisum.ryze.protocol.debug.sampler.DebugSampler;
import io.github.xiaomisum.ryze.protocol.http.sampler.HTTPSampler;
import io.github.xiaomisum.ryze.protocol.jdbc.sampler.JDBCSampler;
import io.github.xiaomisum.ryze.protocol.redis.sampler.RedisSampler;
import io.github.xiaomisum.ryze.support.Customizer;
import io.github.xiaomisum.ryze.support.groovy.Groovy;
import org.apache.commons.lang3.StringUtils;

/**
 * 魔法盒子 提供函数式测试用例执行入口
 *
 * @author xiaomi
 */
public class MagicBox {

    public static TestSuiteResult suite(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure) {
        return suite("", closure);
    }

    public static TestSuiteResult suite(String title,
                                        @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure) {
        return suite(title, closure, null);
    }

    public static TestSuiteResult suite(String title,
                                        @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure,
                                        Customizer<DefaultChildrenBuilder> customizer) {
        var builder = TestSuite.builder();
        Groovy.call(closure, builder);
        if (customizer != null) {
            var children = DefaultChildrenBuilder.builder();
            customizer.customize(children);
            builder.children(children.build());
        }
        return runTest(title, builder.build());
    }

    public static TestSuiteResult suite(Customizer<TestSuite.Builder> customizer) {
        return suite("", customizer);
    }

    public static TestSuiteResult suite(String title, Customizer<TestSuite.Builder> customizer) {
        return suite(title, customizer, null);
    }

    public static TestSuiteResult suite(String title, Customizer<TestSuite.Builder> customizer, Customizer<DefaultChildrenBuilder> children) {
        var builder = TestSuite.builder();
        customizer.customize(builder);
        if (children != null) {
            var localChildren = DefaultChildrenBuilder.builder();
            children.customize(localChildren);
            builder.children(localChildren.build());
        }
        return runTest(title, builder.build());
    }

    public static Result http(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPSampler.Builder.class) Closure<?> closure) {
        return http("", closure);
    }

    public static Result http(String title,
                              @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPSampler.Builder.class) Closure<?> closure) {
        var builder = HTTPSampler.builder();
        Groovy.call(closure, builder);
        return MagicBox.runTest(title, builder.build());
    }

    public static Result http(Customizer<HTTPSampler.Builder> customizer) {
        return http("", customizer);
    }

    public static Result http(String title, Customizer<HTTPSampler.Builder> customizer) {
        var builder = HTTPSampler.builder();
        customizer.customize(builder);
        return MagicBox.runTest(title, builder.build());
    }

    public static Result jdbc(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JDBCSampler.Builder.class) Closure<?> closure) {
        return jdbc("", closure);
    }

    public static Result jdbc(String title,
                              @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JDBCSampler.Builder.class) Closure<?> closure) {
        var builder = JDBCSampler.builder();
        Groovy.call(closure, builder);
        return MagicBox.runTest(title, builder.build());
    }

    public static Result jdbc(Customizer<JDBCSampler.Builder> customizer) {
        return jdbc("", customizer);
    }

    public static Result jdbc(String title, Customizer<JDBCSampler.Builder> customizer) {
        var builder = JDBCSampler.builder();
        customizer.customize(builder);
        return MagicBox.runTest(title, builder.build());
    }

    public static Result redis(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RedisSampler.Builder.class) Closure<?> closure) {
        return redis("", closure);
    }

    public static Result redis(String title,
                               @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RedisSampler.Builder.class) Closure<?> closure) {
        var builder = RedisSampler.builder();
        Groovy.call(closure, builder);
        return runTest(title, builder.build());
    }

    public static Result redis(Customizer<RedisSampler.Builder> customizer) {
        return redis("", customizer);
    }

    public static Result redis(String title, Customizer<RedisSampler.Builder> customizer) {
        var builder = RedisSampler.builder();
        customizer.customize(builder);
        return runTest(title, builder.build());
    }

    public static <R extends Result> R runTest(String title, TestElement<R> testElement) {
        if (testElement instanceof AbstractTestElement<?, ?, R> && StringUtils.isNotBlank(title)) {
            ((AbstractTestElement<?, ?, R>) testElement).setTitle(title);
        }
        return SessionRunner.getSession().runTest(testElement);
    }

    public static Result debug(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DebugSampler.Builder.class) Closure<?> closure) {
        return debug("", closure);
    }

    public static Result debug(String title,
                               @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DebugSampler.Builder.class) Closure<?> closure) {
        var builder = DebugSampler.builder();
        Groovy.call(closure, builder);
        return MagicBox.runTest(title, builder.build());
    }

    public static Result debug(Customizer<DebugSampler.Builder> customizer) {
        return debug("", customizer);
    }

    public static Result debug(String title, Customizer<DebugSampler.Builder> customizer) {
        var builder = DebugSampler.builder();
        customizer.customize(builder);
        return MagicBox.runTest(title, builder.build());
    }
}
