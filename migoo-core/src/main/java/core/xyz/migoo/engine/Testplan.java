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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static core.xyz.migoo.testelement.AbstractTestElement.*;

/**
 * @author xiaomi
 */
public class Testplan extends JSONObject {

    private final boolean sampler;

    public Testplan(JSONObject json) {
        // 同时包含 children 和 title 这两个key 即 认为是 migoo 的测试集合
        var isMiGooSuite = json.containsKey(CHILDREN) && json.containsKey(TITLE);
        var clazz = TestElementService.getServiceClass(json.getString(TEST_CLASS));
        sampler = Objects.nonNull(clazz) && Sampler.class.isAssignableFrom(clazz);
        TestPlanValidator.verify(json, isMiGooSuite ? sampler ? clazz.getSimpleName() : "testsuite" : "");
        // migoo 测试集合 或者 注册的 test class 都是 migoo 测试组件
        initialize(json, isMiGooSuite || Objects.nonNull(clazz));
    }

    private void initialize(JSONObject json, boolean isMiGoo) {
        // 1、先删除 MiGoo组件中的变量，减少一次遍历
        var variables = isMiGoo ? json.remove(VARIABLES) : null;
        json.forEach((key, value) -> {
            // 将 migoo 测试组件 的key 转换为小写
            var newKey = isMiGoo ? key.toLowerCase(Locale.ROOT) : key;
            if (value instanceof Map) {
                put(newKey, parse(json.getJSONObject(key)));
            } else if (value instanceof List) {
                var array = json.getJSONArray(key);
                var newValue = new JSONArray(array.size());
                for (int i = 0; i < array.size(); i++) {
                    var item = array.get(i);
                    // 这里主要是为了处理 配置的子组件，组件配置一定是Map
                    if (item instanceof Map) {
                        newValue.add(parse(array.getJSONObject(i)));
                    } else {
                        newValue.add(item);
                    }
                }
                put(newKey, newValue);
            } else {
                put(newKey, value);
            }
        });
        if (isMiGoo) {
            // 3、重新添加变量到MiGoo组件
            put(VARIABLES, Objects.isNull(variables) ? new MiGooVariables() :
                    variables instanceof MiGooVariables ? variables : new MiGooVariables((JSONObject) variables));
        }
    }

    private JSONObject parse(JSONObject json) {
        var isMiGooSuite = json.containsKey(CHILDREN) && json.containsKey(TITLE);
        var clazz = TestElementService.getServiceClass(json.getString(TEST_CLASS));
        if (isMiGooSuite || Objects.nonNull(clazz)) {
            return new Testplan(json);
        } else {
            return json;
        }
    }

    public boolean isSampler() {
        return sampler;
    }

    public MiGooVariables getVariables() {
        return (MiGooVariables) get(VARIABLES);
    }
}