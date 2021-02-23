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

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2018/10/10 19:49
 */
public class TypeUtil {

    private static final String TRUE = "TRUE";
    private static final String ONE = "1";
    private static final String Y = "Y";
    private static final String T = "T";
    private static final String YES = "YES";
    private static final Pattern REGEX_NUMBER = Pattern.compile("^[-+]?[0-9]+(\\.[0-9]+)?$");
    public static final Pattern VARS_PATTERN = Pattern.compile("\\$\\{(\\w+)}");
    public static final Pattern FUNC_PATTERN = Pattern.compile("__([A-Za-z0-9]+)\\(([\\w]*[=]\\S[^)]*)?\\)");

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
            return TRUE.equals(strVal) || ONE.equals(strVal) || Y.equals(strVal) || T.equals(strVal) || YES.equals(strVal);
        }
        return false;
    }

    public static boolean isNumber(String str){
        return REGEX_NUMBER.matcher(str).find();
    }

    public static boolean isBoolean(String str){
        return Boolean.TRUE.toString().equalsIgnoreCase(str) || Boolean.FALSE.toString().equalsIgnoreCase(str);
    }

    public static boolean isVars(String str){
        return VARS_PATTERN.matcher(str).find();
    }

    public static boolean isFunc(String str){
        return !StringUtils.isEmpty(str) && FUNC_PATTERN.matcher(str).find();
    }
}
