package xyz.migoo.utils.digest;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA签名验签类
 * @author xiaomi
 */
public class RsaCrypt {
	/**
	 * 签名算法
	 */
	public static final String NONE_WITH_RSA = "NONEwithRSA";
	public static final String MD5_WITH_RSA = "MD5withRSA";
	public static final String MD2_WITH_RSA = "MD2withRSA";
	public static final String SHA1_WITH_RSA = "SHA1withRSA";
	public static final String SHA256_WITH_RSA = "SHA256withRSA";
	public static final String SHA384_WITH_RSA = "SHA384withRSA";
	public static final String SHA512_WITH_RSA = "SHA512withRSA";
	public static final String SHA1_WITH_DSA = "SHA1withDSA";


	public static String sign(String content, String privateKey){
		return encryptByPrivateKey(content, privateKey);
	}

	public static boolean verify(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
			Signature signature = Signature.getInstance(NONE_WITH_RSA);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(StandardCharsets.UTF_8));
			return signature.verify(Base64.decodeBase64(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 私钥加密
	 * @param content      加密明文
	 * @param privateKey   私钥
	 * @return
	 */
	public static String encryptByPrivateKey(String content, String privateKey) {
		return encryptByPrivateKey(content, privateKey, NONE_WITH_RSA);
	}

	public static String encryptByPrivateKey(String content, String privateKey, String type) {
		try {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			Signature signature = Signature.getInstance(type);
			signature.initSign(priKey);
			signature.update(content.getBytes(StandardCharsets.UTF_8));
			byte[] signed = signature.sign();
			return Base64.encodeBase64String(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 公钥加密
	 * @param content      加密明文
	 * @param publicKey    公钥
	 * @return
	 */
	public static String encryptByPublicKey(String content, String publicKey) {
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] signed = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
			return Base64.encodeBase64String(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 私钥解密
	 * @param content     加密后的字符串
	 * @param privateKey  私钥
	 * @return
	 */
	public static String decryptByPrivateKey(String content, String privateKey) {
		try {
			// 取得私钥
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyFactory.generatePrivate(keySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			byte[] signed = cipher.doFinal(Base64.decodeBase64(content));
			return new String(signed, StandardCharsets.UTF_8);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 公钥解密
	 * @param content    加密后的字符串
	 * @param publicKey  公钥
	 * @return
	 */
	public static String decryptByPublicKey(String content, String publicKey) {
		try {
			// 取得私钥
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey pubKey = keyFactory.generatePublic(keySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, pubKey);
			byte[] signed = cipher.doFinal(Base64.decodeBase64(content));
			return new String(signed, StandardCharsets.UTF_8);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

}