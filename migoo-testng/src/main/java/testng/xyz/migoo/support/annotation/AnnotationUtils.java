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

package testng.xyz.migoo.support.annotation;

import org.apache.commons.lang3.Strings;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaomi
 */
public class AnnotationUtils {

    public static Datasource getDatasource(Method method) {
        var datasource = method.getAnnotation(Datasource.class);
        if (Objects.nonNull(datasource)) {
            return datasource;
        }
        var migoo = method.getAnnotation(MiGoo.class);
        if (Objects.isNull(migoo)) {
            return null;
        }
        var values = new HashMap<String, Object>();
        values.put("value", migoo.value());
        values.put("parallel", migoo.parallel());
        values.put("type", migoo.type());
        values.put("slice", migoo.slice());
        return newInstance(Datasource.class, values);
    }

    public static boolean hasAnnotation(Class<? extends Annotation> type, Class<? extends Annotation> targetType) {
        if (type == targetType) {
            return true;
        }
        for (var annotation : type.getDeclaredAnnotations()) {
            var annotationType = annotation.annotationType();
            if (!isJDKAnnotation(annotationType) && hasAnnotation(annotationType, targetType)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isJDKAnnotation(Class<? extends Annotation> annotation) {
        return Strings.CI.equals(annotation.getPackage().getName(), "java.lang.annotation");
    }

    public static void main(String[] args) throws NoSuchMethodException {
        var d = newInstance(Datasource.class, Map.of("value", "1", "parallel", true, "slice", "[1..10]", "type", Map.class));
        System.out.println(d.value());
        System.out.println(d.parallel());
        System.out.println(d.type());
        System.out.println(d.slice());
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A newInstance(Class<A> annotationType, Map<String, Object> values) {
        return (A) Proxy.newProxyInstance(annotationType.getClassLoader(),
                new Class[]{annotationType},
                new AnnotationInvocationHandler(values));
    }

    public static boolean isNotMiGooTest(Method method) {
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (AnnotationUtils.hasAnnotation(annotation.annotationType(), MiGooTest.class)) {
                return true;
            }
        }
        return false;
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
