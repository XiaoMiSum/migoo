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

    private CaseSuite caseSuite;
    private Task task;
    private JSONObject testCase;
    private List<String> validate;
    private Request request;
    private Response response;
    private Exception error;
    private AssertionException failure;


    TestCase(Task task, JSONObject testCase, CaseSuite caseSuite){
        super(testCase.getString(CaseKeys.CASE_TITLE));
        this.task = task;
        this.testCase = testCase;
        this.caseSuite = caseSuite;
        validate = new ArrayList<>();
    }

    @Override
    public void runTest(){
        try {
            this.caseSuite.addRTests();
            this.task.run(this.testCase);
            this.request = task.request();
            this.response = task.response();
            this.validate((JSONArray)task.validate());
        }catch (AssertionException failure){
            this.failure = failure;
            this.caseSuite.addFTests();
        }catch (Exception error){
            this.error = error;
            this.caseSuite.addETests();
        }
    }

    private void validate(JSONArray validate){
        for (int i = 0; i < validate.size(); i++) {
            String s = JSONObject.toJSONString(validate.getJSONObject(i), SerializerFeature.WriteNullStringAsEmpty);
            this.validate.add(s);
        }
    }
    List<String> validate(){
        return validate;
    }

    Request request(){
        return request;
    }

    public Response response(){
        return response;
    }

    AssertionException failure(){
        return failure;
    }

    public Exception error(){
        return error;
    }

    public String name(){
        return getName();
    }
}
