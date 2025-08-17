package io.github.xiaomisum.ryze.support.fastjson.deserializer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import io.github.xiaomisum.ryze.core.ApplicationConfig;
import io.github.xiaomisum.ryze.core.interceptor.RyzeInterceptor;
import io.github.xiaomisum.ryze.support.Collections;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface.TEST_CLASS;

@SuppressWarnings({"unchecked", "rawtypes"})
public class InterceptorObjectReader implements ObjectReader<RyzeInterceptor> {

    @Override
    public RyzeInterceptor readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        Object anyData = jsonReader.readAny();
        Map<String, Object> testElementMap = Collections.newHashMap();
        standardizeInterceptors(testElementMap, anyData);
        var pair = checkInterceptor(testElementMap);
        return JSON.parseObject(JSON.toJSONString(testElementMap), pair.getLeft());
    }

    private void standardizeInterceptors(Map<String, Object> elementMap, Object anyData) {
        if (anyData instanceof String) {
            elementMap.put(TEST_CLASS, anyData);
            return;
        }
        if (Objects.requireNonNull(anyData) instanceof Map map) {
            if (map.containsKey(TEST_CLASS)) {
                elementMap.putAll(map);
                return;
            }
            var entries = (Set<Map.Entry>) map.entrySet();
            entries.forEach(entry -> {
                elementMap.put(TEST_CLASS, entry.getKey());
                elementMap.putAll((Map) entry.getValue());
            });
        }
    }

    private Pair<Class<? extends RyzeInterceptor>, String> checkInterceptor(Map<String, Object> elementMap) {
        var keyMap = ApplicationConfig.getTestInterceptorKeyMap();
        var key = elementMap.get(TEST_CLASS).toString().toLowerCase();
        var clazz = keyMap.get(key);
        if (Objects.nonNull(clazz)) {
            return Pair.of(clazz, key);
        }
        throw new JSONException("没有匹配的Interceptor, JSON String: " + JSON.toJSONString(elementMap));
    }

}
