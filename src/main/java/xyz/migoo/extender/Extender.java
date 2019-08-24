package xyz.migoo.extender;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.framework.config.Platform;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ognl.Ognl.getValue;

/**
 * @author xiaomi
 */
public class Extender {


    public static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");

    public static final Pattern PARAM_PATTERN = Pattern.compile("(\\$\\{(\\w+)})+");

    private static Map<String, Method> methods = null;

    static {
        try {
            methods = InvokeUtil.loadFunction(Platform.EXTENDS_CLASS);
        } catch (InvokeException e) {
            MiGooLog.log("load extends function exception. ", e);
        }
    }

    private Extender() {
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

    private static void bind(String key, JSONObject vars, JSONObject use) {
        String regex = "${" + key + "}";
        for (String uKey : use.keySet()) {
            Object object = use.get(uKey);
            if (object instanceof String) {
                String v = (String) object;
                if (v.equals(regex)) {
                    use.put(uKey, vars.getString(key));
                    MiGooLog.log("bind variable: {} -> {}", v, vars.getString(key));
                    continue;
                }
                if (v.contains(regex)) {
                    use.put(uKey, v.replace(regex, vars.getString(key)));
                    MiGooLog.log("bind variable: {} -> {}", object, vars.getString(key));
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

    private static void bind(String key, JSONObject vars, JSONArray use) {
        String regex = "${" + key + "}";
        for (int i = 0; i < use.size(); i++) {
            Object object = use.get(i);
            if (object instanceof String) {
                String v = (String) object;
                if (v.equals(regex)) {
                    MiGooLog.log("bind variable: {} -> {}", v, vars.getString(key));
                    use.remove(i);
                    use.add(i, vars.getString(key));
                    continue;
                }
                if (v.contains(regex)) {
                    MiGooLog.log("bind variable: {} -> {}", object, v.replace(regex, vars.getString(key)));
                    use.remove(i);
                    use.add(i, v.replace(regex, vars.getString(key)));
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
    private static void evalVariables(JSONObject use, JSONObject variables) throws InvokeException {
        if (use == null) {
            return;
        }
        for (String key : use.keySet()) {
            String value = use.getString(key);
            if (StringUtil.isEmpty(value)) {
                return;
            }
            Matcher func = FUNC_PATTERN.matcher(value);
            if (func.find()) {
                Object result = InvokeUtil.invoke(methods, func.group(1), func.group(2), variables);
                use.put(key, result);
            }
        }
    }

    public static void bindAndEval(JSONObject use, JSONObject variables) throws InvokeException {
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
    public static void evalValidate(JSONObject validate, JSONObject variables) throws ExecuteError {
        try {
            String value = validate.getString(CaseKeys.VALIDATE_EXPECT);
            if (StringUtil.isEmpty(value)) {
                return;
            }
            Matcher func = FUNC_PATTERN.matcher(value);
            if (func.find()) {
                Object result = InvokeUtil.invoke(methods, func.group(1), func.group(2), variables);
                validate.put(CaseKeys.VALIDATE_EXPECT, result);
            }
        } catch (Exception e) {
            throw new ExecuteError(e.getMessage(), e);
        }
    }

    public static void hook(String object, JSONObject variables) throws InvokeException {
        if (object == null) {
            return;
        }
        Matcher func = FUNC_PATTERN.matcher(object);
        if (func.find()) {
            InvokeUtil.invoke(methods, func.group(1), func.group(2), variables);
        }
    }
}
