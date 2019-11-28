package xyz.migoo.framework;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
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
    JSONObject variables = new JSONObject();

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
            VariableHelper.hook(setUp.getString(i), variables);
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
                VariableHelper.hook(teardown.getString(i), variables);
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
}
