package xyz.migoo.runner;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.AssertionException;
import xyz.migoo.exception.IgnoreTestException;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.utils.TypeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/24 20:37
 */
public class TestCase extends junit.framework.TestCase{

    private TestSuite testSuite;
    private Task task;
    private JSONObject testCase;
    private List validate;
    private Request request;
    private Response response;
    private Exception error;
    private AssertionException failure;
    private IgnoreTestException ignore;

    TestCase(Task task, JSONObject testCase, TestSuite testSuite){
        super(testCase.getString(CaseKeys.CASE_TITLE));
        this.task = task;
        this.testCase = testCase;
        this.testSuite = testSuite;
        this.validate = new ArrayList<>();
        super.setName(testCase.getString(CaseKeys.CASE_TITLE));
    }

    @Override
    public void runTest() {
        try {
            this.testSuite.addRTests();
            Boolean aBoolean = TypeUtil.booleanOf(testCase.get(CaseKeys.CASE_IGNORE));
            if (aBoolean != null && aBoolean) {
                throw new IgnoreTestException("ignore test");
            }
            this.task.run(this.testCase, this);
        } catch (IgnoreTestException exception){
            this.ignore = exception;
            this.testSuite.addIgnore();
            validate(testCase.getJSONArray(CaseKeys.VALIDATE));
        } catch (AssertionException failure) {
            this.failure = failure;
            this.testSuite.addFTests();
        } catch (Exception error){
            this.error = error;
            this.testSuite.addETests();
        }
        this.testSuite.addTest(this);
    }

    List<HashMap<String, String>> validate(){
        return validate;
    }

    void validate(List validate){
        this.validate.addAll(validate);
    }

    Request request(){
        return request;
    }

    void request(Request request){
        this.request = request;
    }

    public Response response(){
        return response;
    }

    void response(Response response){
        this.response = response;
    }

    AssertionException failure(){
        return failure;
    }

    public Exception error(){
        return error;
    }

    Object ignore() {
        return ignore;
    }
}
