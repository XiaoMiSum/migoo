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

package core.xyz.migoo.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import core.xyz.migoo.variables.MiGooVariables;
import core.xyz.migoo.variables.VariableUtils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @author xiaomi
 * @date 2019/11/18 15:47
 */
public class CompoundParameter extends HashMap<String, Object> {
    private static final long serialVersionUID = 362498820763181265L;

    private final MiGooVariables variables;

    public CompoundParameter(MiGooVariables variables) {
        this.variables = variables;
    }

    public Object put(String parameter) throws Exception {
        String[] array = parameter.split("=");
        return this.put(array[0], this.getParameterValue(array[1]));
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    private Object getParameterValue(String parameter) throws Exception {
        if (VariableUtils.isVars(parameter) || VariableUtils.isFunc(parameter)) {
            return variables.extractVariables(parameter);
        }
        return parameter;
    }

    public boolean isNullKey(String key) {
        return super.get(key) == null;
    }

    public String getString(String key) {
        return isNullKey(key) ? "" : super.get(key).toString();
    }

    public BigDecimal getBigDecimal(String key) {
        return isNullKey(key) ? null : (BigDecimal) get(key);
    }

    public JSONObject getJSONObject(String key) {
        return JSONObject.parseObject(getString(key));
    }

    public JSONArray getJSONArray(String key) {
        return JSONArray.parseArray(getString(key));
    }

    public boolean getBooleanValue(String key) {
        return !isNullKey(key) && TypeUtils.castToBoolean(get(key) != null && TypeUtils.castToBoolean(get(key)));
    }

    public MiGooVariables getCurrentVars() {
        return this.variables;
    }
}