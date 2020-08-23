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


package core.xyz.migoo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaomi
 */
public class DateUtil {

    public static final String TODAY_DATE = format("yyyyMMdd");

    public static String format(String pattern, long time) {
        return new SimpleDateFormat(pattern).format(time);
    }

    public static String format(String pattern, Date date) {
        return format(pattern, date.getTime());
    }

    public static String format(String pattern) {
        return format(pattern, System.currentTimeMillis());
    }

    public static String format() {
        return format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
    }

    public static String format(Date date) {
        return format("yyyy-MM-dd HH:mm:ss", date.getTime());
    }
}
