

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
import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaomi
 */
public class GoogleAuthCode implements Function {

    /**
     * 获取谷歌验证码，支持一个参数，且不允许为空
     * 参数顺序：
     * secret: 谷歌验证器安全码，非空，通过该安全码生成谷歌验证码
     */
    @Override
    public String execute(Args args) {
        if (args.isEmpty() || args.getString(0).isEmpty()) {
            throw new IllegalArgumentException("secretKey con not be null");
        }
        return GoogleAuthenticator.generateVerifyCode(args.getString(0));
    }

    public static class GoogleAuthenticator {

        private static final int LENGTH = 5;

        private static final int SCRATCH_CODE_LENGTH = 8;

        private static final int BYTES_PER_SCRATCH_CODE = 4;

        public static String generateVerifyCode(String secretKey) {
            long t = (System.currentTimeMillis() / 1000L) / 30L;
            byte[] decodedKey = new Base32().decode(secretKey);
            try {
                String code = String.valueOf(generateVerifyCode(decodedKey, t));
                return code.length() == LENGTH ? "0" + code : code;
            } catch (Exception e) {
                throw new RuntimeException("generateVerifyCode exception", e);
            }
        }

        private static int generateVerifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
            byte[] data = new byte[8];
            long value = t;
            for (int i = SCRATCH_CODE_LENGTH; i-- > 0; value >>>= SCRATCH_CODE_LENGTH) {
                data[i] = (byte) value;
            }
            SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signKey);
            byte[] hash = mac.doFinal(data);
            int offset = hash[20 - 1] & 0xF;
            // We're using a long because Java hasn't got unsigned int.
            long truncatedHash = 0;
            for (int i = 0; i < BYTES_PER_SCRATCH_CODE; ++i) {
                truncatedHash <<= 8;
                // We are dealing with signed bytes:
                // we just keep the first byte.
                truncatedHash |= (hash[offset + i] & 0xFF);
            }
            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= 1000000;
            return (int) truncatedHash;
        }
    }
}
