package xyz.migoo.framework.assertions.function;

import xyz.migoo.framework.config.CaseKeys;

import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class EqualsIgnoreCase extends AbstractFunction {

    @Override
    public Boolean assertTrue(Map<String, Object> data) {
        return objectToString(data.get("actual")).equalsIgnoreCase(objectToString(data.get("expect")));
    }
}
