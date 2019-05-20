package xyz.migoo.utils.digest;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * @author xiaomi
 * @date 2018/8/17 16:08
 */
public class GoogleAuthenticator {

    private GoogleAuthenticator(){}

    /**
     * taken from Google pam docs - we probably don't need to mess with these
     */

    public static final int SECRET_SIZE = 10;

    public static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";

    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    private static final int LENGTH = 5;

    private static final int SCRATCH_CODE_LENGTH = 8;

    private static final int BYTES_PER_SCRATCH_CODE = 4;

    private final int maxWindowSize = 17;

    /**
     * default 3 - max 17 (from google docs)最多可偏移的时间
     */
    int windowSize = 3;

    public void setWindowSize(int s) {
        if (s >= 1 && s <= maxWindowSize) {
            windowSize = s;
        }
    }

    /**
     * enter the code shown on device. Edit this and run it fast before the code expires!
     * should give 5 * 30 seconds of grace...
     * @param codes
     * @param secretKey
     * @return
     */
    public static Boolean check(String codes, String secretKey) {
        long code = Long.parseLong(codes);
        long t = System.currentTimeMillis();
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(15);
        boolean r = ga.checkCode(secretKey, code, t);
        return r;
    }

    public static String generateAuthCode(String secretKey) {
        long t = (System.currentTimeMillis() / 1000L) / 30L;
        byte[] decodedKey = new Base32().decode(secretKey);
        try {
            String code = String.valueOf(verifyCode(decodedKey, t));
            if (code.length() == LENGTH){
                code = "0" + code;
            }
            return code;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String genSecretKey(String user, String host) {
        String secret = GoogleAuthenticator.generateSecretKey();
        GoogleAuthenticator.getQRBarcodeURL(user, host, secret);
        return secret;
    }

    public static String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String encodedKey = new String(bEncodedKey);
            return encodedKey;
        }catch (NoSuchAlgorithmException e) {
            // should never occur... configuration error
        }
        return null;
    }


    public static String getQRBarcodeURL(String user, String issuer, String secret) {
        String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s%%3Fsecret%%3D%s%%26issuer%%3D%s";
        return String.format(format, user, secret, issuer);
    }

    public boolean checkCode(String secretKey, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secretKey);
        // convert unix msec time into a 30 second "window"
        // this is per the TOTP spec (see the RFC for details)
        long t = (timeMsec / 1000L) / 30L;
        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        for (int i = -windowSize; i <= windowSize; ++i) {
            long hash;
            try {
                hash = verifyCode(decodedKey, t + i);
            }catch (Exception e) {
                // Yes, this is bad form - but
                // the exceptions thrown would be rare and a static configuration problem
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
                //return false;
            }
            if (hash == code) {
                return true;
            }
        }
        // The validation code is invalid.
        return false;
    }

    private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
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


