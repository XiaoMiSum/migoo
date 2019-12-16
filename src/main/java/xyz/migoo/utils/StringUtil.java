package xyz.migoo.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串工具类
 *
 * @author xiaomi
 */
public class StringUtil {

    /**
     * 将null转为空字符串
     *
     * @param cs
     * @return "" or cs
     */
    public static String toEmpty(String cs) {
        return cs == null || "NULL".equals(cs.toUpperCase()) ? "" : cs;
    }

    public static String trimAny(String cs) {
        StringBuilder sb = new StringBuilder();
        for (char aChar : cs.toCharArray()) {
            if (aChar != 32) {
                sb.append(aChar);
            }
        }
        return sb.toString();
    }

    public static boolean isEmpty(String cs) {
        return cs == null || trimAny(cs).length() == 0;
    }

    public static String valueOf(Object obj){
        return obj == null ? null : String.valueOf(obj);
    }

    public static String initialToUpperCase(String cs){
        char[] chars = cs.toCharArray();
        chars[0] -= chars[0] >= 97 ? 32 : 0;
        return String.valueOf(chars);
    }
}
