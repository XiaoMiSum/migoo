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

package core.xyz.migoo.testelement;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.variable.MiGooVariables;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author xiaomi
 */
public abstract class AbstractTestElement implements TestElement, Serializable {

    public static final String VARIABLES = "variables";
    public static final String CONFIG_ELEMENTS = "configelements";
    public static final String PREPROCESSORS = "preprocessors";
    public static final String POSTPROCESSORS = "postprocessors";
    public static final String TEST_CLASS = "testclass";
    public static final String EXTRACTORS = "extractors";
    public static final String VALIDATORS = "validators";
    public static final String CHILDREN = "children";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String SLEEP = "sleep";
    public static final String CONFIG = "config";

    private final MiGooProperty propMap = new MiGooProperty();

    @Override
    public MiGooVariables getVariables() {
        return (MiGooVariables) propMap.get(VARIABLES);
    }

    @Override
    public void setVariables(MiGooVariables variables) {
        propMap.put(VARIABLES, variables);
    }

    @Override
    public void convertVariable() {
        var variables = getVariables();
        if (variables != null) {
            variables.convertVariables(getProperty());
        }
    }

    @Override
    public void setProperties(Map<String, Object> props) {
        if (props != null && !props.isEmpty()) {
            props.forEach((key, value) -> propMap.putIfAbsent(key.toLowerCase(), value));
        }
    }

    @Override
    public void setProperty(String key, Object value) {
        key = key.toLowerCase();
        propMap.putIfAbsent(key, value);
    }

    @Override
    public MiGooProperty getProperty() {
        return propMap;
    }

    @Override
    public Object removeProperty(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.remove(lowerKey);
    }

    @Override
    public byte[] getPropertyAsByteArray(String key) {
        var lowerKey = key.toLowerCase();
        var val = propMap.get(lowerKey);
        if (val instanceof Map<?, ?> || val instanceof List<?>) {
            return JSON.toJSONBytes(val);
        }
        if (!(val instanceof String) && !(val instanceof byte[])) {
            return val.toString().getBytes(StandardCharsets.UTF_8);
        }
        return propMap.getBytes(lowerKey);
    }

    @Override
    public boolean getPropertyAsBoolean(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getBooleanValue(lowerKey);
    }

    @Override
    public long getPropertyAsLong(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getLongValue(lowerKey);
    }

    @Override
    public int getPropertyAsInt(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getIntValue(lowerKey);
    }

    @Override
    public float getPropertyAsFloat(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getFloatValue(lowerKey);
    }

    @Override
    public double getPropertyAsDouble(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getDoubleValue(lowerKey);
    }

    @Override
    public String getPropertyAsString(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getString(lowerKey);
    }

    @Override
    public JSONObject getPropertyAsJSONObject(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getJSONObject(lowerKey);
    }

    @Override
    public JSONArray getPropertyAsJSONArray(String key) {
        var lowerKey = key.toLowerCase();
        return propMap.getJSONArray(lowerKey);
    }

    @Override
    public Object get(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.get(lowerKey);
    }

    @Override
    public Object get(String key, Object defaultValue) {
        var lowerKey = key.toLowerCase();
        var obj = propMap.get(lowerKey);
        return Optional.ofNullable(obj).orElse(defaultValue);
    }

    @Override
    public MiGooProperty getPropertyAsMiGooProperty(String key) {
        Object object = get(key);
        return switch (object) {
            case MiGooProperty property -> property;
            case Map<?, ?> property -> new MiGooProperty(property);
            case null, default -> null;
        };
    }
}