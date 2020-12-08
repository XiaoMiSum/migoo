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
import core.xyz.migoo.vars.Vars;
import core.xyz.migoo.vars.VarsHelper;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author xiaomi
 * @date 2019/11/18 15:47
 */
public class CompoundVariable extends HashMap<String, Object> {
    private static final long serialVersionUID = 362498820763181265L;

    public Object put(String parameter, JSONObject variables) throws FunctionException {
        String[] array = parameter.split("=");
        return this.put(array[0], this.getParameterValue(array[1], variables));
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    private Object getParameterValue(String parameter, JSONObject variables) throws FunctionException {
        if (TypeUtil.isNumber(parameter)) {
            return new BigDecimal(parameter);
        } else if (TypeUtil.isBoolean(parameter)) {
            return Boolean.valueOf(parameter);
        } else {
            return VarsHelper.extractVariables(parameter, variables);
        }
    }

    public boolean isNullKey(String key) {
        return super.get(key) == null;
    }

    public String getString(String key) {
        return isNullKey(key) ? "" : super.get(key).toString();
    }

    public Long getLong(String key) {
        return isNullKey(key) ? null : getBigDecimal(key).longValue();
    }

    public Integer getInteger(String key) {
        return isNullKey(key) ? null : getBigDecimal(key).intValue();
    }

    public Double getDouble(String key) {
        return isNullKey(key) ? null : getBigDecimal(key).doubleValue();
    }

    public Float getFloat(String key) {
        return isNullKey(key) ? null : getBigDecimal(key).floatValue();
    }

    public BigDecimal getBigDecimal(String key) {
        return isNullKey(key) ? null : (BigDecimal) get(key);
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

    public Vars getCurrentVars() {
        return (Vars) get("migoo.vars");
    }
}
