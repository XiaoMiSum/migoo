package xyz.migoo.functions;

import org.apache.commons.codec.binary.Hex;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;
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
    public Object execute(CompoundVariable parameters) throws ExecuteError {
        if (parameters.isEmpty()){
            throw new ExecuteError("parameters con not be null");
        }
        String algorithm = parameters.getString("algorithm").trim().isEmpty() ? "md5"
                : parameters.getString("algorithm").trim();
        String stringToEncode = parameters.getString("string");
        if (StringUtil.isEmpty(stringToEncode)) {
            throw new ExecuteError("string is null or empty");
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
            throw new ExecuteError(e.getMessage(), e);
        }
    }

    private String uppercase(String encodedString, String shouldUpperCase) {
        return Boolean.TRUE.toString().equalsIgnoreCase(shouldUpperCase) ? encodedString.toUpperCase() : encodedString;
    }
}
