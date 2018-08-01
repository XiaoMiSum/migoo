package xyz.migoo.utils;

import java.util.Locale;

/**
 * @author xiaomi
 * @date 2018/7/8 15:24
 */
public class Os {

    private static String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.US);
    private Os(){
    }

    public static boolean OS_MAC = OS_NAME.contains("mac");

    public static boolean OS_WINDOWS = OS_NAME.contains("windows");

    public static boolean OS_LINUX = OS_NAME.contains("linux");
}
