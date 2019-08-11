package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.assertions.function.Function;
import xyz.migoo.exception.ExecuteError;

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
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain HTMLAssertion HTMLAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     * {@linkplain ResponseCodeAssertion ResponseCodeAssertion}
     * {@linkplain CustomAssertion CustomAssertion}
     *
     * support assert method list:
     * {@linkplain Function assert method }
     *
     * @param data Objects that hold expected values
     * @return assert result
     * @throws ExecuteError
     */
    boolean assertThat(JSONObject data) throws ExecuteError;
}
