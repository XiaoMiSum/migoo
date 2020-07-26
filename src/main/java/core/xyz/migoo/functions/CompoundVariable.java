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
import xyz.migoo.utils.TypeUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2019/11/18 15:47
 */
public class CompoundVariable extends HashMap<String, Object> {
    private static final long serialVersionUID = 362498820763181265L;

    static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");
    static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{(\\w+)}");
    private static final Pattern REGEX_INTEGER = Pattern.compile("^[-\\+]?[0-9]+$");
    private static final Pattern REGEX_FLOAT = Pattern.compile("^[-\\+]?[0-9]+\\.[0-9]+$");

    public Object put(String parameter, JSONObject variables) {
        String[] array = parameter.split("=");
        return this.put(array[0], this.getParameterValue(array[1], variables));
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(key, value);
    }

    private Object getParameterValue(String parameter, JSONObject variables){
        if (REGEX_INTEGER.matcher(parameter).find() || REGEX_FLOAT.matcher(parameter).find()){
            return  new BigDecimal(parameter);
        } else if (Boolean.TRUE.toString().equalsIgnoreCase(parameter) ||
                Boolean.FALSE.toString().equalsIgnoreCase(parameter)){
            return  Boolean.valueOf(parameter);
        } else if (PARAM_PATTERN.matcher(parameter).find()){
            if (variables == null || variables.isEmpty()){
                throw new RuntimeException("variables is null or empty");
            }
            Object object = variables.get(parameter.substring(2, parameter.length() -1));
            if (FUNC_PATTERN.matcher(String.valueOf(object)).find()
                    || PARAM_PATTERN.matcher(String.valueOf(object)).find()){
                throw new RuntimeException(String.format("%s need eval!", parameter));
            }
            return object;
        } else {
            return parameter;
        }
    }

    public String getString(String key){
        return super.get(key) == null ? "" : super.get(key).toString();
    }

    public Long getLong(String key){
        return super.get(key) == null ? null : Long.valueOf(getString(key));
    }

    public Integer getInteger(String key){
        return super.get(key) == null ? null : Integer.valueOf(getString(key));
    }

    public Double getDouble(String key){
        return super.get(key) == null ? null : Double.valueOf(getString(key));
    }

    public Float getFloat(String key){
        return super.get(key) == null ? null : Float.valueOf(getString(key));
    }

    public BigDecimal getBigDecimal(String key){
        return super.get(key) == null ? null : new BigDecimal(getString(key));
    }

    public Boolean getBoolean(String key){
        return TypeUtil.booleanOf(get(key));
    }

    public JSONObject getJSONObject(String key){
        return (JSONObject)get(key);
    }

    public JSONArray getJSONArray(String key){
        return (JSONArray)get(key);
    }
}
