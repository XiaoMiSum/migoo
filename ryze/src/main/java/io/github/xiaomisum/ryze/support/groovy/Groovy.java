package io.github.xiaomisum.ryze.support.groovy;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

public class Groovy {

    public static void call(Closure<?> closure, Object delegate) {
        Closure<?> code = (Closure<?>) closure.clone();
        code.setDelegate(delegate);
        code.setResolveStrategy(Closure.DELEGATE_ONLY);
        code.call();
    }

    public static <T> T builder(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
        try {
            var builder = type.getConstructor().newInstance();
            call(closure, builder);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
