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

package io.github.xiaomisum.ryze.support.testng.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 注解工具类，提供注解处理相关功能
 * <p>
 * 该类提供了处理TestNG和Ryze注解的工具方法，
 * 包括注解获取、注解类型检查和动态注解创建等功能。
 * </p>
 *
 * @author xiaomi
 */
public class AnnotationUtils {

    /**
     * 获取方法上的数据源注解信息
     * <p>
     * 该方法优先查找方法上直接声明的 {@link Datasource}注解，如果找不到，则尝试查找 {@link RyzeTest}注解，
     * 并将其信息转换为Datasource注解实例返回。
     * </p>
     *
     * @param method 需要获取数据源注解的方法
     * @return Datasource注解实例，如果方法上没有相关注解则返回null
     */
    public static Datasource getDatasource(Method method) {
        var datasource = method.getAnnotation(Datasource.class);
        if (Objects.nonNull(datasource)) {
            return datasource;
        }
        var ryze = method.getAnnotation(RyzeTest.class);
        if (Objects.isNull(ryze)) {
            return null;
        }
        var values = new HashMap<String, Object>();
        values.put("value", ryze.value());
        values.put("parallel", ryze.parallel());
        values.put("type", ryze.type());
        values.put("slice", ryze.slice());
        return newInstance(Datasource.class, values);
    }

    /**
     * 递归判断指定注解类型是否包含目标注解类型
     * <p>
     * 该方法会递归检查注解及其元注解，判断是否存在指定的目标注解类型。
     * 主要用于检查一个注解是否标记为Ryze测试注解。
     * </p>
     *
     * @param type       需要检查的注解类型
     * @param targetType 目标注解类型
     * @return 如果type注解或其元注解中包含targetType则返回true，否则返回false
     */
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

    /**
     * 判断指定注解是否为JDK内置注解
     * <p>
     * 通过检查注解所在包名是否为java.lang.annotation来判断是否为JDK内置注解。
     * </p>
     *
     * @param annotation 需要判断的注解类型
     * @return 如果是JDK内置注解返回true，否则返回false
     */
    private static boolean isJDKAnnotation(Class<? extends Annotation> annotation) {
        return annotation.getPackage().getName().equals("java.lang.annotation");
    }

    public static void main(String[] args) throws NoSuchMethodException {
        var d = newInstance(Datasource.class, Map.of("value", "1", "parallel", true, "slice", "[1..10]", "type", Map.class));
        System.out.println(d.value());
        System.out.println(d.parallel());
        System.out.println(d.type());
        System.out.println(d.slice());
    }

    /**
     * 创建指定注解类型的动态代理实例
     * <p>
     * 通过Java动态代理机制创建指定注解类型的实例，并使用提供的值进行初始化。
     * 主要用于将 {@link RyzeTest} 注解转换为 {@link Datasource}注解。
     * </p>
     *
     * @param annotationType 注解类型
     * @param values         注解属性值映射表
     * @param <A>            注解类型参数
     * @return 指定注解类型的实例
     */
    @SuppressWarnings("unchecked")
    public static <A extends Annotation> A newInstance(Class<A> annotationType, Map<String, Object> values) {
        return (A) Proxy.newProxyInstance(annotationType.getClassLoader(),
                new Class[]{annotationType},
                new AnnotationInvocationHandler(values));
    }

    /**
     * 判断方法是否为Ryze测试方法
     * <p>
     * 通过检查方法上声明的所有注解，判断是否存在标记为RyzeTest的注解（包括元注解）。
     * </p>
     *
     * @param method 需要判断的方法
     * @return 如果方法是Ryze测试方法返回true，否则返回false
     */
    public static boolean isRyzeTest(Method method) {
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (AnnotationUtils.hasAnnotation(annotation.annotationType(), RyzeTest.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 注解调用处理器，用于处理动态代理注解实例的方法调用
     * <p>
     * 该处理器实现了{@link InvocationHandler}接口，用于处理动态创建的注解实例的方法调用。
     * </p>
     */
    record AnnotationInvocationHandler(Map<String, Object> values) implements InvocationHandler {

        /**
         * 处理注解方法调用
         * <p>
         * 如果调用的方法在values中有对应的值，则返回该值；否则调用默认方法。
         * </p>
         *
         * @param proxy  代理对象
         * @param method 调用的方法
         * @param args   方法参数
         * @return 方法调用结果
         * @throws Throwable 方法调用异常
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (values.containsKey(method.getName())) {
                return values.get(method.getName());
            }
            return method.invoke(this, args);
        }
    }
}