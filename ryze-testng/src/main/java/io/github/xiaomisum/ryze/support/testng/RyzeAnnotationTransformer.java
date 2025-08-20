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

package io.github.xiaomisum.ryze.support.testng;

import io.github.xiaomisum.ryze.support.testng.annotation.AnnotationUtils;
import io.github.xiaomisum.ryze.support.testng.annotation.Datasource;
import io.github.xiaomisum.ryze.support.testng.dataprovider.RyzeDatasource;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import org.testng.util.Strings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 注解转换器，用于将Ryze测试注解转换为TestNG数据提供者配置
 * <p>
 * 该类实现了TestNG的 {@link IAnnotationTransformer}接口，
 * 在TestNG运行时动态修改测试方法的注解配置。
 * 主要功能是检测带有{@link Datasource}注解的测试方法，
 * 并为其配置相应的数据提供者。
 * </p>
 *
 * @author xiaomi
 */
public class RyzeAnnotationTransformer implements IAnnotationTransformer, TestNGConstantsInterface {

    /**
     * 转换Test注解，将Ryze测试注解配置转换为TestNG数据提供者配置
     * <p>
     * 该方法会检查测试方法是否包含Ryze测试注解，如果包含则设置相应的数据提供者类和方法名，
     * 用于在测试执行时提供测试数据。
     * </p>
     *
     * @param annotation      TestNG测试注解
     * @param testClass       测试类
     * @param testConstructor 测试构造函数
     * @param testMethod      测试方法
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (Objects.isNull(testMethod)) {
            return;
        }
        Datasource datasource = AnnotationUtils.getDatasource(testMethod);
        if (Objects.nonNull(datasource) && Strings.isNotNullAndNotEmpty(datasource.value())) {
            annotation.setDataProviderClass(RyzeDatasource.class);
            annotation.setDataProvider(datasource.parallel() ? DATASOURCE_PROVIDER_PARALLEL : DATASOURCE_PROVIDER);
        }
    }
}