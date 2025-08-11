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

package io.github.xiaomisum.ryze;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.xiaomisum.ryze.core.config.RyzeVariables;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface.*;

/**
 * @author xiaomi
 */
public class JsonTree extends JSONObject {

    public JsonTree(Map<String, Object> testcase) {
        this(testcase instanceof JSONObject json ? json : new JSONObject(testcase));
    }


    public JsonTree(JSONObject testcase) {
        replaceExpiredKeys(testcase);
        var json = prepare(testcase);
        initialize(json, isRyzeTestFramework(json));
    }

    private void initialize(JSONObject json, boolean isRyze) {
        // 1、先删除 Ryze组件中的变量，减少一次遍历
        var variables = isRyze ? json.remove(VARIABLES) : null;
        json.forEach((key, value) -> {
            // 将 ryze 测试组件 的key 转换为小写
            var newKey = isRyze ? key.toLowerCase(Locale.ROOT) : key;
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
        if (isRyzeTestsuite(json)) {
            put(TEST_CLASS, "__testsuite__");
        }
        if (isRyzeSampler(json)) {
            put(TEST_CLASS, json.getString(TEST_CLASS).toLowerCase(Locale.ROOT));
        }
        if (isRyze) {
            // 3、重新添加变量到 Ryze 组件
            put(VARIABLES, variables instanceof RyzeVariables ? variables :
                    new RyzeVariables(Optional.ofNullable((JSONObject) variables).orElse(new JSONObject())));
        }
    }

    private JSONObject parse(JSONObject json) {
        return isRyzeTestFramework(json) ? new JsonTree(json) : json;
    }

    private JSONObject prepare(Map<?, ?> object) {
        var json = new JSONObject(object.size());
        object.keySet().forEach(key -> {
            var keyString = (String) key;
            var value = object.get(key);
            switch (value) {
                case Map<?, ?> map -> json.put(keyString, prepare(map));
                case List<?> objects -> json.put(keyString, prepare(objects));
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
                case null, default -> temp.add(item);
            }
        });
        return temp;
    }

    private boolean isRyzeTestFramework(JSONObject json) {
        return isRyzeTestsuite(json) || isRyzeSampler(json);
    }

    private boolean isRyzeTestsuite(JSONObject json) {
        return ((json.containsKey(CHILDREN) || json.containsKey(CHILD)) && json.containsKey(TITLE));
    }

    private boolean isRyzeSampler(JSONObject json) {
        return json.containsKey(TEST_CLASS);
    }

    private void replaceExpiredKeys(JSONObject json) {
        if (!isRyzeTestFramework(json)) {
            return;
        }
        if (json.containsKey(CHILD)) {
            json.put(CHILDREN, json.remove(CHILD));
        }
    }
}