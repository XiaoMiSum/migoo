/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package core.xyz.migoo.vars;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.functions.FunctionHelper;
import core.xyz.migoo.utils.TypeUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static core.xyz.migoo.utils.TypeUtil.VARS_PATTERN;
import static core.xyz.migoo.utils.TypeUtil.MULTI_VARS_PATTERN;

/**
 * @author xiaomi
 */
public class VarsHelper {

    private VarsHelper() {
    }

    /**
     * 绑定使用多个变量的数据，忽略单变量或变量值为JSON的
     *
     * @param source    使用多个变量占位符的字符串
     * @param variables 变量集合
     * @return 绑定变量后的数据
     */
    public static String bindMultiVariable(String source, JSONObject variables) {
        boolean isConnectedVariables = MULTI_VARS_PATTERN.matcher(source).find();
        Matcher matcher = VARS_PATTERN.matcher(source);
        while (matcher.find() && isConnectedVariables) {
            String value = matcher.group();
            if (variables.get(value.substring(2, value.length() - 1)) instanceof JSON) {
                continue;
            }
            source = source.replace(value, variables.getString(value.substring(2, value.length() - 1)));
        }
        return source;
    }

    public static void bindAndEval(JSONObject source, JSONObject variables) throws FunctionException {
        if (source == null) {
            return;
        }
        bind(source, variables);
        evalVariables(source, variables);
        bind(source, variables);
    }

    /**
     * 绑定 body 专用，防止多级嵌套 有变量没被绑定到
     *
     * @param source    body
     * @param variables 变量
     */
    public static void bindVariable(JSONObject source, JSONObject variables) throws FunctionException {
        bindAndEval(source, variables);
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value instanceof JSONArray) {
                bindVariable(((JSONArray) value), variables);
            } else if (value instanceof JSONObject) {
                bindVariable((JSONObject) value, variables);
            } else if (value instanceof String) {
                source.put(key, bindMultiVariable((String) value, variables));
            }
        }
    }

    private static void bindVariable(JSONArray source, JSONObject variables) throws FunctionException {
        for (int i = 0; i < source.size(); i++) {
            Object value = source.get(i);
            if (value instanceof JSONArray) {
                bindVariable((JSONArray) value, variables);
            } else if (value instanceof JSONObject) {
                bindVariable((JSONObject) value, variables);
            } else if (value instanceof String) {
                String v = bindMultiVariable((String) value, variables);
                source.remove(i);
                source.add(i, v);
            }
        }
    }

    private static void bind(JSONObject source, JSONObject variables) {
        JSONObject usingVarKey = getUsingVarKey(source);
        usingVarKey.forEach((key, value) -> {
            if (value instanceof JSONArray) {
                bind(source, (Object) key, (JSONArray) value, variables);
            }
            if (value instanceof JSONObject) {
                bind(source.getJSONObject(key), (JSONObject) value, variables);
            }
            if (value instanceof ArrayList) {
                bind(source, key, new JSONArray((List) value), variables);
            }
        });
    }

    /**
     * Get using variables from source object
     *
     * @param source using variables source object
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

    /**
     * 获取当前key 使用到的变量列表
     *
     * @param key         使用变量的key
     * @param value       包含变量占位符的JSON对象
     * @param usingVarKey 当前key 使用到的变量列表 默认空
     */
    private static void getUsingVarKey(String key, JSONObject value, JSONObject usingVarKey) {
        JSONObject valueJson = new JSONObject(true);
        value.forEach((k, v) -> getUsingVarKey(k, v, valueJson));
        if (!valueJson.isEmpty()) {
            usingVarKey.put(key, valueJson);
        }
    }

    /**
     * 获取当前key 使用到的变量列表
     *
     * @param key         使用变量的key
     * @param value       包含变量占位符的字符串
     * @param usingVarKey 当前key 使用到的变量列表 默认空
     */
    private static void getUsingVarKey(String key, Object value, JSONObject usingVarKey) {
        if (value instanceof String) {
            List<String> temp = new ArrayList<>();
            Matcher matcher = VARS_PATTERN.matcher((String) value);
            while (matcher.find()) {
                temp.add(matcher.group());
            }
            if (!temp.isEmpty()) {
                usingVarKey.put(key, temp);
            }
        }
    }

    /**
     * 获取当前key 使用到的变量列表
     *
     * @param key         使用变量的key
     * @param value       包含变量占位符的JSON列表
     * @param usingVarKey 当前key 使用到的变量列表 默认空
     */
    private static void getUsingVarKey(String key, JSONArray value, JSONObject usingVarKey) {
        JSONArray valueArray = new JSONArray();
        for (int i = 0; i < value.size(); i++) {
            List<String> temp = new ArrayList<>();
            Matcher matcher = VARS_PATTERN.matcher(value.getString(i));
            while (matcher.find()) {
                temp.add(matcher.group());
            }
            if (!temp.isEmpty()) {
                valueArray.add(temp);
            }
        }
        if (!valueArray.isEmpty()) {
            usingVarKey.put(key, valueArray);
        }
    }

    /**
     * 执行变量绑定
     *
     * @param source        可能使用变量的原数据
     * @param usingVarValue 原数据中使用到变量的集合
     * @param variables     变量集合
     */
    private static void bind(JSONObject source, JSONObject usingVarValue, JSONObject variables) {
        usingVarValue.forEach((key, value) -> {
            if (value instanceof List) {
                bind(source, key, new JSONArray((List) value), variables);
            }
        });
    }


    /**
     * 执行变量绑定
     *
     * @param source    可能使用变量的原数据
     * @param key       原数据中使用到变量的Key
     * @param value     当前key 使用到的变量列表
     * @param variables 变量集合
     */
    private static void bind(JSONObject source, Object key, JSONArray value, JSONObject variables) {
        for (int i = 0; i < value.size(); i++) {
            if (value.get(i) instanceof List) {
                String varValue = getVarValue(value.getJSONArray(i), variables);
                if (varValue.length() > 0) {
                    value.remove(i);
                    value.add(i, varValue);
                }
            }
        }
        source.put(key.toString(), value);
    }

    /**
     * 执行变量绑定
     *
     * @param source    可能使用变量的原数据
     * @param key       原数据中使用到变量的Key
     * @param value     当前key 使用到的变量列表
     * @param variables 变量集合
     */
    private static void bind(JSONObject source, String key, JSONArray value, JSONObject variables) {
        for (int i = 0; i < value.size(); i++) {
            String k = value.getString(i).substring(2, value.getString(i).length() - 1);
            if (!StringUtils.isEmpty(variables.getString(k)) && !TypeUtil.isFunc(variables.getString(k))) {
                if (source.get(key) instanceof List) {
                    source.put(key, getVarValue(source.getJSONArray(key), variables));
                } else if (source.get(key) instanceof String && !(variables.get(k) instanceof JSON)) {
                    source.put(key, source.getString(key).replace(value.getString(i), variables.getString(k)));
                }
            } else if (variables.containsKey(k) && StringUtils.isEmpty(variables.getString(k))) {
                source.put(key, variables.get(k));
            }
        }
    }

    /**
     * 获取 变量值，当变量值为 非JSON对象、非扩展函数时，将变量值绑定到value中
     * 当 变量值为 JSON 或者 扩展函数时，不进行变量绑定
     * 当变量值为空时，返回执行错误（每个变量都应当有值）
     *
     * @param value     使用变量的占位符
     * @param variables 变量集合
     * @return 绑定变量后的字符串
     */
    private static String getVarValue(JSONArray value, JSONObject variables) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            String k = value.getString(i).substring(2, value.getString(i).length() - 1);
            if (!(variables.get(k) instanceof JSON) && !TypeUtil.isFunc(variables.getString(k))) {
                str.append(variables.getString(k));
            } else if (StringUtils.isEmpty(variables.getString(k))) {
                throw new VarsException("variables value con not be null: " + k);
            } else {
                str.append(value.getString(i));
            }
        }
        return str.toString();
    }

    /**
     * 执行 validate.expect 指定的方法，将值put到 validate 对象中
     *
     * @param use 使用方法变量的对象
     */
    private static void evalVariables(JSONObject use, JSONObject variables) throws FunctionException {
        if (use == null) {
            return;
        }
        for (String key : use.keySet()) {
            String value = use.getString(key);
            if (!StringUtils.isEmpty(value) && TypeUtil.isFunc(value)) {
                String func = VarsHelper.bindMultiVariable(value, variables);
                Object result = FunctionHelper.execute(func, variables);
                use.put(key, result);
            }
        }
    }

    /**
     * 执行 validator.expected 指定的方法，将值put到 validator 对象中
     *
     * @param validator 检查点
     * @param variables 变量
     */
    public static void evalValidate(JSONObject validator, JSONObject variables) {
        try {
            String value = validator.getString("expected");
            if (!StringUtils.isEmpty(value)) {
                Object result = value;
                if (TypeUtil.isFunc(value)) {
                    String func = VarsHelper.bindMultiVariable(value, variables);
                    result = FunctionHelper.execute(func, variables);
                } else if (TypeUtil.isMultiVars(value)) {
                    result = VarsHelper.bindMultiVariable(value, variables);
                } else if (TypeUtil.isVars(value)) {
                    result = value.replace(value, variables.getString(value.substring(2, value.length() - 1)));
                }
                validator.put("expected", result);
            }
        } catch (Exception e) {
            throw new VarsException(e.getMessage(), e);
        }
    }
}
