package xyz.migoo.test.example;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.assertions.Assertion;
import xyz.migoo.simplehttp.Response;

/**
 * @author xiaomi
 * @date 2019-04-14 02:22
 */
public class PackageAssertion implements Assertion {

    private Object actual;

    @Override
    public boolean assertThat(JSONObject data) {
        String s1 = String.valueOf(data.get("expected"));
        return s1.equals(getActual());
    }

    @Override
    public void setActual(Object actual) {
        this.actual = ((Response) actual).text();
    }

    @Override
    public Object getActual() {
        return this.actual;
    }
}
