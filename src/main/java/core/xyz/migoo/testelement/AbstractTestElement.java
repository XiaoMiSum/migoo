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

package core.xyz.migoo.testelement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.variables.MiGooVariables;
import core.xyz.migoo.variables.VariableStateListener;

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;

public abstract class AbstractTestElement implements TestElement, VariableStateListener, Serializable {

    public static final String VARIABLES = "variables";
    public static final String TEST_ELEMENTS = "testelements";
    public static final String CONFIG_ELEMENTS = "configelements";
    public static final String PREPROCESSORS = "preprocessors";
    public static final String POSTPROCESSORS = "postprocessors";
    public static final String TEST_CLASS = "testclass";
    public static final String EXTRACTORS = "extractors";
    public static final String VALIDATORS = "validators";
    public static final String CHILDS = "childs";
    public static final String TITLE = "title";
    public static final String REPORT_ELEMENT = "reportelement";

    private MiGooVariables variables;

    private int variableState = 0;

    private final MiGooProperty propMap = new MiGooProperty();

    private final Vector<TestElement> elements = new Vector<>(20);

    @Override
    public MiGooVariables getVariables() {
        return variables;
    }

    @Override
    public void setVariables(MiGooVariables variables) {
        this.variables = variables;
    }

    @Override
    public void convertVariable() {
        try {
            if (variableState == 0 && variables != null) {
                variableState++;
                variables.convertVariables(getProperty());
            }
        } catch (Exception e) {
            throw new RuntimeException("convert variable error", e);
        }
    }

    @Override
    public void addChildTestElement(TestElement child) {
        this.elements.add(child);
    }

    @Override
    public void setProperties(Map<String, Object> props) {
        if (props != null && props.size() > 0) {
            props.forEach((key, value) -> propMap.putAndIgnoreExist(key.toLowerCase(), value));
        }
    }

    @Override
    public Vector<TestElement> getChildTestElements() {
        return this.elements;
    }

    @Override
    public void setProperty(String key, Object value) {
        key = key.toLowerCase();
        propMap.putAndIgnoreExist(key, value);
    }

    @Override
    public MiGooProperty getProperty() {
        return propMap;
    }

    @Override
    public void removeProperty(String key) {
        String lowerKey = key.toLowerCase();
        propMap.remove(lowerKey);
    }

    @Override
    public boolean getPropertyAsBoolean(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getBooleanValue(lowerKey);
    }

    @Override
    public long getPropertyAsLong(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getLongValue(lowerKey);
    }

    @Override
    public int getPropertyAsInt(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getIntValue(lowerKey);
    }

    @Override
    public float getPropertyAsFloat(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getFloatValue(lowerKey);
    }

    @Override
    public double getPropertyAsDouble(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getDoubleValue(lowerKey);
    }

    @Override
    public String getPropertyAsString(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getString(lowerKey);
    }

    @Override
    public JSONObject getPropertyAsJSONObject(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getJSONObject(lowerKey);
    }

    @Override
    public JSONArray getPropertyAsJSONArray(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.getJSONArray(lowerKey);
    }

    @Override
    public Object get(String key) {
        String lowerKey = key.toLowerCase();
        return propMap.get(lowerKey);
    }

    @Override
    public Object get(String key, Object defaultValue) {
        String lowerKey = key.toLowerCase();
        Object obj = propMap.get(lowerKey);
        return obj == null ? defaultValue : obj;
    }

    @Override
    public MiGooProperty getPropertyAsMGooProperty(String key) {
        Object object = get(key);
        if (object instanceof MiGooProperty) {
            return (MiGooProperty) object;
        }
        if (object instanceof Map) {
            return new MiGooProperty((Map<String, Object>) object);
        }
        return null;
    }
}