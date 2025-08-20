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
 * 不包含规则实现类
 *
 * <p>该类实现了不包含比较规则，用于验证实际值是否不包含期望值。该类通过委托给 {@link Contains}类实现，
 * 然后对结果取反来实现不包含的判断逻辑。</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "rule": "notContains",
 *   "actual": "hello world",
 *   "expected": "java"
 * }
 * </pre>
 * 上述示例将返回true，因为"hello world"不包含"java"。</p>
 *
 * <p>支持的关键字包括："notContain", "notContains", "nc", "doesNotContains", "doesNotContain", "⊈"</p>
 *
 * @author xiaomi
 * @see Rule
 * @see Contains
 * @since 2019-08-13 22:17
 */
@KW({"notContain", "notContains", "nc", "doesNotContains", "doesNotContain", "⊈"})
public class DoseNotContains implements Rule {

    /**
     * 包含规则实例，用于委托执行包含性验证
     *
     * <p>通过复用 {@link Contains}类的实现逻辑，然后对结果取反来实现不包含验证，
     * 这种设计遵循了面向对象的组合原则，避免重复实现相同逻辑。</p>
     */
    private final Contains contains = new Contains();

    /**
     * 执行不包含性验证
     *
     * <p>验证逻辑：
     * <ol>
     *   <li>委托 {@link Contains}类执行包含性验证</li>
     *   <li>对 {@link Contains#assertThat(Object, Object)}方法的返回结果取反</li>
     *   <li>返回取反后的结果作为不包含性验证的结果</li>
     * </ol>
     * </p>
     *
     * <p>支持的数据类型与 {@link Contains}类一致：
     * <ul>
     *   <li>String类型: 检查字符串是否不包含指定子串</li>
     *   <li>Map类型: 检查Map是否不包含指定的键或值</li>
     *   <li>List类型: 检查List是否不包含指定元素</li>
     *   <li>其他类型: 当 {@link Contains#assertThat(Object, Object)}返回false时，该方法返回true</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值，被检查的对象
     * @param expected 期望值，需要确认不被包含的内容
     * @return 验证结果，当actual不包含expected时返回true，否则返回false
     * @see Contains#assertThat(Object, Object)
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        return !contains.assertThat(actual, expected);
    }
}