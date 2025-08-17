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

package io.github.xiaomisum.ryze.function;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.function.Args;
import io.github.xiaomisum.ryze.core.function.Function;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Locale;

public class Faker implements Function {

    @Override
    public String key() {
        return "faker";
    }

    /**
     * 通过 Faker库生成假数据，支持2个参数
     * 参数：
     * key: 假数据类型，格式为 Faker库的ClassName.Method
     * local: 语言，可控，默认为 zh-CN
     */
    @Override
    public Object execute(ContextWrapper context, Args args) {
        checkMethodArgCount(args, 1, 2);
        var key = args.getString(0);
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("第一个参数不能为空，参考格式： Faker库的ClassName.Method");
        }
        var locale = args.size() < 2 ? "zh-CN" : args.getLast().toString();

        try {
            var faker = new com.github.javafaker.Faker(Locale.of(locale));
            var keys = key.split("\\.");
            var current = faker.getClass().getDeclaredMethod(StringUtils.uncapitalize(keys[0])).invoke(faker);
            Method method = current.getClass().getDeclaredMethod(StringUtils.uncapitalize(keys[1]));
            return method.invoke(current);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
