package xyz.migoo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.utils.InvokeUtil;
import xyz.migoo.utils.Log;
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

    private static Log log = new Log(BindVariable.class);

    public static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");

    public static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{(\\w+)}");

    private static Map<String, Method> methods = null;

    public static void merge(JSONObject vars1, JSONObject vars2){
        // 1. 将 1 中的 绑定到 2
        loopBindVariables(vars1, vars2);
        // 2. 合并 1 到 2
        if (vars1 != null){
            vars2.putAll(vars1);
        }
        // 3. 将 2 中的 绑定到 2
        BindVariable.loopBindVariables(vars2, vars2);
    }

    public static void loopBindVariables(JSONObject vars, Object useVarObject) {
        try {
            bindVariables(vars, useVarObject);
        } catch (InvokeException e) {
            log.debug(e.getMessage());
        }
    }

    public static void bindVariables(JSONObject vars, Object useVarObject) throws InvokeException {
        int defaultLoop = 3;
        for (int i = 0; i < defaultLoop; i++) {
            bind(vars, useVarObject);
        }
    }

    public static void bind(JSONObject vars, Object useVarObject) throws InvokeException {
        if (useVarObject instanceof JSONArray){
            bind(vars, (JSONArray) useVarObject);
        }
        if (useVarObject instanceof JSONObject){
            bind(vars, (JSONObject) useVarObject);
        }
    }

    private static void bind(JSONObject vars, JSONArray useVarObject) throws InvokeException {
        if (vars == null || vars.isEmpty()){
            return;
        }
        for (String varsKey : vars.keySet()){
            Object varsValue = vars.get(varsKey);
            if (varsValue instanceof String){
                Matcher matcher = FUNC_PATTERN.matcher((String) varsValue);
                if (matcher.find()) {
                    varsValue = evalVariable((String) varsValue, vars);
                }
            }
            String regex = "${" + varsKey + "}";
            for (int i = 0; i < useVarObject.size(); i++) {
                Object object = get(varsValue, regex, useVarObject.get(i));
                useVarObject.remove(i);
                useVarObject.add(i, object);
            }
        }
    }

    private static void bind(JSONObject vars, JSONObject useVarObject) throws InvokeException {
        if (vars == null || vars.isEmpty()){
            return;
        }
        for (String varsKey : vars.keySet()){
            Object varsValue = vars.get(varsKey);
            if (varsValue instanceof String){
                Matcher matcher = FUNC_PATTERN.matcher((String) varsValue);
                if (matcher.find()) {
                    varsValue = evalVariable((String) varsValue, vars);
                    vars.put(varsKey, varsValue);
                }
            }
            String regex = "${" + varsKey + "}";
            for (String useKey : useVarObject.keySet()){
                Object object = get(varsValue, regex, useVarObject.get(useKey));
                useVarObject.put(useKey, object);
            }
        }
    }

    private static void bindByJSONObject(Object varsValue, String regex, JSONObject useVarObject){
        for (String key : useVarObject.keySet()){
            Object object = get(varsValue, regex, useVarObject.get(key));
            useVarObject.put(key, object);
        }
    }

    private static void bindByJSONArray(Object varsValue, String regex, JSONArray useVarObject){
        for (int i = 0; i < useVarObject.size(); i++) {
            Object object = get(varsValue, regex, useVarObject.get(i));
            useVarObject.remove(i);
            useVarObject.add(i, object);
        }
    }

    private static Object get(Object varsValue, String regex, Object object){
        if (!(object instanceof JSON)){
            object = getValue(varsValue, regex, object);
        }
        if (object instanceof JSONObject) {
            bindByJSONObject(varsValue, regex, (JSONObject) object);
        }
        if (object instanceof JSONArray){
            bindByJSONArray(varsValue, regex, (JSONArray)object);
        }
        return object;
    }

    private static Object getValue(Object varsValue, String regex, Object object){
        String stringObj = String.valueOf(object);
        if (regex.equals(stringObj)){
            return varsValue;
        }
        if (StringUtil.contains(stringObj, regex)){
            return stringObj.replace(regex, String.valueOf(varsValue));
        }
        return object;
    }

    public static void evalVariable(Object eval, JSONObject variables) throws InvokeException {
        if (eval == null) {
            return;
        }
        if (eval instanceof JSONObject){
            evalVariable((JSONObject) eval, variables);
            return;
        }
        if (eval instanceof JSONArray){
            evalVariable((JSONArray) eval, variables);
            return;
        }
        evalVariable((String) eval, variables);
    }

    private static void evalVariable(JSONObject eval, JSONObject variables) throws InvokeException {
        if (eval == null || eval.isEmpty()) {
            return;
        }
        for (String key : eval.keySet()) {
            Object value = eval.get(key);
            if (value instanceof JSONObject) {
                evalVariable((JSONObject)value, variables);
                eval.put(key, value);
                continue;
            }
            if (value instanceof JSONArray){
                JSONArray array = (JSONArray) value;
                evalVariable(array, variables);
                eval.put(key, array);
                continue;
            }
            if (!(value instanceof String)){
                continue;
            }
            Object result = evalVariable((String) value, variables);
            eval.put(key, result);
        }
    }

    private static void evalVariable(JSONArray eval, JSONObject variables) throws InvokeException {
        for (int i = 0; i < eval.size(); i++) {
            Object object = eval.get(i);
            eval.remove(i);
            if (object instanceof JSONObject) {
                evalVariable((JSONObject)object, variables);
            }
            if (object instanceof String){
                object = evalVariable((String) object, variables);
            }
            eval.add(i, object);
        }
    }

    private static Object evalVariable(String evalFunc, JSONObject variables) throws InvokeException {
        try {
            Matcher func = FUNC_PATTERN.matcher(evalFunc);
            if (func.find()) {
                functionLoader();
                Object[] parameter = InvokeUtil.parameter(func.group(2), variables);
                return InvokeUtil.invoke(methods, func.group(1), parameter);
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
