package xyz.migoo.md5;

/**
 * @author Administrator
 * @date 2017/11/25
 */
public class Md5Signature {
    /**
     * MD5验证签名
     * @param content
     * @param sign
     * @param key
     * @return
     */
    public static boolean doCheck(String content, String sign, String key) {
        String md5 = MD5.MD5Encode(content + key);
        md5 = md5.toUpperCase();
        System.out.println("content:"+content);
        System.out.println("md5 sign:"+md5);
        if (md5.equals(sign)) {
            return true;
        } else{
            return false;
        }
    }

    public static String getSign(String content,String  key) {
        String s=MD5.MD5Encode(content+key);
        return  s.toUpperCase();
    }
}
