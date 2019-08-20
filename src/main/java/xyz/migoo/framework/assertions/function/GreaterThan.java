package xyz.migoo.framework.assertions.function;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class GreaterThan extends AbstractFunction {

    @Override
    public Boolean assertTrue(Map<String, Object> data) {
        try {
            BigDecimal b1 = new BigDecimal(String.valueOf(data.get("actual")));
            BigDecimal b2 = new BigDecimal(String.valueOf(data.get("expect")));
            return b1.compareTo(b2) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
