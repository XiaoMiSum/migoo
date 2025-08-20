package io.github.xiaomisum.ryze.component.assertion.rule;

import io.github.xiaomisum.ryze.core.assertion.Rule;

import java.util.List;

/**
 * 任意匹配规则基类
 * 
 * <p>该类为所有"任意匹配"类型的规则提供基础实现。任意匹配是指：当期望值为集合或数组时，
 * 只要实际值与其中任意一个期望值匹配成功，就认为整个验证通过。</p>
 * 
 * <p>该类采用模板方法设计模式，定义了通用的任意匹配逻辑，具体的匹配规则由子类提供。</p>
 *
 * @see Rule
 */
public class AnyBase {

    /**
     * 执行任意匹配验证
     * 
     * <p>验证逻辑：
     * <ol>
     *   <li>将期望值转换为List集合（支持数组、List、单个对象和null）</li>
     *   <li>遍历期望值集合中的每个元素</li>
     *   <li>使用指定的规则对实际值和当前期望值进行比较</li>
     *   <li>只要有一个匹配成功就立即返回true</li>
     *   <li>如果所有元素都不匹配则返回false</li>
     * </ol>
     * </p>
     * 
     * <p>期望值类型处理规则：
     * <ul>
     *   <li>Object[]数组: 转换为List</li>
     *   <li>List集合: 直接使用</li>
     *   <li>null值: 转换为包含一个空字符串的List</li>
     *   <li>其他类型: 转换为包含单个元素的List</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值，将与期望值集合中的每个元素进行比较
     * @param expected 期望值，可以是数组、集合或单个对象
     * @param rule     用于执行具体比较的规则实例
     * @return 验证结果，只要actual与expected中的任意一个元素匹配就返回true，否则返回false
     * @see Rule#assertThat(Object, Object)
     * @see List#of(Object[])
     */
    public boolean assertThat(Object actual, Object expected, Rule rule) {
        var objects = switch (expected) {
            case Object[] os -> List.of(os);
            case List<?> ls -> ls;
            case null -> List.of(""); // 这里要给个值，以便进入循环
            default -> List.of(expected);
        };
        for (var object : objects) {
            if (rule.assertThat(actual, object)) {
                return true;
            }
        }
        return false;
    }
}