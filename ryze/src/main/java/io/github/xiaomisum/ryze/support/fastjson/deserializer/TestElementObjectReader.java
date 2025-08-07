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
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import io.github.xiaomisum.ryze.core.ApplicationConfig;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.support.fastjson.interceptor.JSONInterceptor;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface.CONFIG;
import static io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface.TEST_CLASS;

/**
 * @author xiaomi
 * Created at 2025/7/19 12:37
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TestElementObjectReader implements ObjectReader<TestElement> {

    @Override
    public TestElement readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        var testElementMap = jsonReader.readObject();
        var elementMap = new HashMap<String, Object>();
        // 转换所有key 为小写
        for (Map.Entry<String, Object> entry : testElementMap.entrySet()) {
            elementMap.put(entry.getKey().toLowerCase(), entry.getValue());
        }
        var pair = checkTestElement(elementMap);
        JSONInterceptor interceptor = ApplicationConfig.getJsonInterceptorKeyMap().get(pair.getLeft());
        if (interceptor != null) {
            var config = interceptor.deserializeConfigureItem(elementMap.get(CONFIG));
            if (config != null) {
                elementMap.put(CONFIG, config);
            }
        }
        return JSON.parseObject(JSON.toJSONString(elementMap), pair.getLeft());
    }

    private Pair<Class<? extends TestElement>, String> checkTestElement(Map<String, Object> elementMap) {
        var keyMap = ApplicationConfig.getTestElementKeyMap();
        var key = elementMap.get(TEST_CLASS).toString().toLowerCase();
        var clazz = keyMap.get(key);
        if (Objects.nonNull(clazz)) {
            return Pair.of(clazz, key);
        }
        throw new JSONException("没有匹配的测试集或取样器, JSON String: " + JSON.toJSONString(elementMap));
    }


}
