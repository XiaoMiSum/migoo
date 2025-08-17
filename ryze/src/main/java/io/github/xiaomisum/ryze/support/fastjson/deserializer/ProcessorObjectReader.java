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

package io.github.xiaomisum.ryze.support.fastjson.deserializer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import io.github.xiaomisum.ryze.core.ApplicationConfig;
import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;
import io.github.xiaomisum.ryze.core.testelement.processor.Processor;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;


/**
 * @author xiaomi
 * Created at 2025/7/19 14:14
 */
public abstract class ProcessorObjectReader implements ObjectReader<Processor>, TestElementConstantsInterface {
    @Override
    public Processor readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        var testElementMap = jsonReader.readObject();
        var pair = checkTestElement(testElementMap, fieldName);
        standardizeConfig(testElementMap, pair.getRight());
        return JSON.parseObject(JSON.toJSONString(testElementMap), pair.getLeft());
    }

    private Pair<Class<? extends Processor>, String> checkTestElement(Map<String, Object> testElementMap, Object fieldName) {
        var keyMap = getClass().equals(PreprocessorObjectReader.class) ?
                ApplicationConfig.getPreprocessorKeyMap() : ApplicationConfig.getPostprocessorKeyMap();
        var key = testElementMap.get(TEST_CLASS).toString().toLowerCase();
        var clazz = keyMap.get(key);
        if (Objects.nonNull(clazz)) {
            return Pair.of(clazz, key);
        }
        throw new JSONException("没有匹配的 %s 处理器, JSON String: %s".formatted(fieldName, JSON.toJSONString(testElementMap)));
    }

    private void standardizeConfig(Map<String, Object> elementMap, String key) {
        if (elementMap.containsKey(CONFIG)) {
            return;
        }
        // 刪除標準配置項
        elementMap.remove(TEST_CLASS); // 删除测试类
        elementMap.remove(VARIABLES);
        var id = elementMap.remove(ID);
        var title = elementMap.remove(TITLE);
        var disabled = elementMap.remove(DISABLED);
        var interceptors = elementMap.remove(INTERCEPTORS);
        var metadata = elementMap.remove(METADATA);
        var extractors = elementMap.remove(EXTRACTORS);
        var config = new JSONObject(elementMap);
        // 清空當前MAP
        elementMap.clear();
        // 重新設置標準化處理器配置
        elementMap.putAll(JSONObject.of(TEST_CLASS, key, ID, id, TITLE, title, DISABLED, disabled, INTERCEPTORS, interceptors, CONFIG, config, METADATA, metadata, EXTRACTORS, extractors));
    }
}
