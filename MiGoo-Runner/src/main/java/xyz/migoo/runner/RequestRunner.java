package xyz.migoo.runner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.migoo.exception.ValidatorException;
import xyz.migoo.http.Client;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.utils.Log;

/**
 * @author xiaomi
 * @date 2018/7/24 20:34
 */
public class RequestRunner {

    private Request request;
    private TestSuite testSuite;
    private static Log log = new Log(LoggerFactory.getLogger(RequestRunner.class));

    public RequestRunner(Request request, TestSuite testSuite){
        this.request = request;
        this.testSuite = testSuite;
    }

    public void run(JSONObject testCase) throws ValidatorException {
        Response response = new Client().execute(this.request);
        this.testSuite.response(response);
        JSONArray validate = testCase.getJSONArray("validate");
        try {
            Validator.validation(response, validate);
        }catch (ValidatorException e){
            log.error(e.getMessage(), e);
            this.testSuite.failures(response.request().title());
            throw new ValidatorException(e.getMessage().replaceAll("\n","</br>"));
        }

    }
}
