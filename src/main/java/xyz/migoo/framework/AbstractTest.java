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

package xyz.migoo.framework;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.FunctionFactory;
import xyz.migoo.framework.functions.VariableHelper;
import xyz.migoo.framework.entity.Validate;
import xyz.migoo.http.MiGooRequest;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.simplehttp.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:50
 */
public abstract class AbstractTest implements ITest {

    private String fName;
    private JSONArray setUp = new JSONArray();
    private JSONArray teardown = new JSONArray();
    private List<Validate> validates = new ArrayList<>();

    MiGooRequest request;
    Response response;
    JSONObject variables = new JSONObject(true);

    AbstractTest(String name){
        this.fName = name;
    }
    /**
     * Returns the name of the test. Not all
     * test suites have a name and this method
     * can return null.
     *
     * @return test name
     */
    public String getName() {
        return fName;
    }

    /**
     * add the variables of test.
     *
     * @param variables the variables to set
     */
    public void addVariables(JSONObject variables) {
        if (variables != null) {
            this.variables.putAll(variables);
        }
    }

    /**
     * add the setUp of the suite or case.
     *
     * @param setUp the setUp to set
     */
    void addSetUp(JSONArray setUp) {
        if (setUp != null) {
            this.setUp.addAll(setUp);
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
        for (int i = 0; i < setUp.size(); i++) {
            String func = VariableHelper.bindConnectedVariables(setUp.getString(i), variables);
            FunctionFactory.execute(func, variables);
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
                String func = VariableHelper.bindConnectedVariables(teardown.getString(i), variables);
                FunctionFactory.execute(func, variables);
            } catch (ExecuteError e) {
                MiGooLog.log(teardown.getString(i) + " error", e);
            }
        }
        MiGooLog.log("{} end", type);
    }

    public MiGooRequest request() {
        return request;
    }

    public Response response() {
        return response;
    }

    public List<Validate> validates() {
        return validates;
    }

    void validates(List<Validate> validates) {
        this.validates = validates;
    }

    public JSONObject variables(){
        return variables;
    }
}
