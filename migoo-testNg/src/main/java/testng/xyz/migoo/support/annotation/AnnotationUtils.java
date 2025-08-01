package testng.xyz.migoo.support.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnnotationUtils {

    public static Datasource getDatasource(Method method) {
        Datasource datasource = method.getAnnotation(Datasource.class);
        if (Objects.nonNull(datasource)) {
            return datasource;
        }
        MiGoo migoo = method.getAnnotation(MiGoo.class);
        if (Objects.isNull(migoo)) {
            return null;
        }
        Map<String, Object> values = new HashMap<>();
        values.put("value", migoo.value());
        values.put("parallel", migoo.parallel());
        values.put("type", migoo.type());
        return newInstance(Datasource.class, values);
    }

    public static void main(String[] args) {
        var d = newInstance(Datasource.class, Map.of("value", "1", "parallel", true, "type", Map.class));
        System.out.println(d.value());
        System.out.println(d.parallel());
        System.out.println(d.type());
    }


    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A newInstance(Class<A> annotationType, Map<String, Object> values) {
        return (A) Proxy.newProxyInstance(annotationType.getClassLoader(),
                new Class[]{annotationType},
                new AnnotationInvocationHandler(values));
    }


    static class AnnotationInvocationHandler implements InvocationHandler {

        private final Map<String, Object> values;

        AnnotationInvocationHandler(Map<String, Object> values) {
            this.values = values;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (values.containsKey(method.getName())) {
                return values.get(method.getName());
            }
            return method.invoke(this, args);
        }
    }
}
