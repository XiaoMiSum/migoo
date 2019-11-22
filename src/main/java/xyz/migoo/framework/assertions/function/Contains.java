package xyz.migoo.framework.assertions.function;

import java.util.*;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
@Alias(aliasList = {"contains", "contain", "ct", "⊆"})
public class Contains extends AbstractAssertFunction {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        Object actual = data.get("actual");
        Object expect = data.get("expect");
        if (actual instanceof String) {
            return ((String) actual).contains((String) expect);
        }
        if (actual instanceof Map) {
            Map json = (Map) actual;
            return json.containsValue(expect) || json.containsKey(expect);
        }
        if (actual instanceof List) {
            return ((List) actual).contains(expect);
        }
        return false;
    }
}
