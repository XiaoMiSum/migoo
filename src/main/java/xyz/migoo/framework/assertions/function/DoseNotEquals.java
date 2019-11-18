package xyz.migoo.framework.assertions.function;

import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class DoseNotEquals extends AbstractAssertFunction {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        return ! new Equals().assertTrue(data);
    }
}
