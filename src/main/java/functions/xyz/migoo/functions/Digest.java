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


package functions.xyz.migoo.functions;

import core.xyz.migoo.functions.AbstractFunction;
import core.xyz.migoo.functions.CompoundVariable;
import core.xyz.migoo.functions.FunctionsException;
import org.apache.commons.codec.binary.Hex;
import xyz.migoo.utils.StringUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaomi
 * @date 2019/11/18 22:16
 */
public class Digest extends AbstractFunction {

    @Override
    public Object execute(CompoundVariable parameters) throws FunctionsException {
        if (parameters.isEmpty()){
            throw new FunctionsException("parameters con not be null");
        }
        String algorithm = parameters.getString("algorithm").trim().isEmpty() ? "md5"
                : parameters.getString("algorithm").trim();
        String stringToEncode = parameters.getString("string");
        if (StringUtil.isEmpty(stringToEncode)) {
            throw new FunctionsException("string is null or empty");
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(stringToEncode.getBytes(StandardCharsets.UTF_8));
            String salt = parameters.getString("salt");
            if (!salt.isEmpty()){
                md.update(salt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] bytes = md.digest();
            return uppercase(new String(Hex.encodeHex(bytes)), parameters.getString("upper"));
        } catch (NoSuchAlgorithmException e) {
            throw new FunctionsException(e.getMessage(), e);
        }
    }

    private String uppercase(String encodedString, String shouldUpperCase) {
        return Boolean.TRUE.toString().equalsIgnoreCase(shouldUpperCase) ? encodedString.toUpperCase() : encodedString;
    }
}
