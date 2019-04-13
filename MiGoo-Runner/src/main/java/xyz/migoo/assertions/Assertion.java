package xyz.migoo.assertions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.assertions.function.Function;
import xyz.migoo.exception.AssertionException;

/**
 * @author xiaomi
 * @date 2019-04-13 21:36
 */
public interface Assertion {


    /**
     * get expected values from JSONObject (data.get("expect"))
     * and invoke method to assert expected values
     *
     * support type list:
     * {@linkplain xyz.migoo.assertions.JSONAssertion JSONAssertion}
     * {@linkplain xyz.migoo.assertions.HTMLAssertion HTMLAssertion}
     * {@linkplain xyz.migoo.assertions.ResponseAssertion ResponseAssertion}
     * {@linkplain xyz.migoo.assertions.ResponseCodeAssertion ResponseCodeAssertion}
     * {@linkplain xyz.migoo.assertions.CustomAssertion CustomAssertion}
     *
     * support assert method list:
     * {@linkplain Function assert method }
     *
     * @param data Objects that hold expected values
     * @return assert result
     * @throws AssertionException
     */
    Boolean assertThat(JSONObject data);
}
