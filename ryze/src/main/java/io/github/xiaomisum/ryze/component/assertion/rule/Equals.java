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
 * 相等性验证规则实现类
 *
 * <p>该类实现了相等性验证规则，用于判断实际值和期望值是否相等。
 * 对于数字类型，使用数值比较；对于其他类型，使用字符串比较。</p>
 *
 * <p>支持的规则关键字: "==", "===", "eq", "equal", "equals", "is"</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "field": "code",
 *   "expected": 200,
 *   "rule": "=="
 * }
 *
 * {
 *   "testclass": "http"
 *   "field": "message",
 *   "expected": "success",
 *   "rule": "equals"
 * }
 * </pre>
 * </p>
 *
 * @author xiaomi
 * @see Rule 验证规则接口
 * @see NumberEquals 数字相等性验证规则
 * @since 2019-08-13 22:17
 */
@KW({"==", "===", "eq", "equal", "equals", "is"})
public class Equals extends BaseRule implements Rule {

    /**
     * 执行相等性验证
     *
     * <p>验证逻辑：
     * <ol>
     *   <li>如果实际值或期望值是数字类型，则使用NumberEquals进行数值比较</li>
     *   <li>否则使用字符串比较</li>
     * </ol>
     * </p>
     *
     * @param actual   实际值
     * @param expected 期望值
     * @return 验证结果，true表示相等，false表示不相等
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        if (actual instanceof Number || expected instanceof Number) {
            return new NumberEquals().assertThat(actual, expected);
        }
        return objectToString(actual).equals(objectToString(expected));
    }
}