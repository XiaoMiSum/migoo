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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * 数据驱动增强注解，以支持从文件中读取测试用例
 * <p>
 * 该注解用于标记TestNG测试方法，使其能够从外部文件（如JSON、YAML）加载测试数据。
 * 支持并行执行和数据切片功能，可以灵活地控制测试数据的执行范围。
 * </p>
 *
 * @author xiaomi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Datasource {

    /**
     * 数据源：文件路径
     * <p>
     * 指定测试数据文件的路径，支持多种协议：
     * <ul>
     *   <li>本地文件：/path/to/file.json 或 file:/path/to/file.yaml</li>
     *   <li>类路径资源：classpath:file.json</li>
     * </ul>
     * </p>
     *
     * @return 数据源文件路径
     */
    String value() default "";

    /**
     * 是否并行执行
     * <p>
     * 当设置为true时，测试方法会对数据源中的每条数据并行执行。
     * 适用于独立的测试用例，可以显著提高执行效率。
     * </p>
     *
     * @return 是否并行执行
     */
    boolean parallel() default false;

    /**
     * 数据切片,将指定下标的数据截取作为实际测试方法的参数，
     * 默认为空，表示不进行切片
     * <p>
     * 支持多种切片格式：
     * <ul>
     *   <li>[start:end]：范围切片，如[1:10]表示第1到第10条数据</li>
     *   <li>[start:]：从start开始到末尾，如[3:]表示从第3条数据到末尾</li>
     *   <li>[:end]：从开头到end，如[:5]表示从第1条到第5条数据</li>
     *   <li>[index,index,index...]：指定索引，如[1,2,3]表示第1、2、3条数据</li>
     * </ul>
     * </p>
     * <p>
     * 索引从1开始计算，支持负数索引（-1表示最后一条数据）。
     * </p>
     *
     * @return 切片表达式
     */
    String slice() default "";

    /**
     * 返回参数类型
     * <p>
     * 指定从数据源文件解析出的数据类型，默认为Map.class。
     * 当数据源包含复杂对象时，可以指定具体的类型以获得更好的类型安全性。
     * </p>
     *
     * @return 返回参数类型
     */
    Class<?> type() default Map.class;
}