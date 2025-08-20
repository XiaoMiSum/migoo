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
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 基础规则类，提供验证规则的通用功能
 * 
 * <p>该类为各种验证规则提供基础功能，包括对象到字符串的转换、
 * 数字格式化等通用操作。</p>
 * 
 * @author xiaomi
 */
public abstract class BaseRule {

    /**
     * 线程本地变量，用于存储数字格式化器
     */
    private final static ThreadLocal<DecimalFormat> FORMAT_THREAD_LOCAL =
            ThreadLocal.withInitial(BaseRule::createDecimalFormat);

    /**
     * 创建数字格式化器
     * 
     * @return 数字格式化器
     */
    private static DecimalFormat createDecimalFormat() {
        var decimalFormatter = new DecimalFormat("#.#");
        // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMaximumFractionDigits(340);
        decimalFormatter.setMinimumFractionDigits(1);
        return decimalFormatter;
    }

    /**
     * 将对象转换为字符串表示
     * 
     * <p>转换规则：
     * <ul>
     *   <li>String类型: 返回原字符串，空字符串返回"defaultString"</li>
     *   <li>List类型: 空列表返回"defaultString"，否则返回JSON字符串</li>
     *   <li>Map类型: 空映射返回"defaultString"，否则返回JSON字符串</li>
     *   <li>Double/Float类型: 使用数字格式化器格式化</li>
     *   <li>null: 返回"defaultString"</li>
     *   <li>其他类型: 调用toString()方法</li>
     * </ul>
     * </p>
     * 
     * @param subj 待转换的对象
     * @return 对象的字符串表示
     */
    public String objectToString(Object subj) {
        return objectToString(subj, "null");
    }

    /**
     * 将对象转换为字符串表示
     * 
     * <p>转换规则：
     * <ul>
     *   <li>String类型: 返回原字符串，空字符串返回"defaultString"</li>
     *   <li>List类型: 空列表返回"defaultString"，否则返回JSON字符串</li>
     *   <li>Map类型: 空映射返回"defaultString"，否则返回JSON字符串</li>
     *   <li>Double/Float类型: 使用数字格式化器格式化</li>
     *   <li>null: 返回"defaultString"</li>
     *   <li>其他类型: 调用toString()方法</li>
     * </ul>
     * </p>
     * 
     * @param subj 待转换的对象
     * @param defaultString 默认字符串，当对象为空时返回该值
     * @return 对象的字符串表示
     */
    public String objectToString(Object subj, String defaultString) {
        defaultString = StringUtils.isBlank(defaultString) ? "null" : defaultString;
        return switch (subj) {
            case String string -> StringUtils.isBlank(string) ? defaultString : string;
            case List<?> objects -> objects.isEmpty() ? defaultString : JSON.toJSONString(objects);
            case Map<?, ?> entry -> entry.isEmpty() ? defaultString : JSON.toJSONString(entry);
            case Double ignored -> FORMAT_THREAD_LOCAL.get().format(subj);
            case Float ignored -> FORMAT_THREAD_LOCAL.get().format(subj);
            case null -> defaultString;
            default -> subj.toString();
        };
    }

}