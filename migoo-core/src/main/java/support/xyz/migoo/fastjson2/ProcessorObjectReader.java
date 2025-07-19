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
import core.xyz.migoo.processor.Processor;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import static core.xyz.migoo.testelement.TestElementConstantsInterface.*;

/**
 * @author xiaomi
 * Created at 2025/7/19 14:14
 */
@SuppressWarnings({"rawtypes"})
public abstract class ProcessorObjectReader implements ObjectReader<Processor> {
    @Override
    public Processor readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        var testElementMap = jsonReader.readObject();
        var pair = checkTestElement(testElementMap);
        standardizeConfig(testElementMap);
        return JSON.parseObject(JSON.toJSONString(testElementMap), pair.getLeft());
    }

    private Pair<Class<? extends Processor>, String> checkTestElement(Map<String, Object> testElementMap) {
        var keyMap = getClass().equals(PreprocessorObjectReader.class) ?
                ApplicationConfig.getPreprocessorKeyMap() : ApplicationConfig.getPostprocessorKeyMap();
        var key = testElementMap.get(TEST_CLASS).toString();
        var clazz = keyMap.get(key);
        if (Objects.nonNull(clazz)) {
            return Pair.of(clazz, key);
        }
        throw new JSONException("没有匹配的处理器, JSON String: " + JSON.toJSONString(testElementMap));
    }

    private void standardizeConfig(Map<String, Object> testElementMap) {
        // 删除 TestElement 的 config 或创建一个新的 config
        var _config = testElementMap.containsKey(CONFIG) ? new JSONObject((Map) testElementMap.remove(CONFIG)) : new JSONObject();
        // 删除 testElementMap 或 config 中的变量
        var variables = testElementMap.containsKey(VARIABLES) ? testElementMap.remove(VARIABLES) : _config.remove(VARIABLES);
        // 防止变量为 null
        variables = Objects.isNull(variables) ? new JSONObject() : new JSONObject((Map) variables);
        // 处理取样器配置
        // 如果 config 中存在 request 则直接取这个 request, 反之则 config中剩余的属性均为取样器的请求配置
        var request = _config.containsKey(REQUEST) ? _config.getJSONObject(REQUEST) : new JSONObject(_config);
        testElementMap.put(CONFIG, JSONObject.of(VARIABLES, variables, REQUEST, request));
    }
}
