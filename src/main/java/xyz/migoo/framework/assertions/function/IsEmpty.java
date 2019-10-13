package xyz.migoo.framework.assertions.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.utils.StringUtil;

import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class IsEmpty extends AbstractFunction {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        Object actual = data.get("actual");
        if (actual instanceof JSONObject) {
            return ((JSONObject) actual).isEmpty();
        }
        if (actual instanceof JSONArray) {
            return ((JSONArray) actual).isEmpty();
        }
        if (actual instanceof String) {
            return StringUtil.isEmpty((String) actual);
        }
        return actual == null;
    }
}
