package coder.xyz.migoo;

import core.xyz.migoo.SessionRunner;
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
            builder.children(customizer.apply(DefaultChildrenBuilder.builder()).build());
        }
        return runTest(title, builder.build());
    }

    public static TestSuiteResult runSuite(Customizer<TestSuite.Builder> customizer) {
        return runTest("", customizer.apply(TestSuite.builder()).build());
    }

    public static TestSuiteResult runSuite(String title, Customizer<TestSuite.Builder> customizer) {
        return runTest(title, customizer.apply(TestSuite.builder()).build());
    }

    public static TestSuiteResult runSuite(TestSuite.Builder suite) {
        return runTest("", suite.build());
    }


    public static TestSuiteResult runSuite(String title, TestSuite.Builder suite) {
        return runTest(title, suite.build());
    }

    public static TestSuiteResult runSuite(TestSuite suite) {
        return runTest("", suite);
    }


    public static TestSuiteResult runSuite(String title, TestSuite suite) {
        return runTest(title, suite);
    }

    public static <R extends Result> R runTest(TestElement<R> testElement) {
        return runTest("", testElement);
    }

    public static <R extends Result> R runTest(String title, TestElement<R> testElement) {
        if (testElement instanceof AbstractTestElement<?, ?, R> && StringUtils.isNotBlank(title)) {
            ((AbstractTestElement<?, ?, R>) testElement).setTitle(title);
        }
        return SessionRunner.getSession().runTest(testElement);
    }

    // todo 添加运行 其他协议的方法

    public static void main(String[] args) {
        TestSuite.builder()
                .configureElements(ele -> ele.http(
                                http -> http.config(conf -> conf.host("127.0.0.1").port("8080").path("/user").headers(header -> {
                                            header.put("Content-Type", "application/json");
                                            header.put("App", "application/json");
                                            return header;
                                        }
                                )))
                        .jdbc(jdbc -> jdbc.config(conf -> conf.url("jdbc:mysql://127.0.0.1:3306/migoo?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&allowMultiQueries=true")))
                )

        ;
    }
}
