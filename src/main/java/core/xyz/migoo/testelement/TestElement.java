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

import java.util.Map;
import java.util.Vector;

public interface TestElement {

    MiGooVariables getVariables();

    void setVariables(MiGooVariables variables);

    void addChildTestElement(TestElement child);

    Vector<TestElement> getChildTestElements();

    void setProperties(Map<String, Object> props);

    void setProperty(String key, Object value);

    MiGooProperty getProperty();

    Object removeProperty(String key);

    boolean getPropertyAsBoolean(String key);

    long getPropertyAsLong(String key);

    int getPropertyAsInt(String key);

    float getPropertyAsFloat(String key);

    double getPropertyAsDouble(String key);

    String getPropertyAsString(String key);

    JSONObject getPropertyAsJSONObject(String key);

    JSONArray getPropertyAsJSONArray(String key);

    MiGooProperty getPropertyAsMGooProperty(String key);

    Object get(String key);

    Object get(String key, Object defaultValue);
}