package xyz.migoo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
* @author xiaomi
 */
public class DateUtil {

    private static Log log = new Log(DateUtil.class);

    public static final String YYYY_MMDD_HHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MMDD_HH_MM_SS = "yyyy-MM-dd HH-mm-ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String TODAY = format(YYYY_MMDD);

    public static String format(String pattern,long time) {
        return new SimpleDateFormat(pattern).format(time);
    }

    public static String format(String pattern,int later) {
        long longLater = later * 1000;
        return new SimpleDateFormat(pattern).format(System.currentTimeMillis() + longLater);
    }

    public static String format(String pattern) {
        return new SimpleDateFormat(pattern).format(System.currentTimeMillis());
    }

    public static String format(long startTime, long endTime) {
        long day = (endTime - startTime) / (24 * 60 * 60 * 1000);
        long hour = ((endTime - startTime) / (60 * 60 * 1000) - day * 24);
        long minute = (((endTime - startTime) / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = ((endTime - startTime) / 1000 - day * 24 * 60 * 60 - hour
                * 60 * 60 - minute * 60);
        return day + "天" + hour + "时" + minute + "分" + second + "秒";
    }

    public static String format() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DATE);
        int minute = ca.get(Calendar.MINUTE);
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        int second = ca.get(Calendar.SECOND);
        return year + "_" + (month + 1) + "_"
                + day + "_" + hour + "_"
                + minute + "_" + second;
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time * 1000L);
            log.debug("wait " + time + " seconds");
        } catch (InterruptedException e) {
            log.error("wait exception",e);
        }
    }
}
