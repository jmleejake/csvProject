package com.km.adm.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class EncryptUtil {

	/**
	 * [단방향] 알고리즘 : MD5
	 */
	public static final String MD5 = "MD5";

	/**
	 * [단방향] 알고리즘 : SHA-1
	 */
	public static final String SHA1 = "SHA-1";

	/**
	 *  [단방향] 알고리즘 : SHA-256
	 */
	public static final String SHA256 = "SHA-256";

	/**
	 *  [단방향] 알고리즘 : SHA-384
	 */
	public static final String SHA384 = "SHA-384";

	/**
	 *  [단방향] 알고리즘 : SHA-512
	 */
	public static final String SHA512 = "SHA-512";

	/**
	 *  [양방향] 알고리즘 : AES/ECB/PKCS5Padding
	 */
	public static final String AES = "AES/ECB/PKCS5Padding";

	/**
	 * MD5 단방향 암호화(hash)처리
	 */
	public static String encodeMD5(String value){
		String encrypt=null;
		try {
			MessageDigest md = MessageDigest.getInstance(MD5);
			byte[] digest = md.digest(value.getBytes());
			encrypt = String.valueOf(Hex.encodeHex(digest));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encrypt;
	}

	/**
	 * SHA256 단방향 암호화(hash)처리
	 */
	public static String encodeSHA256(String value){
		String encrypt=null;
		try {
			MessageDigest md = MessageDigest.getInstance(SHA256);
			byte[] digest = md.digest(value.getBytes());
			encrypt = String.valueOf(Hex.encodeHex(digest));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypt;
	}

	/**
	 * SHA1 단방향 암호화(hash)처리
	 */
	public static String encodeSHA1(String value){
		String encrypt=null;
		try {
			MessageDigest md = MessageDigest.getInstance(SHA1);
			byte[] digest = md.digest(value.getBytes());
			encrypt = String.valueOf(Hex.encodeHex(digest));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encrypt;
	}

	/**
	 * AES 양방향 암호화 처리
	 */
	public static String encodeAES(String key, String value) {
		String encode = null;
		try {
			// 암호키 키생성
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
			// 암호 인스턴스 생성
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			// 데이터 암호화
			byte[] digest = cipher.doFinal(value.getBytes());
			// LowerCase = false
			encode = String.valueOf(Hex.encodeHex(digest, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encode;
	}

	/**
	 *  AES 양방향 복호화 처리
	 */
	public static String decodeAES(String key, String value) {
		String decode = null;
		try {
			SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			// 데이터 암호화
			byte[] original = cipher.doFinal(Hex.decodeHex(value.toCharArray()));
			String originalStr = new String(original);
			decode = originalStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decode;

	}
	
}
