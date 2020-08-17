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

package core.xyz.migoo.functions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.utils.TypeUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2019/11/18 15:47
 */
public class CompoundVariable extends HashMap<String, Object> {

    public Object put(String parameter, JSONObject variables) throws FunctionException {
        String[] array = parameter.split("=");
        return super.put(array[0], this.getParameterValue(array[1], variables));
    }

    private Object getParameterValue(String parameter, JSONObject variables) throws FunctionException {
        if (TypeUtil.isNumber(parameter)) {
            return new BigDecimal(parameter);
        } else if (TypeUtil.isBoolean(parameter)) {
            return Boolean.valueOf(parameter);
        } else if (TypeUtil.isVars(parameter)) {
            if (variables == null || variables.isEmpty()) {
                throw new FunctionException("variables is null or empty");
            }
            Object object = variables.get(parameter.substring(2, parameter.length() - 1));
            if (TypeUtil.isVarsOrFunc(String.valueOf(object))) {
                throw new FunctionException(String.format("%s need eval!", parameter));
            }
            return object;
        } else {
            return parameter;
        }
    }

    public String getString(String key) {
        return super.get(key) == null ? "" : super.get(key).toString();
    }

    public Long getLong(String key) {
        return super.get(key) == null ? null : Long.valueOf(getString(key));
    }

    public Integer getInteger(String key) {
        return super.get(key) == null ? null : Integer.valueOf(getString(key));
    }

    public Double getDouble(String key) {
        return super.get(key) == null ? null : Double.valueOf(getString(key));
    }

    public Float getFloat(String key) {
        return super.get(key) == null ? null : Float.valueOf(getString(key));
    }

    public BigDecimal getBigDecimal(String key) {
        return super.get(key) == null ? null : new BigDecimal(getString(key));
    }

    public Boolean getBoolean(String key) {
        return TypeUtil.booleanOf(get(key));
    }

    public JSONObject getJSONObject(String key) {
        return (JSONObject) get(key);
    }

    public JSONArray getJSONArray(String key) {
        return (JSONArray) get(key);
    }
}
