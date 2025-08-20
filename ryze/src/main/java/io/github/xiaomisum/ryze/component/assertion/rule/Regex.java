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

import java.util.regex.Pattern;

/**
 * 正则表达式验证规则实现类
 *
 * <p>该类实现了正则表达式验证规则，用于判断实际值是否匹配期望的正则表达式。</p>
 *
 * <p>支持的规则关键字: "regex", "rx"</p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "field": "phone",
 *   "expected": "1[3-9]\\d{9}",
 *   "rule": "regex"
 * }
 *
 * {
 *   "testclass": "http"
 *   "field": "email",
 *   "expected": "\\w+@\\w+\\.\\w+",
 *   "rule": "rx"
 * }
 * </pre>
 * </p>
 *
 * @author xiaomi
 * @see Rule 验证规则接口
 * @since 2019-08-13 22:17
 */
@KW({"regex", "rx"})
public class Regex extends BaseRule implements Rule {

    /**
     * 执行正则表达式验证
     *
     * <p>使用期望值作为正则表达式模式，检查实际值是否完全匹配该模式。</p>
     *
     * @param actual   实际值
     * @param expected 期望的正则表达式
     * @return 验证结果，true表示匹配，false表示不匹配
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        var pattern = Pattern.compile(objectToString(expected));
        var matcher = pattern.matcher(objectToString(actual));
        return matcher.matches();
    }
}