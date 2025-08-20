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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL编码函数实现类
 *
 * <p>该类用于对字符串进行URL编码操作。
 * 常用于构造URL参数、编码特殊字符等场景。</p>
 *
 * <p>在测试用例中可以通过 ${url_encode()} 的方式调用该函数。</p>
 *
 * @author xiaomi
 */
public class UrlEncode implements Function {

    @Override
    public String key() {
        return "url_encode";
    }

    /**
     * 将传入的字符串进行URL编码，支持一个参数
     *
     * <p>参数说明：
     * <ol>
     *   <li>content: 待URL编码的字符串，非空</li>
     * </ol>
     * </p>
     *
     * <p>使用示例：
     * <pre>
     * ${url_encode("Hello World")}     // 返回 "Hello%20World"
     * ${url_encode("中文")}            // 返回 "%E4%B8%AD%E6%96%87"
     * </pre>
     * </p>
     *
     * @param context 上下文对象
     * @param args    参数列表，包含待编码的字符串
     * @return 编码后的字符串
     * @throws RuntimeException 当参数数量不正确或内容为空时抛出异常
     */
    @Override
    public String execute(ContextWrapper context, Args args) {
        checkMethodArgCount(args, 1, 1);
        String content = args.getFirstString();
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("content con not be null");
        }
        return URLEncoder.encode(content, StandardCharsets.UTF_8);
    }
}