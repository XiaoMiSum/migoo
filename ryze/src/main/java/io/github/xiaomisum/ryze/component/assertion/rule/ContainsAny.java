package io.github.xiaomisum.ryze.component.assertion.rule;

import io.github.xiaomisum.ryze.core.assertion.Rule;
import io.github.xiaomisum.ryze.core.testelement.KW;

/**
 * 包含任意规则实现类
 *
 * <p>该类实现了"包含任意"规则，用于验证实际值是否包含期望值集合中的任意一个元素。该规则继承自 {@link AnyBase}基类，
 * 使用 {@link Contains}规则作为具体的匹配规则。</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "rule": "containAny",
 *   "actual": "hello world",
 *   "expected": ["hello", "java"]
 * }
 * </pre>
 * 上述示例将返回true，因为"hello world"包含"hello"。</p>
 *
 * <p>支持的关键字包括："containAny", "ct_any", "cta"</p>
 *
 * @see Rule
 * @see AnyBase
 * @see Contains
 */
@KW({"containAny", "ct_any", "cta"})
public class ContainsAny extends AnyBase implements Rule {

    /**
     * 执行包含任意验证
     *
     * <p>验证逻辑：
     * <ol>
     *   <li>调用父类 {@link AnyBase#assertThat(Object, Object, Rule)} 方法</li>
     *   <li>使用 {@link Contains}规则作为具体的匹配规则</li>
     *   <li>只要实际值包含期望值集合中的任意一个元素就返回true</li>
     * </ol>
     * </p>
     *
     * <p>支持的数据类型与 {@link Contains#assertThat(Object, Object)}类一致：
     * <ul>
     *   <li>String类型: 检查字符串是否包含指定子串</li>
     *   <li>Map类型: 检查Map是否包含指定的键或值</li>
     *   <li>List类型: 检查List是否包含指定元素</li>
     *   <li>其他类型: 返回false</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值，被检查的对象
     * @param expected 期望值，可以是数组、集合或单个对象，只要实际值包含其中任意一个就验证通过
     * @return 验证结果，只要actual包含expected中的任意一个元素就返回true，否则返回false
     * @see AnyBase#assertThat(Object, Object, Rule)
     * @see Contains
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        return super.assertThat(actual, expected, new Contains());
    }

}