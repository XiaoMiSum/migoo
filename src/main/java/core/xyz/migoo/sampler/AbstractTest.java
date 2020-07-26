/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.sampler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.exception.SkippedRun;
import xyz.migoo.framework.functions.FunctionFactory;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.http.MiGooRequest;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.simplehttp.Response;
import xyz.migoo.utils.TypeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public abstract class AbstractTest implements ITest {


    private final JSONArray setup = new JSONArray();
    private final JSONArray teardown = new JSONArray();
    final Vars vars = new Vars(100);

    private final String tName;
    public Vector<AbstractTest> rTests = new Vector<>(10);
    private List<TestChecker> checkers;

    protected JSONObject requestConfig;
    protected JSONObject jdbcConfig;
    protected JSONObject redisConfig;

    protected MiGooRequest request;
    protected Response response;

    protected boolean isSkipped;

    AbstractTest(String name) {
        this.tName = name;
    }

    /**
     * Returns the name of the test. Not all
     * test suites have a name and this method
     * can return null.
     *
     * @return test name
     */
    public String getName() {
        return tName;
    }

    public void initTest(JSONObject config, JSONObject dataset){
        if (config != null) {
            this.requestConfig = config.getJSONObject("request");
            this.jdbcConfig = config.getJSONObject("jdbc");
            this.redisConfig = config.getJSONObject("redis");
            this.isSkipped = TypeUtil.booleanOf(config.get("skip"));
        }
        if (dataset != null) {
            this.addVariables(dataset.getJSONObject("vars"));
            // add setUp、teardown
            this.addSetup(dataset.getJSONArray("setup"));
            // 3. add config.beforeClass to
            this.addTeardown(dataset.getJSONArray("teardown"));
        }
    }

    protected void initRequest(JSONObject requestConfig) {
        this.requestConfig = this.requestConfig != null ? this.requestConfig : new JSONObject();
        JSONObject headers = this.requestConfig.get("headers") != null ?
                this.requestConfig.getJSONObject("headers") : new JSONObject();
        if (requestConfig != null) {
            this.requestConfig.putAll(requestConfig);
            if (requestConfig.get("headers") != null) {
                headers.putAll(requestConfig.getJSONObject("headers"));
            }
        }
        this.requestConfig.put("headers", headers);
    }

    /**
     * add the variables of test.
     *
     * @param variables the variables to set
     */
    public void addVariables(JSONObject variables) {
        if (variables != null) {
            this.vars.putAll(variables);
        }
    }

    /**
     * add the variables of test.
     *
     * @param key   the variable key
     * @param value the variable value
     */
    public void addVariables(String key, String value) {
        this.vars.put(key, value);
    }


    public void addToGlobals() {
        Vars.addToGlobals(tName, vars);
    }


    public void processVariable() {
        VariableHelper.bindAndEval(vars, vars);
        VariableHelper.bindAndEval(requestConfig, vars);
        VariableHelper.bindAndEval(jdbcConfig, vars);
        VariableHelper.bindAndEval(redisConfig, vars);
    }

    /**
     * add the setup of the suite or case.
     *
     * @param setup the setUp to set
     */
    void addSetup(JSONArray setup) {
        if (setup != null) {
            this.setup.addAll(setup);
        }
    }

    /**
     * invoke the setUp of the test.
     *
     * @throws ExecuteError e
     */
    public void setup(String type) throws ExecuteError {
        // bind variable to setUp (this.variables -> this.setUp)
        MiGooLog.log("{} begin", type);
        for (int i = 0; i < setup.size(); i++) {
            String func = VariableHelper.bindMultiVariable(setup.getString(i), vars);
            FunctionFactory.execute(func, vars);
        }
        MiGooLog.log("{} end", type);
    }

    /**
     * add the teardown of the test.
     *
     * @param teardown the teardown to set
     */
    void addTeardown(JSONArray teardown) {
        if (teardown != null) {
            this.teardown.addAll(teardown);
        }
    }

    /**
     * invoke the teardown of the test.
     */
    void teardown(String type) {
        // bind variable to setUp (this.variables -> this.teardown)
        MiGooLog.log("{} begin", type);
        for (int i = 0; i < teardown.size(); i++) {
            try {
                String func = VariableHelper.bindMultiVariable(teardown.getString(i), vars);
                FunctionFactory.execute(func, vars);
            } catch (ExecuteError e) {
                MiGooLog.log(teardown.getString(i) + " error", e);
            }
        }
        MiGooLog.log("{} end", type);
    }

    void addRTest(AbstractTest test) {
        rTests.add(test);
    }

    public MiGooRequest request() {
        return request;
    }

    public Response response() {
        return response;
    }

    public List<TestChecker> checkers() {
        return checkers;
    }

    void checkers(List<TestChecker> checkers) {
        this.checkers = checkers;
    }

    @Override
    public int countTestCases() {
        return rTests.size();
    }

    public void run(TestResult result, String type) {
        try {
            MiGooLog.log("{} begin: {}", type, this.getName());
            if (this.isSkipped) {
                throw new SkippedRun(this.getName());
            }
            this.processVariable();
            this.setup(type + " setup");
            this.rTests.forEach(test -> {
                test.addVariables(vars);
                test.run(result);
            });
            this.teardown(type + " teardown");
        } catch (ExecuteError e) {
            MiGooLog.log(type + " run error. ", e);
        } finally {
            MiGooLog.log("{} end: {}", type, this.getName());
        }
    }
}
