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

package function.xyz.migoo;

import core.xyz.migoo.function.Args;
import core.xyz.migoo.function.Function;
import core.xyz.migoo.function.KwArgs;
import core.xyz.migoo.function.LsArgs;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author xiaomi
 */
public class UrlDecode implements Function {

    /**
     * 将传入的字符串进行 url decode，支持一个参数
     * 参数：
     * content: 待url decode的字符串，非空
     */
    @Override
    public String execute(Args args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("content con not be null");
        }
        return execute(args instanceof KwArgs kwArgs ? execute(kwArgs.getString("content"))
                : execute(((LsArgs) args).getString(0)));
    }

    public String execute(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("content con not be null");
        }
        return URLDecoder.decode(content, StandardCharsets.UTF_8);
    }
}