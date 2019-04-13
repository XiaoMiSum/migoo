package xyz.migoo.assertions.function;

import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-04-14 02:05
 */
public interface IFunction {

    /**
     * Implement the interface to extend the assertion method
     * get expected values from Map Object
     * and the expected values can be null
     * use:  data.get("expect")
     *
     * @param data Objects that hold the actual and expected values
     * @return Boolean Object
     */
    Boolean assertThat(Map<String, Object> data);
}
