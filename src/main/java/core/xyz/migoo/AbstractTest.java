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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import components.migoo.xyz.reports.Report;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.functions.FunctionHelper;
import core.xyz.migoo.vars.Vars;
import core.xyz.migoo.http.MiGooRequest;
import core.xyz.migoo.utils.TypeUtil;
import core.xyz.migoo.vars.VarsHelper;
import xyz.migoo.simplehttp.Response;

import java.util.List;
import java.util.Vector;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public abstract class AbstractTest implements ITest {

    private final JSONArray setup = new JSONArray();
    private final JSONArray teardown = new JSONArray();
    private final Vars vars = new Vars(100);

    private final String tName;
    private final Vector<AbstractTest> rTests = new Vector<>(10);
    private List<TestChecker> checkers;

    protected JSONObject requestConfig;
    protected MiGooRequest request;
    protected Response response;

    protected boolean isSkipped;

    private long startTime;

    private long endTime;

    private String status;

    private Throwable throwable;

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
    @Override
    public String getTestName() {
        return tName;
    }

    public void initTest(JSONObject config, JSONObject dataset) {
        if (config != null) {
            this.requestConfig = config.getJSONObject("request");
            this.isSkipped = TypeUtil.booleanOf(config.get("skip"));
        }
        if (dataset != null) {
            this.addVars(dataset.getJSONObject("vars"));
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
     * @param vars the variables to set
     */
    public void addVars(JSONObject vars) {
        if (vars != null) {
            this.vars.putAll(vars);
        }
    }

    /**
     * add the variables of test.
     *
     * @param key   the variable key
     * @param value the variable value
     */
    public void addVars(String key, Object value) {
        this.vars.put(key, value);
    }

    /**
     * get the variables of test.
     *
     */
    public Vars getVars() {
        return this.vars;
    }

    public void addToGlobals() {
        Vars.addToGlobals(tName, vars);
    }

    public void processVariable() {
        VarsHelper.bindAndEval(vars, vars);
        VarsHelper.bindAndEval(requestConfig, vars);
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
     * @throws FunctionException e
     */
    public void setup() throws FunctionException {
        // bind variable to setUp (this.variables -> this.setUp)
        for (int i = 0; i < setup.size(); i++) {
            String func = VarsHelper.bindMultiVariable(setup.getString(i), vars);
            FunctionHelper.execute(func, vars);
        }
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
    void teardown() {
        // bind variable to setUp (this.variables -> this.teardown)
        for (int i = 0; i < teardown.size(); i++) {
            try {
                String func = VarsHelper.bindMultiVariable(teardown.getString(i), vars);
                FunctionHelper.execute(func, vars);
            } catch (FunctionException e) {
                Report.log(teardown.getString(i) + " error", e);
            }
        }
    }

    void addTest(AbstractTest test) {
        rTests.add(test);
    }

    public Vector<AbstractTest> getRunTests() {
        return this.rTests;
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

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public void end() {
        this.endTime = System.currentTimeMillis();
    }

    @Override
    public long getEndTime() {
        return this.endTime;
    }

    @Override
    public String getStatus() {
        return isSkipped ? "skipped" : this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    void run(IResult result, String type) {
        try {
            Report.log("{} begin: {}", type, this.getTestName());
            this.start();
            if (!this.isSkipped) {
                this.processVariable();
                this.setup();
                this.getRunTests().forEach(test -> {
                    test.addVars(getVars());
                    test.run(result);
                });
                this.teardown();
                this.setStatus("success");
            }
        } catch (Throwable t) {
            Report.log(type + " run error. ", t);
            this.setThrowable(t);
        } finally {
            this.end();
            Report.log("{} end: {}", type, this.getTestName());
        }
    }
}
