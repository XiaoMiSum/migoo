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

import static io.github.xiaomisum.ryze.support.testng.dataprovider.RyzeDatasource.DATASOURCE_PROVIDER;
import static io.github.xiaomisum.ryze.support.testng.dataprovider.RyzeDatasource.DATASOURCE_PROVIDER_PARALLEL;

/**
 * 通过监听器监听，将数据源注解注入测试方法
 *
 * @author xiaomi
 */
public class RyzeAnnotationTransformer implements IAnnotationTransformer {

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