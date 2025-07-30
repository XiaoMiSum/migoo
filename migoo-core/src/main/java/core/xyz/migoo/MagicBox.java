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
import support.xyz.migoo.groovy.Groovy;

import java.util.function.Supplier;

import static core.xyz.migoo.SessionRunner.getSession;
import static groovy.lang.Closure.DELEGATE_ONLY;

public class MagicBox {

    public static TestSuiteResult runSuite(@DelegatesTo(strategy = DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure) {
        return runSuite("", closure);
    }

    public static TestSuiteResult runSuite(String title,
                                           @DelegatesTo(strategy = DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure) {
        return runSuite(title, closure, null);
    }

    public static TestSuiteResult runSuite(String title,
                                           @DelegatesTo(strategy = DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure,
                                           Supplier<DefaultChildrenBuilder> children) {
        var builder = TestSuite.builder();
        Groovy.call(closure, builder);
        if (children != null) {
            builder.children(children.get().build());
        }
        return runTest(title, builder.build());
    }

    public static TestSuiteResult runSuite(Supplier<TestSuite.Builder> suite) {
        return runTest("", suite.get().build());
    }

    public static TestSuiteResult runSuite(String title, Supplier<TestSuite.Builder> suite) {
        return runTest(title, suite.get().build());
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
        return getSession().runTest(testElement);
    }
}
