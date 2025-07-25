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

package core.xyz.migoo.template;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.function.Function;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 模板引擎， 用于替换变量和函数执行
 *
 * @author xiaomi
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface TemplateEngine {

    default Map<String, Object> evaluate(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        map.replaceAll((k, v) -> evaluate(v));
        return map;
    }

    default List<Object> evaluate(List<Object> list) {
        if (list == null) {
            return null;
        }
        list.replaceAll(this::evaluate);
        return list;
    }

    default Object evaluate(Object obj) {
        if (obj instanceof String template) {
            return evaluate(template);
        }
        if (obj instanceof Map map) {
            return evaluate(map);
        }
        if (obj instanceof List list) {
            return evaluate(list);
        }
        return obj;
    }

    default Object evaluate(String template) {
        if (StringUtils.isBlank(template)) {
            return template;
        }
        return parseExpression(template);
    }

    void setContext(ContextWrapper context);

    void setFunctions(Map<String, Function> functions);

    Object parseExpression(String expr);

}
