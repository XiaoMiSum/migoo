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

package core.xyz.migoo;

import core.xyz.migoo.builder.DefaultChildrenBuilder;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestSuite;
import core.xyz.migoo.testelement.TestSuiteResult;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.apache.commons.lang3.StringUtils;
import support.xyz.migoo.Customizer;
import support.xyz.migoo.groovy.Groovy;

/**
 * 魔法盒子 提供函数式测试用例执行入口
 *
 * @author xiaomi
 */
public class MagicBox {

    public static TestSuiteResult runSuite(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure) {
        return runSuite("", closure);
    }

    public static TestSuiteResult runSuite(String title,
                                           @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure) {
        return runSuite(title, closure, null);
    }

    public static TestSuiteResult runSuite(String title,
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

    public static TestSuiteResult runSuite(Customizer<TestSuite.Builder> customizer) {
        return runSuite("", customizer);
    }

    public static TestSuiteResult runSuite(String title, Customizer<TestSuite.Builder> customizer) {
        return runSuite(title, customizer, null);
    }

    public static TestSuiteResult runSuite(String title, Customizer<TestSuite.Builder> customizer, Customizer<DefaultChildrenBuilder> children) {
        var builder = TestSuite.builder();
        customizer.customize(builder);
        if (children != null) {
            var localChildren = DefaultChildrenBuilder.builder();
            children.customize(localChildren);
            builder.children(localChildren.build());
        }
        return runTest(title, builder.build());
    }

    public static <R extends Result> R runTest(String title, TestElement<R> testElement) {
        if (testElement instanceof AbstractTestElement<?, ?, R> && StringUtils.isNotBlank(title)) {
            ((AbstractTestElement<?, ?, R>) testElement).setTitle(title);
        }
        return SessionRunner.getSession().runTest(testElement);
    }


    public static void main(String[] args) {
        runSuite(suite ->
                suite.title("test suite")
                        .variables(var -> var.variables("var1", 1).variables("var2", 2))
                        .configureElements(ele -> ele.http(
                                        http -> http.config(conf -> conf.host("127.0.0.1").port("8080").path("/user").headers(header -> {
                                                    header.put("Content-Type", "application/json");
                                                    header.put("App", "application/json");
                                                }
                                        )))
                                .jdbc(jdbc -> jdbc.config(conf -> conf.url("jdbc:mysql://127.0.0.1:3306/migoo?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true")))
                        ).children(
                                child -> child.http(sampler -> sampler.assertions(assertion -> assertion.json(json -> json.field("$.status").rule("equals").expected("200"))))
                        )
        );
    }
}
