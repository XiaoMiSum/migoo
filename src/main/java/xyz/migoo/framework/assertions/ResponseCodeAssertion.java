package xyz.migoo.framework.assertions;

import xyz.migoo.simplehttp.Response;

/**
 * @author xiaomi
 * @date 2019-04-13 21:37
 */
public class ResponseCodeAssertion extends AbstractAssertion {

    @Override
    public void setActual(Object actual) {
        Response response = (Response)actual;
        this.actual = response.statusCode();
    }
}
