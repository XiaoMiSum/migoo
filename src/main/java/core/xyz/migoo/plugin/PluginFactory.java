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
import components.xyz.migoo.reports.Report;
import core.xyz.migoo.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2020/8/30 12:53
 */
public class PluginFactory {

    private static final Map<String, IPlugin> PLUGINS = new HashMap<>(16);

    public static void create(JSONObject plugins) {
        for (Map.Entry<String, Object> entry : plugins.entrySet()) {
            try {
                JSONObject value = (JSONObject) entry.getValue();
                String clazz = value.get("package") != null ? value.get("package") + "." + StringUtil.initialToUpperCase(entry.getKey())
                        : String.format("components.xyz.migoo.plugins.%s.%s",
                        entry.getKey().toLowerCase(), StringUtil.initialToUpperCase(entry.getKey()));
                IPlugin plugin = (IPlugin) Class.forName(clazz).newInstance();
                plugin.init(value);
                PLUGINS.put(plugin.getClass().getSimpleName().toUpperCase(), plugin);
            } catch (Exception e) {
                Report.log("plugin init error: {}, {}", entry.getKey(), e.getMessage());
            }
        }
    }

    public static IPlugin get(String pluginName){
        return PLUGINS.get(pluginName.toUpperCase());
    }
}
