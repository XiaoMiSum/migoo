/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.variable;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.function.FunctionService;
import core.xyz.migoo.testelement.MiGooProperty;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;

import static core.xyz.migoo.variable.VariableUtils.*;

/**
 * @author xiaomi
 */
public class MiGooVariables implements VariableStateListener, Cloneable {

    private final MiGooProperty propMap = new MiGooProperty();

    public MiGooVariables() {

    }

    public MiGooVariables(Map<? extends String, ?> variables) {
        this.putAll(variables);
    }

    public void putAll(MiGooVariables variables) {
        if (variables != null) {
            propMap.putAll(variables.getProperty());
        }
    }

    public void putAll(Map<? extends String, ?> variables) {
        if (variables != null) {
            propMap.putAll(variables);
        }
    }

    public void put(String name, Object value) {
        propMap.put(name, value);
    }

    public MiGooProperty getProperty() {
        return this.propMap;
    }

    public Object get(String key) {
        return propMap.get(key);
    }

    public Object remove(String key) {
        return propMap.remove(key);
    }

    @Override
    public void convertVariable() {
        this.convertVariables(getProperty());
    }

    public void mergeVariable(MiGooVariables other) {
        if (other != null) {
            MiGooVariables clone = this.clone();
            this.getProperty().clear();
            this.putAll(other);
            this.putAll(clone);
        }
    }

    @Override
    public MiGooVariables clone() {
        Map<? extends String, ?> variables;
        try {
            variables = ((MiGooVariables) super.clone()).getProperty();
        } catch (CloneNotSupportedException e) {
            variables = this.getProperty();
        }
        return new MiGooVariables(variables);
    }

    public JSONObject getRequestBody() {
        return this.getProperty().getJSONObject("migoo_protocol_http_request_body");
    }

    public JSONObject getRequestData() {
        return this.getProperty().getJSONObject("migoo_protocol_http_request_data");
    }

    public JSONObject getRequestQuery() {
        return this.getProperty().getJSONObject("migoo_protocol_http_request_query");
    }

    public void convertVariables(JSONObject dataMapping) {
        if (dataMapping != null) {
            Set<Map.Entry<String, Object>> entries = dataMapping.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object v = entry.getValue();
                if (v instanceof String) {
                    dataMapping.put(entry.getKey(), extractVariables((String) v));
                } else if (v instanceof JSONObject) {
                    convertVariables((JSONObject) v);
                } else if (v instanceof JSONArray) {
                    convertVariables((JSONArray) v);
                } else if (v instanceof MiGooVariables) {
                    convertVariables(((MiGooVariables) v).getProperty());
                }
            }
        }
    }

    private void convertVariables(JSONArray temp) {
        for (int i = 0; i < temp.size(); i++) {
            Object item = temp.get(i);
            if (item instanceof String) {
                item = extractVariables((String) item);
            } else if (item instanceof JSONObject) {
                convertVariables((JSONObject) item);
            } else if (item instanceof JSONArray) {
                convertVariables((JSONArray) item);
            }
            temp.remove(i);
            temp.add(i, item);
        }
    }

    public Object extractVariables(String value) {
        value = value.trim();
        if (isFunc(value)) {
            return evalVariable(value);
        } else if (isVars(value)) {
            return extractVariable(value);
        }
        return value;
    }

    private Object extractVariable(String value) {
        Matcher matcher = VARS_PATTERN.matcher(value);
        int x = 0;
        while (matcher.find(x)) {
            x = matcher.end();
            String temp = matcher.group();
            String key = matcher.group(1);
            Object v = propMap.get(key);
            if (Objects.isNull(v)) {
                continue;
            }
            if (v instanceof String && (JSON.isValidObject((String) v) || JSON.isValidArray((String) v))) {
                // 字符串可以解析为 json 不允许出现在多变量组合的情况
                return JSON.parse((String) v);
            } else if (v instanceof String) {
                value = value.replace(temp, evalVariable((String) v));
            } else if (v instanceof Number || v instanceof Boolean) {
                value = value.replace(temp, v.toString());
            } else {
                // 在设计上 不允许变量值为 非字符串、非数字、非布尔类型 出现在多变量组合的情况
                return v;
            }
        }
        return value;
    }

    private String evalVariable(String v) {
        Matcher func = FUNC_PATTERN.matcher(v);
        while (func.find()) {
            String result = FunctionService.execute(func.group(1), func.group(2), this).toString();
            v = v.replace(func.group(), result);
        }
        return v;
    }

    @Override
    public String toString() {
        return propMap.toString();
    }
}