package xyz.migoo.functions;

import org.apache.commons.codec.binary.Hex;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaomi
 * @date 2019/11/18 22:16
 */
public class Digest extends AbstractFunction {

    @Override
    public Object execute(CompoundVariable parameters) {
        String algorithm = parameters.getAsString("algorithm").trim().isEmpty() ? "md5"
                : parameters.getAsString("algorithm").trim();
        String stringToEncode = parameters.getAsString("string");
        String encodedString = "";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(stringToEncode.getBytes(StandardCharsets.UTF_8));
            String salt = parameters.getAsString("salt");
            if (!salt.isEmpty()){
                md.update(salt.getBytes(StandardCharsets.UTF_8));
            }
            byte[] bytes = md.digest();
            encodedString = uppercase(new String(Hex.encodeHex(bytes)), parameters.getAsString("upper"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    private String uppercase(String encodedString, String shouldUpperCase) {
        return Boolean.TRUE.toString().equalsIgnoreCase(shouldUpperCase) ? encodedString.toUpperCase() : encodedString;
    }
}