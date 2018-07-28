package xyz.migoo.utils;

import java.text.SimpleDateFormat;

/**
 * @author xiaomi
 * @date 2018/7/26 11:13
 */
public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYYMMDD_HH_MM_SS = "yyyy-MM-dd HH-mm-ss";

    public static String format(long time, String pattern){
        return new SimpleDateFormat(pattern).format(time);
    }
}
