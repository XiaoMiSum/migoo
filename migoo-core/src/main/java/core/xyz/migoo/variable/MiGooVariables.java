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

import static core.xyz.migoo.variable.VariableUtils.*;

/**
 * @author xiaomi
 */
public class MiGooVariables {

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

    public void convertVariable() {
        this.convertVariables(getProperty());
    }

    public void mergeVariable(MiGooVariables other) {
        if (other != null) {
            var copy = new MiGooVariables(getProperty());
            this.getProperty().clear();
            this.putAll(other);
            this.putAll(copy);
        }
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
            var entries = dataMapping.entrySet();
            for (var entry : entries) {
                var v = entry.getValue();
                switch (v) {
                    case String s -> dataMapping.put(entry.getKey(), extractVariables(s));
                    case JSONObject en -> convertVariables(en);
                    case JSONArray en -> convertVariables(en);
                    case MiGooVariables en -> convertVariables(en.getProperty());
                    case null, default -> dataMapping.put(entry.getKey(), v);
                }
            }
        }
    }

    private void convertVariables(JSONArray dataMapping) {
        for (int i = 0; i < dataMapping.size(); i++) {
            var item = dataMapping.get(i);
            if (item instanceof String string) {
                item = extractVariables(string);
            } else if (item instanceof JSONObject entry) {
                convertVariables(entry);
            } else if (item instanceof JSONArray objects) {
                convertVariables(objects);
            }
            dataMapping.set(i, item);
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
        var matcher = VARS_PATTERN.matcher(value);
        int x = 0;
        while (matcher.find(x)) {
            x = matcher.end();
            var temp = matcher.group();
            var key = matcher.group(1);
            var v = propMap.get(key);
            if (Objects.isNull(v)) {
                continue;
            }
            if (v instanceof String && (JSON.isValidObject((String) v) || JSON.isValidArray((String) v))) {
                // 字符串可以解析为 json 不允许出现在多变量组合的情况
                return JSON.parse((String) v);
            } else if (v instanceof String) {
                var result = evalVariable((String) v);
                if (result instanceof String s) {
                    // 变量计算结果为字符串时进行变量替换，同时继续判断字符串中是否还存在变量引用
                    value = value.replace(temp, s);
                } else {
                    // 非字符串计算结果，直接返回
                    return result;
                }
            } else if (v instanceof Number || v instanceof Boolean) {
                value = value.replace(temp, v.toString());
            } else {
                // 在设计上 不允许变量值为 非字符串、非数字、非布尔类型 出现在多变量组合的情况
                return v;
            }
        }
        return value;
    }

    private Object evalVariable(String v) {
        var func = FUNC_PATTERN.matcher(v);
        while (func.find()) {
            var result = FunctionService.execute(func.group(1), func.group(2), this);
            if (result instanceof String && (JSON.isValidObject((String) result) || JSON.isValidArray((String) result))) {
                // 字符串可以解析为 json 不允许出现在多变量组合的情况
                return JSON.parse((String) result);
            } else if (result instanceof String || result instanceof Number || result instanceof Boolean) {
                // 字符串、数字、布尔类型对象，转换为字符串替换（可能存在多个函数，不能return）
                v = v.replace(func.group(), result.toString());
            } else {
                // 其他未知类型，直接返回原始对象
                return result;
            }
        }
        return v;
    }

    @Override
    public String toString() {
        return propMap.toString();
    }
}