package xyz.migoo.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA签名验签类
* @author xiaomi
 */
public class RSASignature {
	/**
	 * 签名算法
	 */
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * RSA签名
	 *
	 * @param content    待签名数据
	 * @param privateKey 商户私钥
	 * @param encode     字符集编码
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String encode) {
        return sign(content, Base64.decode(privateKey), encode);
	}

	public static String sign(String content, byte[] priBytes) {
		return sign(content, priBytes, "utf-8");
	}

	/**
	 * 密钥签名
	 *
	 * @param content
	 * @param privateKey
	 * @return
	 */
	public static String sign(String content, String privateKey) {
			return sign(content, Base64.decode(privateKey), "utf-8");
	}

    public static String sign(String content, byte[] priBytes, String encode) {
        try {
            PKCS8EncodedKeySpec privatePKCS8 = new PKCS8EncodedKeySpec(priBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyFactory.generatePrivate(privatePKCS8);
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(encode));
            byte[] signed = signature.sign();
            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * RSA验签名检查
	 *
	 * @param content   待签名数据
	 * @param sign      签名值
	 * @param publicKey 分配给开发商公钥
	 * @param encode    字符集编码
	 * @return 布尔值
	 */
	public static boolean doCheck(String content, String sign, String publicKey, String encode) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(encode));
			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * RSA验签名检查
	 *
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @return
	 */
	public static boolean doCheck(String content, String sign, String publicKey) {
		return doCheck(content, sign, publicKey, "utf-8");
	}

	public static boolean doCheck(String content, String sign, byte[] pubBytes) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = pubBytes;
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes());
			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}





    /**
     * 公钥加密过程
     *
     * @param publicKey
     *            公钥
     * @param plainTextData
     *            明文数据
     * @return
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        return encrypt(plainTextData, publicKey);
    }

    /**
     * 私钥加密过程
     *
     * @param privateKey
     *            私钥
     * @param plainTextData
     *            明文数据
     * @return
     * @throws Exception
     *             加密过程中的异常信息
     */

    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception {
        return encrypt(plainTextData, privateKey);
    }

    public static byte[] encrypt(byte[] plainTextData, Key key) throws Exception {
        if (key == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 私钥解密过程
     *
     * @param privateKey
     *            私钥
     * @param cipherData
     *            密文数据
     * @return 明文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        return decrypt(cipherData, privateKey);
    }

    /**
     * 公钥解密过程
     *
     * @param publicKey
     *            公钥
     * @param cipherData
     *            密文数据
     * @return 明文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
        return decrypt(cipherData, publicKey);
    }

    private static byte[]decrypt(byte[] cipherData, Key key) throws Exception {
        if (key == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        try {
            // 使用默认RSA
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            return null;
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }
}