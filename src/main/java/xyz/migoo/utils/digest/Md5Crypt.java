package xyz.migoo.utils.digest;

import java.security.MessageDigest;

/**
 * @author Administrator
 * @date 2017/11/25
 */
public class Md5Crypt {

    private Md5Crypt(){}

    public static String md5(String content) {
        return encode(content, "MD5");
    }

    /**
     * sha-256编码
     *
     * @param content 原始字符串
     * @return 经过sha-256加密之后的结果
     */
    public static String sha256(String content) {
        return encode(content, "SHA-256");
    }

    /**
     *
     * @param content 原始字符串
     * @return 经过加密之后的结果
     */
    public static String sha512(String content) {
        return encode(content, "SHA-512");
    }

    public static String encode(String content, String type) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance(type);
            resultString = byteArrayToHexString(md.digest(content.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    private final static String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    protected static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制格式
     */
    protected static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }
}
