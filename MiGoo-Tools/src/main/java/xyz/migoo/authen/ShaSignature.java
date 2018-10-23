package xyz.migoo.authen;

import java.security.MessageDigest;

/**
 * @author Administrator
 * @date 2017/11/25
 */
public class ShaSignature {

    private ShaSignature(){}

    /**
     * sha-256编码
     *
     * @param origin 原始字符串
     * @return 经过sha-256加密之后的结果
     */
    public static String sha256(String origin) {
      return shaEncode(origin, "SHA-256");
    }

    /**
     * sha-512编码
     *
     * @param origin 原始字符串
     * @return 经过sha-256加密之后的结果
     */
    public static String sha512(String origin) {
        return shaEncode(origin, "SHA-512");
    }

    private static String shaEncode(String origin, String type) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            resultString = Utils.byteArrayToHexString(md.digest(origin.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
