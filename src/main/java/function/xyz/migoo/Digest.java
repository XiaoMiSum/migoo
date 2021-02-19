/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2021. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package function.xyz.migoo;

import core.xyz.migoo.function.CompoundParameter;
import core.xyz.migoo.function.Function;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Digest implements Function {

    /**
     * 获取信息摘要，通常为MD5，支持四个参数
     * 参数：
     *      algorithm: 算法，允许为空，默认为md5
     *      content: 待编码的原始内容，非空
     *      salt：盐，允许为空
     *      upper: 是否将结果转为大写，允许为空，默认 false
     *
     */
    @Override
    public Object execute(CompoundParameter parameters) throws Exception {
        if (parameters.isEmpty()) {
            throw new Exception("parameters con not be null");
        }
        String algorithm = parameters.getString("algorithm").trim().isEmpty() ? "md5"
                : parameters.getString("algorithm").trim();
        String stringToEncode = parameters.isNullKey("content") ?
                parameters.getString("string") : parameters.getString("content");
        if (StringUtils.isEmpty(stringToEncode)) {
            throw new Exception("content is null or empty");
        }
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(stringToEncode.getBytes(StandardCharsets.UTF_8));
        String salt = parameters.getString("salt");
        if (!salt.isEmpty()) {
            md.update(salt.getBytes(StandardCharsets.UTF_8));
        }
        byte[] bytes = md.digest();
        return parameters.getBooleanValue("upper") ? new String(Hex.encodeHex(bytes)).toUpperCase()
                : new String(Hex.encodeHex(bytes));

    }
}