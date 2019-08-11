package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONPath;
import xyz.migoo.framework.http.Response;

/**
 * @author xiaomi
 * @date 2019-04-13 21:37
 */
public class JSONAssertion extends AbstractAssertion {

    private static final String BODY_ = "body";
    private static final String JSON_ = "json";

    private String jsonPath;

    JSONAssertion(String jsonPath){
        if (jsonPath.startsWith(JSON_) || jsonPath.startsWith(BODY_)){
            jsonPath = "$" + jsonPath.substring(BODY_.length());
        }
        this.jsonPath = jsonPath;
    }

    @Override
    public void setActual(Object actual) {
        Response response = (Response) actual;
        this.actual = JSONPath.read(response.body(), jsonPath);
    }
}
