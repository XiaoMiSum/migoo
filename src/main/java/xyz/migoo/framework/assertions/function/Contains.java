package xyz.migoo.framework.assertions.function;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class Contains extends AbstractFunction {

    @Override
    public Boolean assertTrue(Map<String, Object> data) {
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
