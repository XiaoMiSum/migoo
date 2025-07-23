/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.variable;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
public class VariableUtils {

    public static final Pattern VARS_PATTERN = Pattern.compile("\\$\\{(\\w+)?}");

    public static final Pattern FUNC_PATTERN = Pattern.compile("__(\\w+)\\(([.]*[^)]*)?\\)");

    public static final Pattern KWARGS_PATTERN = Pattern.compile("((\\w+)=(.+))+");

    public static final Pattern FILE_PATTERN = Pattern.compile("^@F\\((.+)+\\)");

    public static boolean isVars(String str) {
        return VARS_PATTERN.matcher(str).find();
    }

    public static boolean isFunc(String str) {
        return !StringUtils.isEmpty(str) && FUNC_PATTERN.matcher(str).find();
    }

    public static boolean isKwArgs(String str) {
        return KWARGS_PATTERN.matcher(str).find();
    }

    public static boolean isFile(String str) {
        return FILE_PATTERN.matcher(str).find();
    }


    public static void main(String[] args) {
        System.out.println("${var}: " + VARS_PATTERN.matcher("${var}").matches());
        System.out.println("${var}${var}: " + VARS_PATTERN.matcher("${var}${var}").matches());
        System.out.println("${var}${var}qweqwe: " + VARS_PATTERN.matcher("qwe${var}${var}qweqwe").matches());
        System.out.println("${var}qeqeqwe${var}: " + VARS_PATTERN.matcher("${var}qeqeqwe${var}").matches());
        System.out.println("__func(): " + FUNC_PATTERN.matcher("__func()").matches());
        System.out.println("__func(1): " + FUNC_PATTERN.matcher("__func(1)").matches());
        System.out.println("__func(a=1,b=2): " + FUNC_PATTERN.matcher("__func(a=1,b=2)").matches());
        // 不支持函数的参数为另一个函数
        System.out.println("__func(__func()): " + FUNC_PATTERN.matcher("__func(__func())").matches());
        System.out.println("__func()__func(): " + FUNC_PATTERN.matcher("__func()__func()").matches());
        System.out.println("__func(a=1,b=2)245234__func(): " + FUNC_PATTERN.matcher("__func(a=1,b=2)245234__func()").matches());
        System.out.println("__func(a=1,b=2)245234__func()wer: " + FUNC_PATTERN.matcher("__func(a=1,b=2)245234__func()wer").matches());
        // 不支持函数的参数为另一个函数
        System.out.println("__func(a=1,b=__func()): " + FUNC_PATTERN.matcher("__func(a=1,b=__func())").matches());
        System.out.println("__func(a=1,b=${var}): " + FUNC_PATTERN.matcher("__func(a=1,b=${var})").matches());
        var matcher = FUNC_PATTERN.matcher("__func(1)");
        if (matcher.find()) {
            System.out.println("函数名称1: " + matcher.group(1));
            System.out.println("参数：" + matcher.group(2));
        }
        if (matcher.matches()) {
            System.out.println("函数名称2: " + matcher.group(1));
            System.out.println("参数：" + matcher.group(2));
        }
        if (matcher.find(0)) {
            System.out.println("函数名称3: " + matcher.group(1));
            System.out.println("参数：" + matcher.group(2));
        }
    }
}