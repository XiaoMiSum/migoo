package xyz.migoo.assertions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.assertions.function.Function;
import xyz.migoo.exception.AssertionException;

/**
 * @author xiaomi
 * @date 2019-04-14 02:36
 */
public class CustomAssertion extends AbstractAssertion{
    @Override
    public void setActual(Object actual) {
        String str = (String)actual;
        this.actual = str.substring(str.indexOf(".") + 1);
    }

    @Override
    public Boolean assertThat(JSONObject data) throws AssertionException {
        return Function.custom((String) actual, data.get("expect"));
    }
}
