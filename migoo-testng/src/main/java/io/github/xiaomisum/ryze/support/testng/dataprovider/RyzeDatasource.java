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

package io.github.xiaomisum.ryze.support.testng.dataprovider;

import io.github.xiaomisum.ryze.support.TestDataLoader;
import io.github.xiaomisum.ryze.support.testng.annotation.AnnotationUtils;
import io.leangen.geantyref.TypeFactory;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;
import org.testng.util.Strings;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * 数据源提供者
 *
 * @author xiaomi
 */
public class RyzeDatasource {

    public static final String DATASOURCE_PROVIDER = "__ryze_testng_data_provider__";
    public static final String DATASOURCE_PROVIDER_PARALLEL = "__ryze_testng_data_provider_parallel__";

    private static final Object[][] EMPTY_DATA = new Object[][]{};

    @DataProvider(name = DATASOURCE_PROVIDER_PARALLEL)
    public static Object[][] datasourceParallel(ITestNGMethod method) {
        return datasource(method);
    }

    @DataProvider(name = DATASOURCE_PROVIDER)
    public static Object[][] datasource(ITestNGMethod method) {
        var datasource = AnnotationUtils.getDatasource(method.getConstructorOrMethod().getMethod());
        if (Objects.isNull(datasource) || Strings.isNullOrEmpty(datasource.value())) {
            return EMPTY_DATA;
        }
        // 动态设置数据源类型
        Type type = TypeFactory.parameterizedClass(List.class, datasource.type());
        List<?> data = TestDataLoader.toJavaObject(datasource.value(), type);
        // 数据过滤
        return Objects.isNull(data) || data.isEmpty() ? EMPTY_DATA :
                data.stream().map(obj -> new Object[]{obj}).toArray(Object[][]::new);
    }
}
