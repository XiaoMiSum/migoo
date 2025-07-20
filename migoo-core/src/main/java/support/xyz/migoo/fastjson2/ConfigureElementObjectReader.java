/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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
 *
 */

package support.xyz.migoo.fastjson2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import core.xyz.migoo.ApplicationConfig;
import core.xyz.migoo.configureelement.ConfigureElement;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import static core.xyz.migoo.configureelement.ConfigureElementConstantsInterface.REF_NAME;
import static core.xyz.migoo.configureelement.ConfigureElementConstantsInterface.VARIABLE_NAME;
import static core.xyz.migoo.testelement.TestElementConstantsInterface.CONFIG;
import static core.xyz.migoo.testelement.TestElementConstantsInterface.TEST_CLASS;

/**
 * @author xiaomi
 * Created at 2025/7/19 14:14
 */
@SuppressWarnings({"rawtypes"})
public class ConfigureElementObjectReader implements ObjectReader<ConfigureElement> {
    @Override
    public ConfigureElement readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        var testElementMap = jsonReader.readObject();
        var pair = checkTestElement(testElementMap);
        standardizeConfig(testElementMap, pair.getRight());
        var rawData = JSON.toJSONString(testElementMap);
        return JSON.parseObject(rawData, pair.getLeft());
    }

    private Pair<Class<? extends ConfigureElement>, String> checkTestElement(Map<String, Object> testElementMap) {
        var keyMap = ApplicationConfig.getConfigureElementKeyMap();
        var key = testElementMap.get(TEST_CLASS).toString();
        var clazz = keyMap.get(key);
        if (Objects.nonNull(clazz)) {
            return Pair.of(clazz, key);
        }
        throw new JSONException("没有匹配的配置元件, JSON String: " + JSON.toJSONString(testElementMap));
    }

    private void standardizeConfig(Map<String, Object> testElementMap, String key) {
        testElementMap.remove(TEST_CLASS); // 删除测试类
        var variableName = testElementMap.remove(VARIABLE_NAME);
        var refName = Objects.isNull(variableName) ? testElementMap.remove(REF_NAME) : variableName;
        // 删除 TestElement 的 config 或创建一个新的 config
        var _config = testElementMap.containsKey(CONFIG) ? new JSONObject((Map) testElementMap.remove(CONFIG)) : new JSONObject();
        if (_config.isEmpty()) {
            // config 没有属性，则将 testElementMap 的所有属性添加到 config 中
            _config.putAll(testElementMap);
            testElementMap.clear();
        }
        // 测试集config中只存在变量
        testElementMap.putAll(JSONObject.of(TEST_CLASS, key, REF_NAME, refName, CONFIG, _config));
    }
}
