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

package core.xyz.migoo.function;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.variable.MiGooVariables;
import core.xyz.migoo.variable.VariableUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019/11/18 15:47
 */
public class Args extends ArrayList<Object> {

    private final MiGooVariables variables;

    public Args(MiGooVariables variables) {
        this.variables = variables;
    }

    public static Args newArgs(String origin, MiGooVariables variables) {
        var args = new Args(variables);
        if (StringUtils.isNotBlank(origin)) {
            Collections.addAll(args, origin.split(","));
        }
        return args;
    }

    @Override
    public boolean add(Object parameter) {
        return super.add(parameter instanceof String s ? getParameterValue(s) : parameter);
    }

    private Object getParameterValue(String parameter) {
        if (VariableUtils.isVars(parameter) || VariableUtils.isFunc(parameter)) {
            return variables.extractVariables(parameter);
        }
        return parameter.trim();
    }

    public String getString(int index) {
        return size() <= index || super.get(index) == null ? "" : super.get(index).toString().trim();
    }

    public BigDecimal getNumber(int index) {
        return getString(index).isEmpty() ? null : new BigDecimal(getString(index));
    }

    public JSONObject getJSONObject(int index) {
        var value = get(index);
        return switch (value) {
            case null -> null;
            case String string -> StringUtils.isBlank(string) ? null : JSON.parseObject(string);
            case Map<?, ?> entry -> new JSONObject(entry);
            default -> JSON.parseObject(JSON.toJSONString(value));
        };
    }

    public JSONArray getJSONArray(int index) {
        var value = get(index);
        return switch (value) {
            case null -> null;
            case String string -> StringUtils.isBlank(string) ? null : JSON.parseArray(string);
            case List<?> objects -> new JSONArray(objects);
            default -> JSON.parseArray(JSON.toJSONString(value));
        };
    }

    public boolean getBooleanValue(int index) {
        String value = getString(index);
        return StringUtils.equalsAnyIgnoreCase(value, "1", "t", "true");
    }

    public MiGooVariables getCurrentVars() {
        return this.variables;
    }
}