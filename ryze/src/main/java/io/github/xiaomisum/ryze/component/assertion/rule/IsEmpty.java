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

import com.alibaba.fastjson2.JSON;
import io.github.xiaomisum.ryze.core.assertion.Rule;
import io.github.xiaomisum.ryze.core.testelement.KW;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 空值验证规则实现类
 *
 * <p>该类实现了空值验证规则，用于判断实际值是否为空。
 * 支持字符串、Map、List等类型的空值检查。</p>
 *
 * <p>支持的规则关键字: "isEmpty", "isNull", "empty", "blank"</p>
 *
 * <p>验证逻辑：
 * <ul>
 *   <li>String类型: 检查字符串是否为空或只包含空白字符</li>
 *   <li>Map类型: 检查Map是否为空</li>
 *   <li>List类型: 检查List是否为空</li>
 *   <li>null值: 返回true</li>
 *   <li>其他类型: 返回false</li>
 * </ul>
 * </p>
 *
 * <p>使用示例：
 * <pre>
 * {
 *   "testclass": "http"
 *   "field": "data",
 *   "expected": "",
 *   "rule": "isEmpty"
 * }
 * </pre>
 * </p>
 *
 * @author xiaomi
 * @see Rule 验证规则接口
 * @see IsNotEmpty 非空验证规则
 * @since 2019-08-13 22:17
 */
@KW({"isEmpty", "isNull", "empty", "blank"})
public class IsEmpty extends BaseRule implements Rule {

    /**
     * 执行空值验证
     *
     * <p>验证逻辑：
     * <ul>
     *   <li>String类型: 检查字符串是否为空或只包含空白字符</li>
     *   <li>Map类型: 检查Map是否为空</li>
     *   <li>List类型: 检查List是否为空</li>
     *   <li>null值: 返回true</li>
     *   <li>其他类型: 返回false</li>
     * </ul>
     * </p>
     *
     * @param actual   实际值
     * @param expected 期望值（该规则不使用期望值）
     * @return 验证结果，true表示为空，false表示非空
     */
    @Override
    public boolean assertThat(Object actual, Object expected) {
        return switch (actual) {
            case String obj -> JSON.isValidArray(obj) ? JSON.parseArray(obj).isEmpty() :
                    JSON.isValidObject(obj) ? JSON.parseObject(obj).isEmpty() : StringUtils.isBlank(obj);
            case Map<?, ?> obj -> obj.isEmpty();
            case List<?> obj -> obj.isEmpty();
            case null -> true;
            default -> false;
        };
    }
}