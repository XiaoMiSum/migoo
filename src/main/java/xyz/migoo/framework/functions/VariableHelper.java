/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

    public static String bind(String source, JSONObject variables) {
        Matcher matcher = PARAM_PATTERN.matcher(source);
        while (matcher.find()) {
            String value = matcher.group();
            source = source.replace(value, variables.getString(value.substring(2, value.length() - 1)));
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
                bind(source.getJSONObject(key), (JSONObject) value, variables);
            }
            if (value instanceof ArrayList) {
                bind(source, key, new JSONArray((List<Object>) value), variables);
            }
        });
    }

    /**
     * Get using variables from source object
     *
     * @param source use variables source object
     * @return using variables
     */
    private static JSONObject getUsingVarKey(JSONObject source) {
        JSONObject usingVarKey = new JSONObject(true);
        source.forEach((key, value) -> {
            if (value instanceof String) {
                getUsingVarKey(key, value, usingVarKey);
            }
            if (value instanceof JSONObject) {
                getUsingVarKey(key, (JSONObject) value, usingVarKey);
            }
            if (value instanceof JSONArray) {
                getUsingVarKey(key, (JSONArray) value, usingVarKey);
            }
        });
        return usingVarKey;
    }

    private static void getUsingVarKey(String key, JSONObject value, JSONObject usingVarKey) {
        JSONObject valueJson = new JSONObject(true);
        value.forEach((k, v) -> getUsingVarKey(k, v, valueJson));
        if (!valueJson.isEmpty()) {
            usingVarKey.put(key, valueJson);
        }
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
        JSONArray valueArray = new JSONArray();
        for (int i = 0; i < value.size(); i++) {
            List<String> temp = new ArrayList<>();
            Matcher matcher = PARAM_PATTERN.matcher(value.getString(i));
            while (matcher.find()) {
                temp.add(matcher.group());
            }
            if (!temp.isEmpty()) {
                valueArray.add(temp);
            }
        }
        if (!valueArray.isEmpty()){
            usingVarKey.put(key, valueArray);
        }
    }

    private static void bind(JSONObject source, JSONObject usingVarValue, JSONObject variables) {
        usingVarValue.forEach((key, value) -> {
            if (value instanceof List) {
                bind(source, key, new JSONArray((List)value), variables);
            }
        });
    }

    private static void bind(JSONArray value, JSONObject variables) {
        for (int i = 0; i < value.size(); i++) {
            if (value.get(i) instanceof List) {
                String varValue = getVarValue(value.getJSONArray(i), variables);
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
            if (!StringUtil.isEmpty(variables.getString(k)) && !FUNC_PATTERN.matcher(variables.getString(k)).find()) {
                if (source.get(key) instanceof List) {
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
            if (!StringUtil.isEmpty(variables.getString(k)) && !FUNC_PATTERN.matcher(variables.getString(k)).find()) {
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
                if (FUNC_PATTERN.matcher(value).find()) {
                    Object result = FunctionFactory.execute(value, variables);
                    validate.setExpect(result);
                    return;
                }
                if (PARAM_PATTERN.matcher(value).find()) {
                    validate.setExpect(variables.get(value.substring(2, value.length() - 1)));
                }
            }
        } catch (Exception e) {
            throw new ExecuteError(e.getMessage(), e);
        }
    }
}
