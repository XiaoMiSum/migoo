package xyz.migoo.framework.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.assertions.Validate;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.framework.http.Response;
import xyz.migoo.parser.BindVariable;
import xyz.migoo.utils.Hook;
import xyz.migoo.utils.MiGooLog;

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
    private List<Validate> validate = new ArrayList<>();

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
    void addVariables(JSONObject variables) {
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
     * @throws InvokeException e
     */
    public void setUp(String type) throws InvokeException {
        // bind variable to setUp (this.variables -> this.setUp)
        MiGooLog.log("{} begin", type);
        BindVariable.bind(variables, setUp);
        Hook.hook(setUp, variables);
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
     *
     * @throws InvokeException e
     */
    void teardown(String type) throws InvokeException {
        // bind variable to setUp (this.variables -> this.teardown)
        MiGooLog.log("{} begin", type);
        BindVariable.bind(variables, teardown);
        Hook.hook(teardown, variables);
        MiGooLog.log("{} end", type);
    }

    public Response response() {
        return response;
    }

    public List<Validate> validate() {
        return validate;
    }

    void validate(Object object) {
        JSONArray array = (JSONArray) object;
        for (int i = 0; i < array.size(); i++) {
            JSONObject json = array.getJSONObject(i);
            Validate validate = new Validate();
            validate.setCheckPoint(json.getString(CaseKeys.VALIDATE_CHECK));
            validate.setExcept(json.getString(CaseKeys.VALIDATE_EXPECT));
            validate.setFunc(json.getString(CaseKeys.VALIDATE_TYPE));
            this.validate.add(validate);
        }
    }
}
