package xyz.migoo.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 *
 * @author xiaomi
 */
public class StringUtil extends StringUtils {


    /**
     * 将null转为空字符串
     *
     * @param str
     * @return
     */
    public static String nullToEmpty(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static String trim(String s) {
        return s.replaceAll(" ", "");
    }

    public static String substring(String s, int i) {
        return s.substring(0, s.length() - i);
    }
}
