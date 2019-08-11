package xyz.migoo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.config.Platform;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.utils.InvokeUtil;
import xyz.migoo.utils.MiGooLog;
import xyz.migoo.utils.StringUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2019-05-07 21:21
 */
public class BindVariable {

    public static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");

    public static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{(\\w+)}");

    private static Map<String, Method> methods = null;

    public static void bindVariables(JSONObject vars, Object useVarObject) {
        int defaultLoop = 3;
        for (int i = 0; i < defaultLoop; i++) {
            try {
                bind(vars, useVarObject, true);
            }catch (InvokeException e) {
                MiGooLog.log(e.getMessage());
            }
        }
    }

    public static void bind(JSONObject vars, Object useVarObject){
        try {
            bind(vars, useVarObject, false);
        } catch (InvokeException e) {
            MiGooLog.log(e.getMessage());
        }
    }

    public static void bind(JSONObject vars, Object useVarObject, boolean isEval) throws InvokeException {
        if (useVarObject instanceof JSONArray) {
            bind(vars, (JSONArray) useVarObject, isEval);
        }
        if (useVarObject instanceof JSONObject) {
            bind(vars, (JSONObject) useVarObject, isEval);
        }
    }

    private static void bind(JSONObject vars, JSONArray useVarObject, boolean isEval) throws InvokeException {
        if (vars == null || useVarObject == null) {
            return;
        }
        for (String varsKey : vars.keySet()) {
            eval(vars, varsKey, isEval);
            String regex = "${" + varsKey + "}";
            for (int i = 0; i < useVarObject.size(); i++) {
                Object object = get(vars.get(varsKey), regex, useVarObject.get(i));
                useVarObject.remove(i);
                useVarObject.add(i, object);
            }
        }
    }

    private static void eval(JSONObject vars, String varsKey, boolean isEval) throws InvokeException {
        Object varsValue = vars.get(varsKey);
        if (varsValue instanceof String) {
            Matcher matcher = FUNC_PATTERN.matcher((String) varsValue);
            if (matcher.find() && isEval) {
                vars.put(varsKey, evalVariable((String) varsValue, vars));
            }
        }
    }

    private static void bind(JSONObject vars, JSONObject useVarObject, boolean isEval) throws InvokeException {
        if (vars == null || useVarObject == null) {
            return;
        }
        for (String varsKey : vars.keySet()) {
            eval(vars, varsKey, isEval);
            String regex = "${" + varsKey + "}";
            for (String useKey : useVarObject.keySet()) {
                Object object = get(vars.get(varsKey), regex, useVarObject.get(useKey));
                useVarObject.put(useKey, object);
            }
        }
    }

    private static void bindByJSONObject(Object varsValue, String regex, JSONObject useVarObject) {
        for (String key : useVarObject.keySet()) {
            Object object = get(varsValue, regex, useVarObject.get(key));
            useVarObject.put(key, object);
        }
    }

    private static void bindByJSONArray(Object varsValue, String regex, JSONArray useVarObject) {
        for (int i = 0; i < useVarObject.size(); i++) {
            Object object = get(varsValue, regex, useVarObject.get(i));
            useVarObject.remove(i);
            useVarObject.add(i, object);
        }
    }

    private static Object get(Object varsValue, String regex, Object object) {
        if (!(object instanceof JSON)) {
            object = getValue(varsValue, regex, object);
        }
        if (object instanceof JSONObject) {
            bindByJSONObject(varsValue, regex, (JSONObject) object);
        }
        if (object instanceof JSONArray) {
            bindByJSONArray(varsValue, regex, (JSONArray) object);
        }
        return object;
    }

    private static Object getValue(Object varsValue, String regex, Object object) {
        String stringObj = String.valueOf(object);
        if (regex.equals(stringObj)) {
            return varsValue;
        }
        if (StringUtil.contains(stringObj, regex)) {
            return stringObj.replace(regex, String.valueOf(varsValue));
        }
        return object;
    }

    private static Object evalVariable(String evalFunc, JSONObject variables) throws InvokeException {
        try {
            Matcher func = FUNC_PATTERN.matcher(evalFunc);
            if (func.find()) {
                functionLoader();
                return InvokeUtil.invoke(methods, func.group(1), func.group(2), variables);
            }
        } catch (RuntimeException ignored) {

        }
        return evalFunc;
    }

    /**
     * 从指定扩展类中 获取扩展函数
     */
    private static void functionLoader() throws InvokeException {
        if (methods == null) {
            methods = InvokeUtil.functionLoader(Platform.EXTENDS_VARIABLE);
        }
    }
}
