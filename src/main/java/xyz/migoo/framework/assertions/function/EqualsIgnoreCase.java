package xyz.migoo.framework.assertions.function;

import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
@Alias(aliasList = {"equalsIgnoreCase", "ignoreCase"})
public class EqualsIgnoreCase extends AbstractAssertFunction {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        return objectToString(data.get("actual")).equalsIgnoreCase(objectToString(data.get("expect")));
    }
}
