package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.framework.entity.Validate;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.StringUtil;

import java.util.regex.Matcher;

import static xyz.migoo.framework.functions.CompoundVariable.FUNC_PATTERN;
import static xyz.migoo.framework.functions.CompoundVariable.PARAM_PATTERN;

/**
 * @author xiaomi
 */
public class VariableHelper {

    private VariableHelper() {
    }

    public static void bind(Object use, JSONObject vars) {
        if (use == null || vars == null) {
            return;
        }
        if (use instanceof JSONObject) {
            bind((JSONObject) use, vars);
        }
        if (use instanceof JSONArray) {
            bind((JSONArray) use, vars);
        }
    }

    private static void bind(JSONObject use, JSONObject vars) {
        for (String key : vars.keySet()) {
            if (FUNC_PATTERN.matcher(vars.getString(key)).find()) {
                continue;
            }
            bind(key, vars, use);
        }
    }

    private static void bind(JSONArray use, JSONObject vars) {
        for (String key : vars.keySet()) {
            if (FUNC_PATTERN.matcher(vars.getString(key)).find()) {
                continue;
            }
            bind(key, vars, use);
        }
    }

    private static void bind(String varsKey, JSONObject vars, JSONObject use) {
        String regex = "${" + varsKey + "}";
        for (String uKey : use.keySet()) {
            Object object = use.get(uKey);
            if (object instanceof String) {
                String v = (String) object;
                if (v.contains(regex)) {
                    v = v.replace(regex, vars.getString(varsKey));
                    use.put(uKey, v);
                    MiGooLog.log("bind variable: {} = {} -> {}", uKey, regex, v);
                    continue;
                }
            }
            if (object instanceof JSONObject) {
                bind((JSONObject) object, vars);
            }
            if (object instanceof JSONArray) {
                bind((JSONArray) object, vars);
            }
        }
    }

    private static void bind(String varsKey, JSONObject vars, JSONArray use) {
        String regex = "${" + varsKey + "}";
        for (int i = 0; i < use.size(); i++) {
            Object object = use.get(i);
            if (object instanceof String) {
                String v = (String) object;
                if (v.contains(regex)) {
                    v = v.replace(regex, vars.getString(varsKey));
                    use.remove(i);
                    use.add(i, v);
                    MiGooLog.log("bind variable: {} -> {}", regex, v);
                    continue;
                }
            }
            if (object instanceof JSONObject) {
                bind((JSONObject) object, vars);
            }
            if (object instanceof JSONArray) {
                bind((JSONArray) object, vars);
            }
        }
    }

    /**
     * 执行 validate.expect 指定的方法，将值put到 validate 对象中
     *
     * @param use 使用方法变量的对象
     */
    private static void evalVariables(JSONObject use, JSONObject variables) throws ExecuteError {
        if (use == null) {
            return;
        }
        for (String key : use.keySet()) {
            String value = use.getString(key);
            if (!StringUtil.isEmpty(value)) {
                Matcher func = FUNC_PATTERN.matcher(value);
                if (func.find()) {
                    Object result = FunctionFactory.execute(func.group(1), func.group(2), variables);
                    use.put(key, result);
                }
            }
        }
    }

    public static void bindAndEval(JSONObject use, JSONObject variables) throws ExecuteError {
        if (use == null) {
            return;
        }
        bind(use, variables);
        evalVariables(use, variables);
        bind(use, variables);
    }

    /**
     * 执行 validate.expect 指定的方法，将值put到 validate 对象中
     *
     * @param validate 检查点
     * @throws ExecuteError 检查异常
     */
    public static void evalValidate(Validate validate, JSONObject variables) throws ExecuteError {
        try {
            String value = String.valueOf(validate.getExpect());
            if (!StringUtil.isEmpty(value)) {
                Matcher func = FUNC_PATTERN.matcher(value);
                if (func.find()) {
                    Object result = FunctionFactory.execute(func.group(1), func.group(2), variables);
                    validate.setExpect(result);
                    return;
                }
                if (PARAM_PATTERN.matcher(value).find()){
                    validate.setExpect(variables.get(value.substring(2, value.length() -1)));
                }
            }
        } catch (Exception e) {
            throw new ExecuteError(e.getMessage(), e);
        }
    }

    public static void hook(String object, JSONObject variables) throws ExecuteError {
        if (object == null) {
            return;
        }
        Matcher func = FUNC_PATTERN.matcher(object);
        if (func.find()) {
            FunctionFactory.execute(func.group(1), func.group(2), variables);
        }
    }

    public static void main(String[] args) {
        String value = "__json(test=${test})";
        if (PARAM_PATTERN.matcher(value).find()){
            System.out.println(1);
        }
    }
}
