/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package xyz.migoo.utils;

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
