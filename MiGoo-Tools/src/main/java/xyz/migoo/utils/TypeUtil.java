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
                String strVal = ((String) value).toLowerCase();
                if (StringUtil.isNotBlank(StringUtil.toEmpty(strVal))) {
                    return null;
                }
                if (TRUE.equals(strVal) || ONE.equals(strVal)) {
                    return Boolean.TRUE;
                }
                if (FALSE.equals(strVal) || ZERO.equals(strVal)) {
                    return Boolean.FALSE;
                }
                if (Y.equals(strVal) || T.equals(strVal) || YES.equals(strVal)) {
                    return Boolean.TRUE;
                }
                if (F.equals(strVal) || N.equals(strVal) || NO.equals(strVal) ) {
                    return Boolean.FALSE;
                }
            }
            throw new RuntimeException("can not cast to boolean, value : " + value);
        }
    }
}
