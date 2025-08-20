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
 * 不相等规则实现类
 *
 * <p>该类实现了不相等比较规则，用于验证实际值是否与期望值不相等。该类通过委托给 {@link Equals}类实现，
 * 然后对结果取反来实现不相等的判断逻辑。</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "rule": "!=",
 *   "actual": 5,
 *   "expected": 3
 * }
 * </pre>
 * 上述示例将返回true，因为5不等于3。</p>
 *
 * <p>支持的关键字包括："!==", "not", "<>", "!="</p>
 *
 * @author xiaomi
 * @see Rule
 * @see Equals
 * @since 2019-08-13 22:17
 */
@KW({"!==", "not", "<>", "!="})
public class DoseNotEquals implements Rule {

    /**
     * 相等规则实例，用于委托执行相等性验证
     *
     * <p>通过复用 {@link Equals}类的实现逻辑，然后对结果取反来实现不相等验证，
     * 这种设计遵循了面向对象的组合原则，避免重复实现相同逻辑。</p>
     */
    private final Equals equals = new Equals();

    /**
     * 执行不相等性验证
     *
     * <p>验证逻辑：
     * <ol>
     *   <li>委托 {@link Equals}类执行相等性验证</li>
     *   <li>对 {@link Equals#assertThat(Object, Object)}方法的返回结果取反</li>
     *   <li>返回取反后的结果作为不相等性验证的结果</li>
     * </ol>
     * </p>
     *
     * <p>相等性判断规则与 {@link Equals}类一致：
     * <ul>
     *   <li>如果实际值或期望值是数字类型，则使用 {@link NumberEquals#assertThat(Object, Object)}进行数值比较</li>
     *   <li>否则使用字符串比较</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值，参与比较的第一个操作数
     * @param expected 期望值，参与比较的第二个操作数
     * @return 验证结果，当actual不等于expected时返回true，否则返回false
     * @see Equals#assertThat(Object, Object)
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        return !equals.assertThat(actual, expected);
    }
}