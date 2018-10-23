package xyz.migoo.authen;

import java.security.MessageDigest;

/**
 * @author Administrator
 * @date 2017/11/25
 */
public class Md5Signature {

    private Md5Signature(){}

    /**
     * MD5验证签名
     * @param content    签名明文
     * @param sign       签名密文
     * @param key        签名密钥
     * @return boolean   验证结果
     */
    public static boolean check(String content, String sign, String key) {
        String md5 = md5Encode(content + key).toUpperCase();
        return md5.equals(sign);
    }

    public static String sign(String content,String  key) {
        return md5Encode(content+key).toUpperCase();
    }


    /**
     * MD5编码
     *
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    private static String md5Encode(String origin) {
        String resultString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = Utils.byteArrayToHexString(md.digest(origin.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
