/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package core.xyz.migoo.variables;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.function.FunctionService;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.MiGooProperty;
import core.xyz.migoo.testelement.TestElement;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import static core.xyz.migoo.variables.VariableUtils.*;

public class MiGooVariables  implements VariableStateListener, Cloneable {

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

    public MiGooProperty getProperty(){
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
        try {
            this.convertVariables(getProperty());
        } catch (Exception e) {
            throw new RuntimeException("convert variable error", e);
        }
    }

    public void mergeVariable(MiGooVariables other) {
        MiGooVariables clone = this.clone();
        this.getProperty().clear();
        this.putAll(other);
        this.putAll(clone);
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

    public JSONObject getRequestBody(){
        return this.getProperty().getJSONObject("migoo.protocol.http.request.body");
    }

    public JSONObject getRequestData(){
        return this.getProperty().getJSONObject("migoo.protocol.http.request.data");
    }

    public JSONObject getRequestQuery(){
        return this.getProperty().getJSONObject("migoo.protocol.http.request.query");
    }

    public void convertVariables(JSONObject dataMapping) throws Exception {
        if (dataMapping != null) {
            Set<Map.Entry<String, Object>> entries = dataMapping.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                Object v = entry.getValue();
                if (v instanceof String) {
                    dataMapping.put(entry.getKey(), extractVariables((String) v));
                } else if (v instanceof JSONObject) {
                    convertVariables((JSONObject) v);
                    dataMapping.put(entry.getKey(), v);
                } else if (v instanceof JSONArray) {
                    convertVariables((JSONArray) v);
                    dataMapping.put(entry.getKey(), v);
                }
            }
        }
    }

    private void convertVariables(JSONArray temp) throws Exception {
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

    public Object extractVariables(String value) throws Exception {
        if (isFunc(value)) {
            return evalVariable(value);
        } else if (isVars(value)) {
            return extractVariable(value);
        }
        return value;
    }

    private Object extractVariable(String value) throws Exception {
        Matcher matcher = VARS_PATTERN.matcher(value);
        while (matcher.find()) {
            String temp = matcher.group();
            String key = temp.substring(2, temp.length() - 1);
            Object v = propMap.get(key);
            if (v == null) {
                throw new Exception("The variable value cannot be null: " + key);
            }
            // 变量值是个对象，直接返回
            if (v instanceof List || v instanceof Map) {
                return v;
            }
            if (v instanceof String) {
                v = evalVariable((String) v);
            }
            value = value.replace(temp, v.toString());
        }
        return value;
    }

    private String evalVariable(String v) throws Exception {
        Matcher func = FUNC_PATTERN.matcher( v);
        while (func.find()) {
            String result = FunctionService.execute(func.group(1), func.group(2), this).toString();
            v = v.replace(func.group(), result);
        }
        return v;
    }
}