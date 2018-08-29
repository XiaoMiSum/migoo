package xyz.migoo.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Random;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 *
 * @author xiaomi
 */
public class StringUtil extends StringUtils {

    public static final String BASE_STR = "abcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * 将null转为空字符串
     *
     * @param str
     * @return
     */
    public static String nullToEmpty(String str) {
        return str == null || "NULL".equals(str.toUpperCase()) ? "" : str;
    }

    public static String trimAny(String s) {
        return s.replaceAll(" ", "");
    }

    public static String getStackTrace(Throwable throwable) {
        if (null == throwable) {
            return null;
        }
        Writer result = null;
        PrintWriter printWriter = null;
        try {
            result = new StringWriter();
            printWriter = new PrintWriter(result);
            throwable.printStackTrace(printWriter);
            return result.toString();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
            if (result != null) {
                try {
                    result.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String random(int length){
        return random(length, BASE_STR);
    }

    /**
     * 生成指定长度随机字符串
     * @param length
     * @param str
     * @return
     */
    public String random(int length, String str){
        StringBuffer sb = new StringBuffer(length);
        Random random = new Random();
        for (int i = 0; i < length; i++){
            int num = random.nextInt(str.length());
            sb.append(str.charAt(num));
        }
        return sb.toString();
    }
}
