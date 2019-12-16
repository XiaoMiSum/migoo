package xyz.migoo.utils;

import java.text.SimpleDateFormat;

/**
 * @author xiaomi
 */
public class DateUtil {

    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String TODAY_DATE = format(YYYYMMDD);

    public static String format(String pattern, long time) {
        return new SimpleDateFormat(pattern).format(time);
    }

    public static String format(String pattern) {
        return new SimpleDateFormat(pattern).format(System.currentTimeMillis());
    }
}
