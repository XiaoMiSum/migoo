package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONObject;
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
     * {@linkplain ResponseAssertion ResponseAssertion}
     * {@linkplain ResponseCodeAssertion ResponseCodeAssertion}
     *
     * @param data Objects that hold expected values
     * @return assert result
     * @throws ExecuteError
     */
    boolean assertThat(JSONObject data) throws ExecuteError;
}
