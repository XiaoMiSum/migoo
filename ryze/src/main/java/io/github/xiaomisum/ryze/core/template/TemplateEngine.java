/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.core.template;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.support.Collections;
import io.github.xiaomisum.ryze.support.Computable;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 模板引擎， 用于替换变量和函数执行
 *
 * @author xiaomi
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface TemplateEngine {

    /**
     * freemarker 模板引擎表达式匹配正则表达式，用于判断模板中是否有模板变量或函数调用
     */
    Pattern EXPRESSION = Pattern.compile("[\\s\\S]*\\$\\{.+}[\\s\\S]*");

    static boolean hasExpression(String template) {
        return EXPRESSION.matcher(template).matches();
    }

    static boolean noneExpression(String template) {
        return !hasExpression(template);
    }

    default Map<String, Object> evaluate(ContextWrapper context, Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        // 替换不可变Map
        if (Collections.isUnmodifiableMap(map)) {
            map = Collections.newHashMap(map);
        }
        map.replaceAll((k, v) -> evaluate(context, v));
        return map;
    }

    default List<Object> evaluate(ContextWrapper context, List<Object> list) {
        if (list == null) {
            return null;
        }
        // 替换不可变List
        if (Collections.isUnmodifiableList(list)) {
            list = Collections.newArrayList(list);
        }
        list.replaceAll(v -> evaluate(context, v));
        return list;
    }

    default Object evaluate(ContextWrapper context, Object object) {
        return switch (object) {
            case String template -> evaluate(context, template);
            case Computable<?> computable -> computable.evaluate(context);
            case Map map -> evaluate(context, map);
            case List list -> evaluate(context, list);
            case null -> null;
            default -> object;
        };
    }

    Object evaluate(ContextWrapper context, String expression);

    Object evaluate(Map<String, Object> model, String expression);

}
