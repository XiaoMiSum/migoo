package core.xyz.migoo.template;

import com.alibaba.fastjson2.JSON;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.function.Function;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TemplateEngine {

    public static final Pattern FUNC_EXPRESSION = Pattern.compile("__(\\w+)\\(([.]*[^)]*)?\\)");

    public static final Pattern VARS_EXPRESSION = Pattern.compile("\\$\\{(\\w+)?}");

    private final ContextWrapper context;
    private final Map<String, Function> functions;

    public TemplateEngine(ContextWrapper context, Map<String, Function> functions) {
        this.context = context;
        this.functions = functions;
    }

    public Map<String, Object> evaluate(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        map.replaceAll((k, v) -> evaluate(v));
        return map;
    }

    public List<Object> evaluate(List<Object> list) {
        if (list == null) {
            return null;
        }
        list.replaceAll(this::evaluate);
        return list;
    }

    public Object evaluate(Object obj) {
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

    public Object evaluate(String template) {
        return parseExpression(template);
    }

    private Object parseExpression(String expr) {
        // 尝试完全匹配函数
        var matcher = FUNC_EXPRESSION.matcher(expr);
        if (matcher.matches()) {
            return calc(matcher, expr);
        }

        // 尝试完全匹配变量
        matcher = VARS_EXPRESSION.matcher(expr);
        if (matcher.matches()) {
            return getValue(matcher, expr);
        }

        // 尝试匹配包含函数的字符串
        matcher = FUNC_EXPRESSION.matcher(expr);
        while (matcher.find()) {
            var value = calc(matcher, expr);
            if (!(value instanceof String || value instanceof Number || value instanceof Boolean)) {
                throw new IllegalArgumentException("表达式不支持组合使用: " + expr);
            }
            if (value instanceof String str && JSON.isValid(str)) {
                throw new IllegalArgumentException("表达式不支持组合使用: " + expr);
            }
            expr = expr.replace(matcher.group(), value.toString());
        }

        // 尝试匹配包含变量的字符串
        matcher = VARS_EXPRESSION.matcher(expr);
        while (matcher.find()) {
            var value = getValue(matcher, expr);
            if (!(value instanceof String || value instanceof Number || value instanceof Boolean)) {
                throw new IllegalArgumentException("表达式不支持组合使用: " + expr);
            }
            if (value instanceof String str && JSON.isValid(str)) {
                throw new IllegalArgumentException("表达式不支持组合使用: " + expr);
            }
            expr = expr.replace(matcher.group(), value.toString());
        }
        return expr;
    }

    private Object getValue(Matcher matcher, String expr) {
        var value = context.getAllVariablesWrapper().get(matcher.group(1));
        if (value == null) {
            throw new IllegalArgumentException("变量未定义或值为null: " + expr);
        }
        return value;
    }

    private Object calc(Matcher matcher, String expr) {
        var funcName = matcher.group(1);
        var handler = functions.get(funcName);
        if (handler == null) {
            throw new IllegalArgumentException("函数未定义: " + funcName);
        }
        var args = Function.newArgs(context, matcher.group(2));
        var value = handler.apply(args);
        if (value == null) {
            throw new IllegalArgumentException("表达式计算结果为 null: " + expr);
        }
        return value;
    }
}
