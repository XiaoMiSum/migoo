/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021.  Lorem XiaoMiSum (mi_xiao@qq.com)
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
import core.xyz.migoo.variables.MiGooVariables;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaomi
 * Created in 2021/10/19 10:11
 */
public class DigestTest {

    @Test
    public void testDigest1() {
        // 传入待签名的内容
        Args args = new Args(new MiGooVariables());
        args.add("112233");
        // 默认使用MD5
        Function function = new Digest();
        String result = function.execute(args).toString();
        // Digest 里面改变了 args的元素，112233 在第1这个位置了
        assert result.equals(digest("md5", args.getString(1), null, false));
    }

    @Test
    public void testDigest2() {
        // 传入 签名方式, 待签名的内容
        Args args = new Args(new MiGooVariables());
        args.add("md5");
        args.add("112233");
        Function function = new Digest();
        String result = function.execute(args).toString();
        assert result.equals(digest(args.getString(0), args.getString(1), null, false));
    }

    @Test
    public void testDigest3() {
        // 传入 签名方式, 待签名的内容, 盐
        Args args = new Args(new MiGooVariables());
        args.add("md5");
        args.add("112233");
        args.add("aabbcc");
        Function function = new Digest();
        String result = function.execute(args).toString();
        assert result.equals(digest(args.getString(0), args.getString(1), args.getString(2), false));
    }

    @Test
    public void testDigest4() {
        // 传入 签名方式, 待签名的内容, 盐, 结果转换为答谢
        Args args = new Args(new MiGooVariables());
        args.add("md5");
        args.add("112233");
        args.add("aabbcc");
        args.add(true);
        Function function = new Digest();
        String result = function.execute(args).toString();
        assert result.equals(digest(args.getString(0), args.getString(1), args.getString(2), true));
    }

    @Test
    public void testDigest5() {
        // 传入 签名方式, 待签名的内容, 盐, 结果转换为答谢
        Args args = new Args(new MiGooVariables());
        args.add("SHA-1");
        //    SHA-256
        args.add("112233");
        args.add("aabbcc");
        args.add(true);
        Function function = new Digest();
        String result = function.execute(args).toString();
        assert result.equals(digest(args.getString(0), args.getString(1), args.getString(2), true));
    }

    @Test
    public void testDigest6() {
        // 传入 签名方式, 待签名的内容, 盐, 结果转换为答谢
        Args args = new Args(new MiGooVariables());
        args.add("SHA-256");
        args.add("112233");
        args.add("aabbcc");
        args.add(true);
        Function function = new Digest();
        String result = function.execute(args).toString();
        assert result.equals(digest(args.getString(0), args.getString(1), args.getString(2), true));
    }

    @Test
    public void testDigest7() {
        Args args = new Args(new MiGooVariables());
        try {
            new Digest().execute(args);
        } catch (Exception e) {
            assert "parameters con not be null".equals(e.getMessage());
        }
    }

    @Test
    public void testDigest8() {
        Args args = new Args(new MiGooVariables());
        args.add("");
        try {
            new Digest().execute(args);
        } catch (Exception e) {
            assert "content is null or empty".equals(e.getMessage());
        }
    }

    private String digest(String algorithm, String content, String salt, boolean toUpdater) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(content.getBytes(StandardCharsets.UTF_8));
            if (StringUtils.isNotBlank(salt)) {
                md.update(salt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] bytes = md.digest();
            return toUpdater ? new String(Hex.encodeHex(bytes)).toUpperCase() : new String(Hex.encodeHex(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
