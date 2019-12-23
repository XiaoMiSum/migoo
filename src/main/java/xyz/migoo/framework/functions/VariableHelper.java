package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.entity.Validate;
import xyz.migoo.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static xyz.migoo.framework.functions.CompoundVariable.PARAM_PATTERN;
import static xyz.migoo.framework.functions.CompoundVariable.FUNC_PATTERN;

/**
 * @author xiaomi
 */
public class VariableHelper {

    private VariableHelper() {
    }

    public static String bind(String source, JSONObject variables){
        List<String> temp = new ArrayList<>();
        Matcher matcher = PARAM_PATTERN.matcher(source);
        while (matcher.find()) {
            temp.add(matcher.group());
        }
        for (String value : temp){
            source =  source.replace(value, variables.getString(value.substring(2, value.length() -1)));
        }
        return source;
    }

    public static void bindAndEval(JSONObject source, JSONObject variables) throws ExecuteError {
        if (source == null) {
            return;
        }
        bind(source, variables);
        evalVariables(source, variables);
        bind(source, variables);
    }

    private static void bind(JSONObject source, JSONObject variables) {
        JSONObject usingVarKey = getUsingVarKey(source);
        usingVarKey.forEach((key, value) -> {
            if (value instanceof JSONArray) {
                bind((JSONArray) value, variables);
            }
            if (value instanceof JSONObject) {
                ((JSONObject) value).forEach((k, v) -> {
                    if (v instanceof List) {
                        bind(source.getJSONObject(key), k, ((JSONObject) value).getJSONArray(k), variables);
                    }
                });
            }
            if (value instanceof ArrayList) {
                bind(source, key, new JSONArray((List<Object>) value), variables);
            }
        });
    }

    private static JSONObject getUsingVarKey(JSONObject source) {
        JSONObject usingVarKey = new JSONObject(true);
        source.forEach((key, value) -> {
            if (value instanceof String) {
                getUsingVarKey(key, value, usingVarKey);
            }
            if (value instanceof JSONObject) {
                JSONObject valueJson = new JSONObject(true);
                ((JSONObject) value).forEach((k, v) -> getUsingVarKey(k, v, valueJson));
                usingVarKey.put(key, valueJson);
            }
            if (value instanceof JSONArray) {
                getUsingVarKey(key, (JSONArray) value, usingVarKey);
            }
        });
        return usingVarKey;
    }

    private static void getUsingVarKey(String key, Object value, JSONObject usingVarKey) {
        if (value instanceof String) {
            List<String> temp = new ArrayList<>();
            Matcher matcher = PARAM_PATTERN.matcher((String) value);
            while (matcher.find()) {
                temp.add(matcher.group());
            }
            if (!temp.isEmpty()) {
                usingVarKey.put(key, temp);
            }
        }
    }

    private static void getUsingVarKey(String key, JSONArray value, JSONObject usingVarKey) {
        for (int i = 0; i < value.size(); i++) {
            List<String> temp = new ArrayList<>();
            Matcher matcher = PARAM_PATTERN.matcher(value.getString(i));
            while (matcher.find()) {
                temp.add(matcher.group());
            }
            if (!temp.isEmpty()) {
                value.remove(i);
                value.add(i, temp);
            }
        }
        usingVarKey.put(key, value);
    }

    private static void bind(JSONArray value, JSONObject variables) {
        for (int i = 0; i < value.size(); i++) {
            if (value.get(i) instanceof List) {
                JSONArray varList = value.getJSONArray(i);
                String varValue = getVarValue(varList, variables);
                if (varValue.length() > 0) {
                    value.remove(i);
                    value.add(i, varValue);
                }
            }
        }
    }

    private static void bind(JSONObject source, String key, JSONArray value, JSONObject variables) {
        for (int i = 0; i < value.size(); i++) {
            String k = value.getString(i).substring(2, value.getString(i).length() - 1);
            if (!FUNC_PATTERN.matcher(variables.getString(k)).find()) {
                if (source.get(key) instanceof List){
                    source.put(key, getVarValue(source.getJSONArray(key), variables));
                } else if (source.get(key) instanceof String) {
                    source.put(key, source.getString(key).replace(value.getString(i), variables.getString(k)));
                }
            }
        }
    }

    private static String getVarValue(JSONArray value, JSONObject variables) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            String k = value.getString(i).substring(2, value.getString(i).length() - 1);
            if (!FUNC_PATTERN.matcher(variables.getString(k)).find()) {
                str.append(variables.getString(k));
            }
        }
        return str.toString();
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
                if (FUNC_PATTERN.matcher(value).find()) {
                    Object result = FunctionFactory.execute(value, variables);
                    use.put(key, result);
                }
            }
        }
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
                if (PARAM_PATTERN.matcher(value).find()) {
                    validate.setExpect(variables.get(value.substring(2, value.length() - 1)));
                } else if (FUNC_PATTERN.matcher(value).find()) {
                    Object result = FunctionFactory.execute(value, variables);
                    validate.setExpect(result);
                }
            }
        } catch (Exception e) {
            throw new ExecuteError(e.getMessage(), e);
        }
    }
}
