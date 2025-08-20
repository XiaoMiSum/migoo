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

import java.math.BigDecimal;

/**
 * 大于验证规则实现类
 *
 * <p>该类实现了大于验证规则，用于判断实际值是否大于期望值。
 * 使用BigDecimal进行数值比较，确保精度。</p>
 *
 * <p>支持的规则关键字: ">", "greater", "greaterThan", "gt"</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "field": "age",
 *   "expected": 18,
 *   "rule": ">"
 * }
 *
 * {
 *   "testclass": "http"
 *   "field": "score",
 *   "expected": 90.5,
 *   "rule": "greaterThan"
 * }
 * </pre>
 * </p>
 *
 * @author xiaomi
 * @see Rule 验证规则接口
 * @since 2019-08-13 22:17
 */
@KW({">", "greater", "greaterThan", "gt"})
public class GreaterThan extends BaseRule implements Rule {

    /**
     * 执行大于验证
     *
     * <p>使用BigDecimal进行数值比较，确保精度。
     * null值被当作0处理。</p>
     *
     * @param actual   实际值
     * @param expected 期望值
     * @return 验证结果，true表示实际值大于期望值，false表示实际值不大于期望值
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        var b1 = new BigDecimal(objectToString(actual, "0"));
        var b2 = new BigDecimal(objectToString(expected, "0"));
        return b1.compareTo(b2) > 0;
    }
}