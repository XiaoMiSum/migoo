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

package io.github.xiaomisum.ryze.component.assertion.rule;

import io.github.xiaomisum.ryze.core.assertion.Rule;
import io.github.xiaomisum.ryze.core.testelement.KW;

/**
 * 非空验证规则实现类
 *
 * <p>该类实现了非空验证规则，用于判断实际值是否非空。
 * 该规则是IsEmpty规则的反向实现。</p>
 *
 * <p>支持的规则关键字: "isNotEmpty", "isNotNull", "isNotBlank", "notEmpty", "notNull", "notBlank"</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "field": "data",
 *   "expected": "",
 *   "rule": "isNotEmpty"
 * }
 * </pre>
 * </p>
 *
 * @author xiaomi
 * @see Rule 验证规则接口
 * @see IsEmpty 空值验证规则
 * @since 2019-08-13 22:17
 */
@KW({"isNotEmpty", "isNotNull", "isNotBlank", "notEmpty", "notNull", "notBlank"})
public class IsNotEmpty implements Rule {

    /**
     * 静态的空值验证规则实例
     */
    private static final IsEmpty IS_EMPTY = new IsEmpty();

    /**
     * 执行非空验证
     *
     * <p>该方法通过调用IsEmpty规则的验证结果并取反来实现非空验证。</p>
     *
     * @param actual   实际值
     * @param expected 期望值（该规则不使用期望值）
     * @return 验证结果，true表示非空，false表示为空
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        return !IS_EMPTY.assertThat(actual, expected);
    }
}