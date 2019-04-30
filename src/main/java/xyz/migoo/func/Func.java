package xyz.migoo.func;

import xyz.migoo.utils.authen.GoogleAuthenticator;
import xyz.migoo.utils.authen.RSASignature;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

/**
 * @author xiaomi
 * @date 2019-02-20 19:02
 */
public class Func {

    public static String generateGoogleAuthCode(String secretKey){
        return GoogleAuthenticator.generateAuthCode(secretKey);
    }

    public static String urlEncode(String value, String enc){
        try {
            return URLEncoder.encode(value, enc);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public static String dateFormat(String pattern, String time) {
        return DateUtil.format(pattern, Integer.parseInt(time));
    }

    public static String dateFormat(String pattern) {
        return DateUtil.format(pattern, System.currentTimeMillis());
    }

    public static String dateFormat() {
        return DateUtil.format(DateUtil.YYYY_MMDD_HHMMSS, System.currentTimeMillis());
    }

    public static String random(String length, String baseString, Object upperCase){
        if (Boolean.valueOf(upperCase.toString())){
            return StringUtil.random(Integer.valueOf(length), baseString).toUpperCase();
        }
        return StringUtil.random(Integer.valueOf(length), baseString);
    }

    public static String random(String length, Object upperCase){
        if (Boolean.valueOf(upperCase.toString())){
            return StringUtil.random(Integer.valueOf(length)).toUpperCase();
        }
        return StringUtil.random(Integer.valueOf(length));
    }

    public static String random(String length, String baseString){
        return StringUtil.random(Integer.valueOf(length), baseString);
    }

    public static String random(String length){
        return StringUtil.random(Integer.valueOf(length));
    }

    public static String add(String num1, String num2){
        return new BigDecimal(num1).add(new BigDecimal(num2)).toPlainString();
    }

    public static String subtract(String num1, String num2){
        return new BigDecimal(num1).subtract(new BigDecimal(num2)).toPlainString();
    }

    public static String divide(String num1, String num2){
        return new BigDecimal(num1).divide(new BigDecimal(num2)).toPlainString();
    }

    public static String divide(String num1, String num2, String scale, String roundingMode){
        return new BigDecimal(num1).divide(new BigDecimal(num2), Integer.parseInt(scale), Integer.parseInt(roundingMode)).toPlainString();
    }

    public static String multiply(String num1, String num2){
        return new BigDecimal(num1).multiply(new BigDecimal(num2)).toPlainString();
    }

    public static long timestamp(){
        return System.currentTimeMillis();
    }

    public static String encryptByPublicKey(String content, String publicKey){
        return RSASignature.encryptByPublicKey(StringUtil.toEmpty(content), publicKey);
    }

    public static String encryptByPrivateKey(String content, String privateKey){
        return RSASignature.encryptByPrivateKey(content, privateKey);
    }

    /** type 见  RSASignature类 **/
    public static String encryptByPrivateKey(String content, String privateKey, String type){
        return RSASignature.encryptByPrivateKey(content, privateKey, type);
    }

}
