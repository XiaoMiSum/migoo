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
 * 数值相等规则实现类
 *
 * <p>该类实现了数值相等比较规则，用于验证两个数值是否相等。该类使用BigDecimal进行精确的数值比较，
 * 避免了浮点数运算的精度问题。适用于需要精确数值比较的场景。</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "rule": "number_equals",
 *   "actual": 10.0,
 *   "expected": "10"
 * }
 * </pre>
 * 上述示例将返回true，因为10.0和"10"在数值上是相等的。</p>
 *
 * <p>支持的关键字包括："number_equals", "num_eq", "numeq", "ne"</p>
 *
 * @author xiaomi
 * @see Rule
 * @see BaseRule
 * @see BigDecimal
 * @since 2021/10/13 18:41
 */
@KW({"number_equals", "num_eq", "numeq", "ne"})
public class NumberEquals extends BaseRule implements Rule {

    /**
     * 执行数值相等性验证
     *
     * <p>验证逻辑：
     * <ol>
     *   <li>将实际值和期望值都转换为字符串表示（如果为null则使用默认值"0"）</li>
     *   <li>将字符串转换为BigDecimal对象以支持精确数值比较</li>
     *   <li>使用BigDecimal.compareTo()方法进行数值比较</li>
     *   <li>返回比较结果（当两个数值相等时返回true）</li>
     * </ol>
     * </p>
     *
     * <p>注意事项：
     * <ul>
     *   <li>使用BigDecimal确保浮点数比较的精度</li>
     *   <li>对于非数值类型，会尝试转换为数值进行比较</li>
     *   <li>如果转换失败，可能会导致比较结果不符合预期</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值，参与比较的第一个数值
     * @param expected 期望值，参与比较的第二个数值
     * @return 验证结果，当actual与expected在数值上相等时返回true，否则返回false
     * @see BaseRule#objectToString(Object, String)
     * @see BigDecimal#compareTo(BigDecimal)
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        var b1 = new BigDecimal(objectToString(actual, "0"));
        var b2 = new BigDecimal(objectToString(expected, "0"));
        return b1.compareTo(b2) == 0;
    }
}