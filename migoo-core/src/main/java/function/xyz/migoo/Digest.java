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
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaomi
 */
public class Digest implements Function {


    /**
     * 获取信息摘要，通常为MD5，支持四个参数
     * 参数：
     * algorithm: 算法，允许为空，默认为md5
     * content: 待编码的原始内容，非空
     * salt：盐，允许为空
     * upper: 是否将结果转为大写，允许为空，默认 false
     */
    @Override
    public String execute(Args args) {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("parameters con not be null");
        }
        return args instanceof KwArgs kwArgs ? execute(kwArgs) : execute((LsArgs) args);
    }

    private String execute(KwArgs args) {
        var algorithm = args.getString("algorithm");
        var content = args.getString("content");
        var salt = args.getString("salt");
        var upper = args.getBooleanValue("upper");
        return hex(algorithm, content, salt, upper);
    }

    private String execute(LsArgs args) {
        if (args.size() == 1) {
            args.addFirst("md5");
        }
        var algorithm = args.getString(0).trim();
        var content = args.getString(1);
        var salt = args.getString(2);
        var upper = args.getBooleanValue(3);
        return hex(algorithm, content, salt, upper);
    }

    private String hex(String algorithm, String content, String salt, boolean upper) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("content is null or empty");
        }
        algorithm = StringUtils.isBlank(algorithm) ? "md5" : algorithm.trim();
        try {
            var md = MessageDigest.getInstance(algorithm);
            md.update(content.getBytes(StandardCharsets.UTF_8));
            if (StringUtils.isNotBlank(salt)) {
                md.update(salt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] bytes = md.digest();
            return upper ? new String(Hex.encodeHex(bytes)).toUpperCase() : new String(Hex.encodeHex(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}