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
import core.xyz.migoo.variable.MiGooVariables;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static core.xyz.migoo.testelement.AbstractTestElement.*;

/**
 * @author xiaomi
 */
public class Testplan extends JSONObject {

    private final boolean standardSampler;

    public Testplan(JSONObject json) {
        standardSampler = !json.containsKey(CHILDS) && !json.containsKey(CHILD);
        Object o = json.remove(VARIABLES);
        boolean isTestElement = json.containsKey(TEST_CLASS) || json.containsKey(CHILDS) || json.containsKey(CHILD);
        // migoo 定义的组件需要添加变量对象
        if (isTestElement) {
            put(VARIABLES, o instanceof MiGooVariables ? o : new MiGooVariables((JSONObject) o));
        }
        Set<String> keys = json.keySet();
        keys.forEach(key -> {
            // 先取出原始数据，然后将 migoo定义的组件 key转换为小写
            Object obj = json.get(key);
            String newKey = isTestElement ? key.toLowerCase(Locale.ROOT) : key;
            newKey = CHILDS.equals(newKey) ? CHILD : newKey;
            if (obj instanceof Map) {
                put(newKey, new Testplan(json.getJSONObject(key)));
            } else if (obj instanceof List) {
                JSONArray array = json.getJSONArray(key);
                JSONArray newValue = new JSONArray(array.size());
                for (int i = 0; i < array.size(); i++) {
                    newValue.add(new Testplan(array.getJSONObject(i)));
                }
                put(newKey, newValue);
            } else {
                put(newKey, obj);
            }
        });
    }

    public boolean isStandardSampler() {
        return standardSampler;
    }

    public MiGooVariables getVariables() {
        return (MiGooVariables) get(VARIABLES);
    }
}