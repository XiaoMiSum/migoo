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

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.Map;

/**
 * @author xiaomi
 */
public interface TestElement {

    /**
     * 获取当前测试元素的变量
     *
     * @return 变量
     */
    MiGooVariables getVariables();

    /**
     * 设置当前测试元素的变量
     *
     * @param variables 变量
     */
    void setVariables(MiGooVariables variables);

    /**
     * 通过 map 设置测试元素的属性
     *
     * @param props 属性集
     */
    void setProperties(Map<String, Object> props);

    /**
     * 通过 key\value 设置测试元素的属性
     *
     * @param key   key
     * @param value value
     */
    void setProperty(String key, Object value);

    /**
     * 获取当前测试元素的属性集
     *
     * @return 当前测试元素的属性集
     */
    MiGooProperty getProperty();

    /**
     * 通过 key 删除一个属性
     *
     * @param key key
     * @return 被删除key的value
     */
    Object removeProperty(String key);

    /**
     * 通过key获取一个ByteArray类型的属性值
     *
     * @param key key
     * @return ByteArray类型的属性值
     */
    byte[] getPropertyAsByteArray(String key);

    /**
     * 通过key获取一个boolean类型的属性值
     *
     * @param key key
     * @return boolean类型的属性值
     */
    boolean getPropertyAsBoolean(String key);

    /**
     * 通过key获取一个long类型的属性值
     *
     * @param key key
     * @return long类型的属性值
     */
    long getPropertyAsLong(String key);

    /**
     * 通过key获取一个int类型的属性值
     *
     * @param key key
     * @return int类型的属性值
     */
    int getPropertyAsInt(String key);

    /**
     * 通过key获取一个float类型的属性值
     *
     * @param key key
     * @return float类型的属性值
     */
    float getPropertyAsFloat(String key);

    /**
     * 通过key获取一个double类型的属性值
     *
     * @param key key
     * @return double类型的属性值
     */
    double getPropertyAsDouble(String key);

    /**
     * 通过key获取一个string类型的属性值
     *
     * @param key key
     * @return string类型的属性值
     */
    String getPropertyAsString(String key);

    /**
     * 通过key获取一个fastjson.JSONObject类型的属性值
     *
     * @param key key
     * @return fastjson.JSONObject类型的属性值
     */
    JSONObject getPropertyAsJSONObject(String key);

    /**
     * 通过key获取一个fastjson.JSONArray类型的属性值
     *
     * @param key key
     * @return fastjson.JSONArray类型的属性值
     */
    JSONArray getPropertyAsJSONArray(String key);

    /**
     * 通过key获取一个MiGooProperty类型的属性值
     *
     * @param key key
     * @return MiGooProperty类型的属性值
     */
    MiGooProperty getPropertyAsMiGooProperty(String key);

    /**
     * 通过key获取一个Object类型的属性值
     *
     * @param key key
     * @return object类型的属性值
     */
    Object get(String key);

    /**
     * 通过key获取一个Object类型的属性值，当值为 null时，返回 defaultValue
     *
     * @param key          key
     * @param defaultValue 值为空时的默认值
     * @return object类型的属性值
     */
    Object get(String key, Object defaultValue);
}