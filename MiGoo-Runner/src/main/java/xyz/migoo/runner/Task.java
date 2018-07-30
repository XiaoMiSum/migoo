package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import xyz.migoo.exception.ValidatorException;
import xyz.migoo.http.Client;
import xyz.migoo.http.Request;
import xyz.migoo.http.Response;
import xyz.migoo.parser.CaseSet;
import xyz.migoo.utils.Log;

/**
 * @author xiaomi
 * @date 2018/7/24 20:34
 */
public class Task {

    private Request request;
    private TestSuite testSuite;
    private static Log log = new Log(Task.class);

    public Task(Request request, TestSuite testSuite){
        this.request = request;
        this.testSuite = testSuite;
    }

    public synchronized void run(CaseSet.Case testCase) throws ValidatorException {
        Response response = new Client().execute(this.request);
        this.testSuite.response(response);
        JSON validate = testCase.getValidate();
        try {
            Validator.validation(response, validate);
        }catch (ValidatorException e){
            log.error(e.getMessage(), e);
            this.testSuite.failures(response.request().title());
            throw new ValidatorException(e.getMessage().replaceAll("\n","</br>"));
        }
    }
}
