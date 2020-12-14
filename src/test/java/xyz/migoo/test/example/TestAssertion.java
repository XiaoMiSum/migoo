package xyz.migoo.test.example;

import core.xyz.migoo.Validator;
import core.xyz.migoo.assertions.Assertion;
import xyz.migoo.simplehttp.Response;

/**
 * @author xiaomi
 * @date 2019-04-14 02:22
 */
public class TestAssertion implements Assertion {

    private Object actual;

    @Override
    public void assertThat(Validator validator) {
        String s1 = String.valueOf(validator.getExpected());
        boolean result = s1.equals(getActual());
        validator.setResult(result);
    }

    @Override
    public void setActual(Response actual) {
        this.actual = actual.text();
    }

    @Override
    public Object getActual() {
        return this.actual;
    }
}
