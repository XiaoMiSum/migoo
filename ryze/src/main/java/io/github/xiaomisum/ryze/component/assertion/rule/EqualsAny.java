package io.github.xiaomisum.ryze.component.assertion.rule;

import io.github.xiaomisum.ryze.core.assertion.Rule;
import io.github.xiaomisum.ryze.core.testelement.KW;

/**
 * 相等任意规则实现类
 *
 * <p>该类实现了"相等任意"规则，用于验证实际值是否与期望值集合中的任意一个元素相等。该规则继承自{@link AnyBase}基类，
 * 使用{@link Equals}规则作为具体的匹配规则。</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "rule": "any",
 *   "actual": 5,
 *   "expected": [1, 3, 5, 7]
 * }
 * </pre>
 * 上述示例将返回true，因为5与期望值集合中的第三个元素相等。</p>
 *
 * <p>支持的关键字包括："any", "eqa", "equalAny", "eq_any"</p>
 *
 * @see Rule
 * @see AnyBase
 * @see Equals
 */
@KW({"any", "eqa", "equalAny", "eq_any"})
public class EqualsAny extends AnyBase implements Rule {

    /**
     * 执行相等任意验证
     *
     * <p>验证逻辑：
     * <ol>
     *   <li>调用父类 {@link AnyBase#assertThat(Object, Object, Rule)}方法</li>
     *   <li>使用 {@link Equals#assertThat(Object, Object)}规则作为具体的匹配规则</li>
     *   <li>只要实际值与期望值集合中的任意一个元素相等就返回true</li>
     * </ol>
     * </p>
     *
     * <p>相等性判断规则与 {@link Equals#assertThat(Object, Object)}类一致：
     * <ul>
     *   <li>如果实际值或期望值是数字类型，则使用 {@link NumberEquals#assertThat(Object, Object)}进行数值比较</li>
     *   <li>否则使用字符串比较</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值，参与比较的对象
     * @param expected 期望值，可以是数组、集合或单个对象，只要实际值与其中任意一个相等就验证通过
     * @return 验证结果，只要actual与expected中的任意一个元素相等就返回true，否则返回false
     * @see AnyBase#assertThat(Object, Object, Rule)
     * @see Equals
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        return super.assertThat(actual, expected, new Equals());
    }
}