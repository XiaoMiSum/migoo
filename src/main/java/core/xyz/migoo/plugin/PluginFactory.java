/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.plugin;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2020/8/30 12:53
 */
public class PluginFactory {

    private static final Map<String, Plugin> PLUGINS = new HashMap<>(16);

    public static void create(JSONObject plugins) throws Exception {
        if (plugins != null && !plugins.isEmpty()) {
            for (Map.Entry<String, Object> entry : plugins.entrySet()) {
                JSONObject value = (JSONObject) entry.getValue();
                String clazz = value.get("package") != null ? value.get("package") + "." + StringUtil.initialToUpperCase(entry.getKey())
                        : String.format("components.xyz.migoo.plugins.%s.%s",
                        entry.getKey().toLowerCase(), StringUtil.initialToUpperCase(entry.getKey()));
                Plugin plugin = (Plugin) Class.forName(clazz).newInstance();
                plugin.initialize(value);
                PLUGINS.put(plugin.getClass().getSimpleName().toUpperCase(), plugin);
            }
        }
    }

    public static Plugin get(String pluginName) {
        return PLUGINS.get(pluginName.toUpperCase());
    }

    public static void close() {
        for (Map.Entry<String, Plugin> entry : PLUGINS.entrySet()) {
            entry.getValue().close();
        }
        PLUGINS.clear();
    }
}
