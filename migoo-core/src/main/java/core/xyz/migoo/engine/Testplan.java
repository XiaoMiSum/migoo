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

package core.xyz.migoo.engine;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.variable.MiGooVariables;
import util.xyz.migoo.loader.Loader;

import java.util.*;

import static core.xyz.migoo.testelement.AbstractTestElement.*;
import static core.xyz.migoo.variable.VariableUtils.FILE_PATTERN;

/**
 * @author xiaomi
 */
public class Testplan extends JSONObject {

    private final boolean sampler;

    public Testplan(JSONObject testcase) {
        // 同时包含 children 和 title 这两个key 即 认为是 migoo 的测试集合
        var json = prepare(testcase);
        var isMiGooSuite = json.containsKey(CHILDREN) && json.containsKey(TITLE);
        var clazz = TestElementService.getServiceClass(json.getString(TEST_CLASS));
        sampler = Objects.nonNull(clazz) && Sampler.class.isAssignableFrom(clazz);
        // migoo 测试集合 或者 注册的 test class 都是 migoo 测试组件
        initialize(json, isMiGooSuite || Objects.nonNull(clazz));
    }

    private void initialize(JSONObject json, boolean isMiGoo) {
        // 1、先删除 MiGoo组件中的变量，减少一次遍历
        var variables = isMiGoo ? json.remove(VARIABLES) : null;
        json.forEach((key, value) -> {
            // 将 migoo 测试组件 的key 转换为小写
            var newKey = isMiGoo ? key.toLowerCase(Locale.ROOT) : key;
            switch (value) {
                case JSONObject object -> put(newKey, parse(object));
                case JSONArray objects -> {
                    for (int i = 0; i < objects.size(); i++) {
                        var item = objects.get(i);
                        // 这里主要是为了处理 配置的子组件，组件配置一定是Map
                        objects.set(i, item instanceof Map ? parse(objects.getJSONObject(i)) : item);
                    }
                    put(newKey, objects);
                }
                case null, default -> put(newKey, value);
            }
        });
        if (isMiGoo) {
            // 3、重新添加变量到MiGoo组件
            put(VARIABLES, variables instanceof MiGooVariables ? variables :
                    new MiGooVariables(Optional.ofNullable((JSONObject) variables).orElse(new JSONObject())));
        }
    }

    private JSONObject parse(JSONObject json) {
        var isMiGooSuite = json.containsKey(CHILDREN) && json.containsKey(TITLE);
        var clazz = TestElementService.getServiceClass(json.getString(TEST_CLASS));
        return isMiGooSuite || Objects.nonNull(clazz) ? new Testplan(json) : json;
    }

    private JSONObject prepare(Map<?, ?> object) {
        var json = new JSONObject(object.size());
        object.keySet().forEach(key -> {
            var keyString = (String) key;
            var value = object.get(key);
            switch (value) {
                case Map<?, ?> map -> json.put(keyString, prepare(map));
                case List<?> objects -> json.put(keyString, prepare(objects));
                case String s -> json.put(keyString, prepare(s));
                case null, default -> json.put(keyString, value);
            }
        });
        return json;
    }

    private JSONArray prepare(List<?> items) {
        var temp = new JSONArray(items.size());
        items.forEach(item -> {
            switch (item) {
                case List<?> objects -> temp.add(prepare(objects));
                case Map<?, ?> map -> temp.add(prepare(map));
                case String s -> temp.add(prepare(s));
                case null, default -> temp.add(item);
            }
        });
        return temp;
    }

    private Object prepare(String value) {
        var matcher = FILE_PATTERN.matcher(value);
        if (matcher.find()) {
            var result = Loader.toJSON(matcher.group(1));
            return switch (result) {
                case List<?> objects -> prepare(objects);
                case Map<?, ?> object -> prepare(object);
                case null, default -> result;
            };
        }
        return value;
    }

    public boolean isSampler() {
        return sampler;
    }

    public MiGooVariables getVariables() {
        return (MiGooVariables) get(VARIABLES);
    }
}