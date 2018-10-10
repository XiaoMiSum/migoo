package xyz.migoo.utils;

/**
 * @author xiaomi
 * @date 2018/10/10 19:49
 */
public class TypeUtil {

    private static final String TRUE = "true";
    private static final String ONE = "1";
    private static final String FALSE = "false";
    private static final String ZERO = "0";
    private static final String Y = "Y";
    private static final String T = "T";
    private static final String YES = "YES";
    private static final String F = "F";
    private static final String N = "N";
    private static final String NO = "NO";

    public static Boolean booleanOf(Object value){
        if (value == null) {
            return null;
        }else if (value instanceof Boolean) {
            return (Boolean) value;
        }else if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        }else {
            if (value instanceof String) {
                String strVal = (String) value;
                if (StringUtil.isNotBlank(StringUtil.toEmpty(strVal))) {
                    return null;
                }
                if (TRUE.equalsIgnoreCase(strVal) || ONE.equals(strVal)) {
                    return Boolean.TRUE.booleanValue();
                }
                if (FALSE.equalsIgnoreCase(strVal) || ZERO.equals(strVal)) {
                    return Boolean.FALSE.booleanValue();
                }
                if (Y.equalsIgnoreCase(strVal) || T.equalsIgnoreCase(strVal) || YES.equalsIgnoreCase(strVal)) {
                    return Boolean.TRUE.booleanValue();
                }
                if (F.equalsIgnoreCase(strVal) || N.equalsIgnoreCase(strVal) || NO.equalsIgnoreCase(strVal) ) {
                    return Boolean.FALSE.booleanValue();
                }
            }
            throw new RuntimeException("can not cast to boolean, value : " + value);
        }
    }
}
