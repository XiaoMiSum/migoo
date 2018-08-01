package xyz.migoo.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

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
}
