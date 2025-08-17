/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.xiaomisum.ryze.core.context.variables;

import java.util.Map;

/**
 * 变量包装类
 *
 * @author xiaomi
 */
public interface VariablesWrapper {

    /**
     * 根据变量名称读取变量值
     *
     * @param name 变量名称
     * @return 变量值
     */
    Object get(String name);

    /**
     * 设置指定名称变量的值
     *
     * @param name  变量名称
     * @param value 变量值
     * @return 变量之前的值，如果有，否则返回 null
     */
    Object put(String name, Object value);

    /**
     * 移除指定变量
     *
     * @param name 变量名称
     * @return 变量之前的值，如果有，否则返回 null
     */
    Object remove(String name);

    /**
     * 合并所有上下文的变量。
     *
     * <p>合并后返回一个新的 Map 对象，对该对象的修改不会反映到上下文的原始变量中。
     *
     * @return 合并后的变量
     */
    Map<String, Object> mergeVariables();

}
