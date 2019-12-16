package xyz.migoo.functions;

import org.apache.commons.codec.binary.Base32;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaomi
 * @date 2019/11/18 17:22
 */
public class GoogleAuthCode extends AbstractFunction {

    @Override
    public String execute(CompoundVariable parameters) throws ExecuteError {
        if (parameters.isEmpty()){
            throw new ExecuteError("parameters con not be null");
        }
        String secretKey = parameters.getString("secretKey");
        if (secretKey == null){
            throw new ExecuteError("secretKey con not be null");
        }
        return GoogleAuthenticator.generateVerifyCode(parameters.getString("secretKey"));
    }

    public static class GoogleAuthenticator {

        private static final int LENGTH = 5;

        private static final int SCRATCH_CODE_LENGTH = 8;

        private static final int BYTES_PER_SCRATCH_CODE = 4;

        public static String generateVerifyCode(String secretKey) throws ExecuteError {
            long t = (System.currentTimeMillis() / 1000L) / 30L;
            byte[] decodedKey = new Base32().decode(secretKey);
            try {
                String code = String.valueOf(generateVerifyCode(decodedKey, t));
                return code.length() == LENGTH ? "0" + code : code;
            } catch (Exception e) {
                throw new ExecuteError("generateVerifyCode exception", e);
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
