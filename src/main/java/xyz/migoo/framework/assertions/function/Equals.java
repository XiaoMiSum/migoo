package xyz.migoo.framework.assertions.function;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
@Alias(aliasList = {"==", "===", "eq", "equal", "equals", "is"})
public class Equals extends AbstractAssertFunction {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        Object actual = data.get("actual");
        Object expect = data.get("expect");
        String str1 = objectToString(actual);
        String str2 = objectToString(expect);
        if (actual instanceof Number || expect instanceof Number){
            str1 = "null".equals(str1) ? "0" : str1;
            str2 = "null".equals(str2) ? "0" : str2;
            return new BigDecimal(str1).compareTo(new BigDecimal(str2)) == 0;
        }
        return str1.equals(str2);
    }
}
