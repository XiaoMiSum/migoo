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

package core.xyz.migoo;

import com.alibaba.fastjson.JSONObject;
import components.xyz.migoo.reports.Report;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.plugin.Plugin;
import core.xyz.migoo.utils.StringUtil;
import core.xyz.migoo.vars.VarsHelper;

import java.util.Date;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-10 11:07
 */
public class TestSuite extends Test {

    public TestSuite(TestContext context) {
        super(context);
        context.getSets().forEach(set ->
                super.addTest(new TestSet(set, context))
        );
    }

    @Override
    public IResult run() {
        Report.log("Beginning of the test，ID：{}，name：{}", this.getTestId(), this.getTestName());
        return super.run();
    }

    @Override
    public void processVariable() throws FunctionException {
        super.processVariable();
        VarsHelper.convertVariables(context.getReportConfig(), super.getVars());
        VarsHelper.convertVariables(context.getMailConfig(), super.getVars());
        VarsHelper.convertVariables(context.getPlugins(), super.getVars());
    }

    @Override
    public void setup() throws Exception {
        this.startTime = new Date();
        if (!isSkipped()) {
            this.processVariable();
            this.initializePlugins();
            super.setup();
        }
    }

    @Override
    public void teardown() {
        super.teardown();
        this.closePlugins();
    }

    private void initializePlugins() throws Exception {
        JSONObject plugins = new JSONObject();
        if (context.getPlugins() != null && !context.getPlugins().isEmpty()) {
            for (Map.Entry<String, Object> entry : context.getPlugins().entrySet()) {
                JSONObject value = (JSONObject) entry.getValue();
                String clazz = value.get("class") != null ? value.getString("class")
                        : String.format("components.xyz.migoo.plugins.%s.%s",
                        entry.getKey().toLowerCase(), StringUtil.initialToUpperCase(entry.getKey()));
                Plugin plugin = (Plugin) Class.forName(clazz).newInstance();
                plugin.initialize(value);
                plugins.put(entry.getKey().toLowerCase(), plugin);
            }
        }
        super.getVars().put("migoo.plugins", plugins);
    }

    private void closePlugins() {
        JSONObject plugins = super.getVars().getJSONObject("migoo.plugins");
        if (plugins != null && !plugins.isEmpty()) {
            for (Map.Entry<String, Object> entry : plugins.entrySet()) {
                ((Plugin) entry.getValue()).close();
            }
        }
    }
}
