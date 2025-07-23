package core.xyz.migoo.template;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.function.FunctionHandler;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodMatcher {

    public static final Pattern FUNC_EXPRESSION = Pattern.compile("__(\\w+)\\(([.]*[^)]*)?\\)");
    private final Matcher matcher;
    private final ContextWrapper context;
    private final Map<String, FunctionHandler> functions;
    private final String expr;
    private boolean matches = false;
    private boolean find = false;

    public MethodMatcher(String expr, ContextWrapper context, Map<String, FunctionHandler> functions) {
        matcher = FUNC_EXPRESSION.matcher(expr);
        this.context = context;
        this.functions = functions;
        this.expr = expr;
        if (matcher.matches()) {
            matches = true;
        } else if (matcher.find()) {
            find = true;
        }
    }

    public boolean isMatches() {
        return matches;
    }

    public boolean isFind() {
        return find;
    }

    private Object calc() {
        var funcName = matcher.group(1);
        var handler = functions.get(funcName);
        if (handler == null) {
            throw new IllegalArgumentException("函数未定义: " + funcName);
        }
        var args = FunctionHandler.newArgs(context, matcher.group(2));
        var value = handler.apply(args);
        if (value == null) {
            throw new IllegalArgumentException("表达式计算结果为 null: " + expr);
        }
        return value;
    }
}
