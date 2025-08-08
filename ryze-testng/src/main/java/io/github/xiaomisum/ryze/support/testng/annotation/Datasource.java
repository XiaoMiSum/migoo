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
 * 数据驱动增强注解，以支持从文件中读取 测试用例
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Datasource {

    /**
     * 数据源：文件路径
     *
     * @return 数据源
     */
    String value() default "";

    /**
     * 是否并行执行
     *
     * @return 是否并行执行
     */
    boolean parallel() default false;

    /**
     * 数据切片,将指定下标的数据截取作为实际测试方法的参数，
     * 默认为空，表示不进行切片
     * <p>
     * [start:end]，start和end为下标，start为起始为1，end最大为数据源长度。
     * slice="[1:10]" 表示将数据源的第一行到第十行作为实际测试方法的参数
     * <p>
     * [start:]，以start开头，不指定结尾，start为起始为1。
     * slice="[3:]" 表示将数据源的第三行到最后一行作为实际测试方法的参数
     * <p>
     * [:end]，以end结尾，不指定开头，end最大为数据源长度。
     * slice="[:5]" 表示将数据源的第一行到第五行作为实际测试方法的参数
     * <p>
     * [index,index,index...]，截取指定行的数据作为实际测试方法的参数
     * slice="[1,2,3]"，表示将数据源的第一行、第二行、第三行作为实际测试方法的参数
     *
     * @return 切片
     */
    String slice() default "";

    /**
     * 返回参数类型
     *
     * @return 返回参数类型
     */
    Class<?> type() default Map.class;
}
