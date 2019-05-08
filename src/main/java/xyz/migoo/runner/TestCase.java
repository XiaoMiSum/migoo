package xyz.migoo.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.AssertionException;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;

import java.util.ArrayList;
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

    public TestCase(Task task, JSONObject testCase, TestSuite testSuite){
        super(testCase.getString(CaseKeys.CASE_TITLE));
        this.task = task;
        this.testCase = testCase;
        this.testSuite = testSuite;
        this.testCase = testCase;
        this.validate = new ArrayList<>();
        super.setName(testCase.getString(CaseKeys.CASE_TITLE));
    }

    @Override
    public void runTest(){
        try {
            this.testSuite.addRTests();
            this.task.run(this.testCase, this);
        }catch (AssertionException failure){
            this.failure = failure;
            this.testSuite.addFTests();
        }catch (Exception error){
            this.error = error;
            this.testSuite.addETests();
        }
        this.testSuite.addTest(this);
    }

    List<String> validate(){
        return validate;
    }

    public void validate(JSONArray validate){
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
}
