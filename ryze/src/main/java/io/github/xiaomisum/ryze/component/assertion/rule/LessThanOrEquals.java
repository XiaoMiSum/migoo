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
 * 小于等于规则实现类
 *
 * <p>该类实现了"<="比较规则，用于验证实际值是否小于或等于期望值。支持数值类型的精确比较，
 * 包括整数、浮点数等。比较时会将对象转换为字符串，再转换为BigDecimal进行精确数值比较。</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "rule": "<=",
 *   "actual": 5,
 *   "expected": 10
 * }
 * </pre>
 * 上述示例将返回true，因为5小于等于10。</p>
 *
 * <p>支持的关键字包括："<="</p>
 *
 * @author xiaomi
 * @see Rule
 * @see BaseRule
 * @see BigDecimal
 * @since 2019-08-13 22:17
 */
@KW({"<="})
public class LessThanOrEquals extends BaseRule implements Rule {

    /**
     * 执行小于等于比较验证
     *
     * <p>验证逻辑：
     * <ol>
     *   <li>将实际值和期望值都转换为字符串表示（如果为null则使用默认值"0"）</li>
     *   <li>将字符串转换为BigDecimal对象以支持精确数值比较</li>
     *   <li>使用BigDecimal.compareTo()方法进行数值比较</li>
     *   <li>返回比较结果（当实际值小于等于期望值时返回true）</li>
     * </ol>
     * </p>
     *
     * <p>注意事项：
     * <ul>
     *   <li>对于非数值类型，会尝试转换为数值进行比较</li>
     *   <li>如果转换失败，可能会导致比较结果不符合预期</li>
     *   <li>使用BigDecimal确保浮点数比较的精度</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值，参与比较的第一个操作数
     * @param expected 期望值，参与比较的第二个操作数
     * @return 验证结果，当actual <= expected时返回true，否则返回false
     * @see BaseRule#objectToString(Object, String)
     * @see BigDecimal#compareTo(BigDecimal)
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        var b1 = new BigDecimal(objectToString(actual, "0"));
        var b2 = new BigDecimal(objectToString(expected, "0"));
        return b1.compareTo(b2) <= 0;
    }
}