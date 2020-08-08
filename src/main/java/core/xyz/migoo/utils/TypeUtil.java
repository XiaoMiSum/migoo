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

/**
 * @author xiaomi
 * @date 2018/10/10 19:49
 */
public class TypeUtil {

    private static final String TRUE = "TRUE";
    private static final String ONE = "1";
    private static final String FALSE = "FALSE";
    private static final String ZERO = "0";
    private static final String Y = "Y";
    private static final String T = "T";
    private static final String YES = "YES";
    private static final String F = "F";
    private static final String N = "N";
    private static final String NO = "NO";

    public static boolean booleanOf(Object value) {
        if (value == null) {
            return false;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue() == 1;
        } else if (value instanceof String) {
            String strVal = ((String) value).toUpperCase();
            if (StringUtil.isEmpty(StringUtil.toEmpty(strVal))) {
                return false;
            }
            if (TRUE.equals(strVal) || ONE.equals(strVal) || Y.equals(strVal) || T.equals(strVal) || YES.equals(strVal)) {
                return true;
            }
            if (FALSE.equals(strVal) || ZERO.equals(strVal) || F.equals(strVal) || N.equals(strVal) || NO.equals(strVal)) {
                return false;
            }
        }
        return false;
    }
}
