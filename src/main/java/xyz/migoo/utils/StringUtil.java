package xyz.migoo.utils;

import java.util.Random;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 *
 * @author xiaomi
 */
public class StringUtil {

    public static final String BASE_STR = "abcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * 将null转为空字符串
     *
     * @param str
     * @return
     */
    public static String toEmpty(String str) {
        return str == null || "NULL".equals(str.toUpperCase()) ? "" : str;
    }

    public static String trimAny(String s) {
        return s.replaceAll(" ", "");
    }

    public static boolean isEmpty(String cs) {
        return cs == null || trimAny(cs).length() == 0;
    }

    public static String valueOf(Object obj){
        return obj == null ? null : String.valueOf(obj);
    }

    public static String random(int length){
        return random(length, BASE_STR);
    }

    /**
     * 生成指定长度随机字符串
     * @param length    随机字符串长度
     * @param baseStr   基础字符串
     * @return
     */
    public static String random(int length, String baseStr){
        StringBuffer sb = new StringBuffer(length);
        Random random = new Random();
        for (int i = 0; i < length; i++){
            int num = random.nextInt(baseStr.length());
            sb.append(baseStr.charAt(num));
        }
        return sb.toString();
    }

    public static String indexToUpperCase(String str){
        char[] chars = str.toCharArray();
        chars[0] -= chars[0] >= 97 ? 32 : 0;
        return String.valueOf(chars);
    }
}
