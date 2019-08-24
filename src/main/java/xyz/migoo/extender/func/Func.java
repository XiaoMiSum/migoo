package xyz.migoo.extender.func;

import xyz.migoo.utils.digest.GoogleAuthenticator;
import xyz.migoo.utils.digest.Md5Crypt;
import xyz.migoo.utils.digest.RsaCrypt;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

/**
 * @author xiaomi
 * @date 2019-02-20 19:02
 */
public class Func {

    /**
     * 通过指定 secretKey 生成 google code
     * @param secretKey 安全码
     * @return google code
     */
    public static String googleCode(String secretKey){
        return GoogleAuthenticator.generateAuthCode(secretKey);
    }

    /**
     * url encode
     * @param url 需要 encode 的url
     * @return encode 后的 url
     * */
    public static String urlEncode(String url){
        return urlEncode(url, "utf-8");
    }
    /**
     * url encode
     * @param url 需要 encode 的url
     * @param enc 编码类型
     * @return encode 后的 url
     * */
    public static String urlEncode(String url, String enc){
        try {
            return URLEncoder.encode(url, enc);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    /**
     * url decode
     * @param url 需要 decode 的url
     * @return decode 后的 url
     * */
    public static String urlDecode(String url){
        return urlDecode(url, "utf-8");
    }
    /**
     * url decode
     * @param url 需要 decode 的url
     * @param enc 编码类型
     * @return decode 后的 url
     * */
    public static String urlDecode(String url, String enc){
        try {
            return URLDecoder.decode(url, enc);
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }

    /**
     * 日期格式化
     *
     * @param pattern  指定格式
     * @param later  N 秒之后
     * @return N 秒之后的格式化时间
     */
    public static String dateFormat(String pattern, String later) {
        return DateUtil.format(pattern, Integer.parseInt(later));
    }

    /**
     * 日期格式化
     *
     * @param pattern  指定格式
     * @return 当前的格式化时间
     */
    public static String dateFormat(String pattern) {
        return DateUtil.format(pattern, System.currentTimeMillis());
    }

    /**
     * 日期格式化 默认 格式 yyyyMMddHHmmss
     *
     * @return 当前的格式化时间
     */
    public static String dateFormat() {
        return DateUtil.format(DateUtil.YYYY_MMDD_HHMMSS);
    }

    /**
     * 生成指定长度的伪随机字符串
     *
     * @param length 指定长度
     * @param baseString 基础字符串
     * @param upperCase 是否转化为大写
     * @return 指定长度的伪随机字符串
     * */
    public static String random(String length, String baseString, Object upperCase){
        if (Boolean.parseBoolean(upperCase.toString())){
            return StringUtil.random(Integer.parseInt(length), baseString).toUpperCase();
        }
        return StringUtil.random(Integer.parseInt(length), baseString);
    }

    /**
     * 生成指定长度的伪随机字符串
     *
     * @param length 指定长度
     * @param baseString 基础字符串
     * @return 指定长度的伪随机字符串
     * */
    public static String random(String length, String baseString){
        return StringUtil.random(Integer.parseInt(length), baseString);
    }

    /**
     * 生成指定长度的伪随机字符串
     *
     * @param length 指定长度
     * @return 指定长度的伪随机字符串
     * */
    public static String random(String length){
        return StringUtil.random(Integer.parseInt(length));
    }

    /**
     * 生成 int类型的 0 - max 范围内的 伪随机数
     * @param max 指定最大范围
     * @return 返回 0 - max 范围内的 伪随机数
     * */
    public static Integer randomInt(int max){
        return new Random().nextInt(max);
    }

    /**
     * 生成 int类型的 0 - MAX_VALUE 范围内的 伪随机数
     * @return 返回 0 - MAX_VALUE 范围内的 伪随机数
     * */
    public static Integer randomInt(){
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    /**
     * 生成 long 类型的伪随机数
     * @return 返回 long 类型的伪随机数
     * */
    public static Long randomLong(){
        return new Random().nextLong();
    }

    /**
     * 生成 float 类型的伪随机数
     * @return 返回 float 类型的伪随机数
     * */
    public static Float randomFloat(){
        return new Random().nextFloat();
    }

    /**
     * 生成 double 类型的伪随机数
     * @return 返回 double 类型的伪随机数
     * */
    public static Double randomDouble(){
        return new Random().nextDouble();
    }

    /**
     * 计算 两数之和
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 两数之和
     * */
    public static String add(String num1, String num2){
        return new BigDecimal(num1).add(new BigDecimal(num2)).toPlainString();
    }

    /**
     * 计算 两数之差 num1 - num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 两数之差
     * */
    public static String subtract(String num1, String num2){
        return new BigDecimal(num1).subtract(new BigDecimal(num2)).toPlainString();
    }

    /**
     * 计算 num1 / num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return num1 / num 2
     * */
    public static String divide(String num1, String num2){
        return new BigDecimal(num1).divide(new BigDecimal(num2)).toPlainString();
    }

    /**
     * 计算 num1 / num2
     *
     * @param num1 数字1
     * @param num2 数字2
     * @param roundingMode 四舍五入模式
     * @return num1 / num 2
     * */
    public static String divide(String num1, String num2, String scale, String roundingMode){
        return new BigDecimal(num1).divide(new BigDecimal(num2), Integer.parseInt(scale), Integer.parseInt(roundingMode)).toPlainString();
    }

    /**
     * 计算 两数之积
     *
     * @param num1 数字1
     * @param num2 数字2
     * @return 两数之积
     * */
    public static String multiply(String num1, String num2){
        return new BigDecimal(num1).multiply(new BigDecimal(num2)).toPlainString();
    }

    /**
     * 获取当前毫秒时间
     * @return 当前毫秒时间
     */
    public static long timestamp(){
        return System.currentTimeMillis();
    }

    /**
     * rsa 签名
     * @param content 加密明文
     * @param publicKey  公钥字符串（公钥内容，不含换行符和 头尾描述）
     * @return 通过公钥生成的 签名
     */
    public static String encryptByPublicKey(String content, String publicKey){
        return RsaCrypt.encryptByPublicKey(StringUtil.toEmpty(content), publicKey);
    }

    /**
     * rsa 签名
     * @param content 加密明文
     * @param privateKey  私钥字符串（公钥内容，不含换行符和 头尾描述）
     * @return 通过私钥生成的 签名
     */
    public static String encryptByPrivateKey(String content, String privateKey){
        return RsaCrypt.encryptByPrivateKey(content, privateKey);
    }

    /**
     * rsa 签名
     * @param content 加密明文
     * @param privateKey  私钥字符串（公钥内容，不含换行符和 头尾描述）
     * @param type 见  RSASignature类
     * @return 通过私钥生成的 签名
     */
    public static String encryptByPrivateKey(String content, String privateKey, String type){
        return RsaCrypt.encryptByPrivateKey(content, privateKey, type);
    }

    /**
     * digest 加密
     *
     * @param content 加密明文
     * @param type 加密类型
     * @return 密文
     * */
    public static String digest(String content, String type){
        return Md5Crypt.encode(content, type);
    }

    /**
     * md5 加密
     *
     * @param content 加密明文
     * @return 密文
     * */
    public static String md5Digest(String content){
        return Md5Crypt.md5(content);
    }

    /**
     * sha 256 加密
     *
     * @param content 加密明文
     * @return 密文
     * */
    public static String sha256Digest(String content){
        return Md5Crypt.sha256(content);
    }

    /**
     * sha 512 加密
     *
     * @param content 加密明文
     * @return 密文
     * */
    public static String sha512Digest(String content){
        return Md5Crypt.sha512(content);
    }

}
