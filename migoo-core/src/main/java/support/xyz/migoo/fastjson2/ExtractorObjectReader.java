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
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import component.xyz.migoo.extractor.JSONExtractor;
import core.xyz.migoo.ApplicationConfig;
import core.xyz.migoo.extractor.Extractor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import static core.xyz.migoo.configureelement.ConfigureElementConstantsInterface.REF_NAME;
import static core.xyz.migoo.configureelement.ConfigureElementConstantsInterface.VARIABLE_NAME;
import static core.xyz.migoo.testelement.TestElementConstantsInterface.TEST_CLASS;

/**
 * @author xiaomi
 * Created at 2025/7/19 14:54
 */
public class ExtractorObjectReader implements ObjectReader<Extractor> {

    @Override
    public Extractor readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        var testElementMap = jsonReader.readObject();
        var testClass = testElementMap.get(TEST_CLASS);
        if (Objects.isNull(testClass) || StringUtils.isBlank(testClass.toString())) {
            testElementMap.put(TEST_CLASS, JSONExtractor.class.getSimpleName());
        }
        var variableName = testElementMap.remove(VARIABLE_NAME);
        var refName = Objects.isNull(variableName) ? testElementMap.remove(REF_NAME) : variableName;
        testElementMap.put(REF_NAME, refName);
        var pair = checkTestElement(testElementMap);
        return JSON.parseObject(JSON.toJSONString(testElementMap), pair.getLeft());
    }

    private Pair<Class<? extends Extractor>, String> checkTestElement(Map<String, Object> testElementMap) {
        var keyMap = ApplicationConfig.getExtractorKeyMap();
        var key = testElementMap.get(TEST_CLASS).toString();
        var clazz = keyMap.get(key);
        if (Objects.nonNull(clazz)) {
            return Pair.of(clazz, key);
        }
        throw new JSONException("没有匹配的验证器, JSON String: " + JSON.toJSONString(testElementMap));
    }

}
