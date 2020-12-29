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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.functions.FunctionHelper;
import core.xyz.migoo.utils.StringUtil;
import core.xyz.migoo.utils.TypeUtil;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import static core.xyz.migoo.utils.TypeUtil.VARS_PATTERN;


/**
 * @author xiaomi
 * @date 2020/8/19 20:35
 **/
public class VarsHelper {

    /**
     * bind variable to data mapping
     * may be variable that might not have been initialized, the first convert variables
     *
     * @param dataMapping      use of variables in data mapping
     * @param variablesMapping variables mapping
     */
    public static void convertVariables(JSONObject dataMapping, JSONObject variablesMapping) throws FunctionException {
        if (dataMapping != null && variablesMapping != null) {
            Set<Map.Entry<String, Object>> entries = dataMapping.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object v = entry.getValue();
                if (v instanceof String) {
                    dataMapping.put(entry.getKey(), extractVariables((String) v, variablesMapping));
                } else if (v instanceof JSONObject) {
                    convertVariables((JSONObject) v, variablesMapping);
                    dataMapping.put(entry.getKey(), v);
                } else if (v instanceof JSONArray) {
                    convertVariables((JSONArray) v, variablesMapping);
                    dataMapping.put(entry.getKey(), v);
                }
            }
        }
    }

    /**
     * convert variables
     * may be variable that might not have been initialized
     *
     * @param variables variables mapping
     */
    public static void convertVariables(JSONObject variables) throws FunctionException {
        if (variables != null) {
            convertVariables(variables, variables);
        }
    }

    /**
     * convert variables
     * may be variable that might not have been initialized
     *
     * @param variables variables mapping
     */
    public static String convertVariables(String mapping, JSONObject variables) throws FunctionException {
        if (variables != null) {
            mapping = extractVariable(mapping, variables);
        }
        return mapping;
    }

    private static void convertVariables(JSONArray temp, JSONObject variables) throws FunctionException {
        for (int i = 0; i < temp.size(); i++) {
            String item = temp.getString(i);
            temp.remove(i);
            temp.add(i, extractVariables(item, variables));
        }
    }

    private static Object extractVariables(String value, JSONObject variables) throws FunctionException {
        if (TypeUtil.isFunc(value)) {
            return FunctionHelper.execute(value, variables);
        } else if (TypeUtil.isVars(value)) {
            return extractVariable(value, variables);
        }
        return value;
    }

    private static String extractVariable(String value, JSONObject variables) throws FunctionException {
        Matcher matcher = VARS_PATTERN.matcher(value);
        while (matcher.find()) {
            String temp = matcher.group();
            String key = temp.substring(2, temp.length() - 1);
            Object v = variables.get(key);
            if (v == null || StringUtil.isEmpty(v.toString())) {
                throw new VarsException("The variable value cannot be null: " + key);
            }
            if (v instanceof String && TypeUtil.isFunc((String) v)) {
                v=  FunctionHelper.execute((String) v, variables);
            }
            value = value.replace(temp, v.toString());
        }
        return value;
    }
}
