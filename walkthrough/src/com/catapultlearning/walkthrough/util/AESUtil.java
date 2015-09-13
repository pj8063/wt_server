package com.catapultlearning.walkthrough.util;

import java.security.SecureRandom;

import com.catapultlearning.walkthrough.constants.WalkthroughPropertiesKeyConstants;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtil {
	
    private AESUtil(){ 
    }
    private static BASE64Encoder encoder    = new BASE64Encoder();
    private static BASE64Decoder decoder    = new BASE64Decoder();
    private static String CIPHER_ALGORITHM  = "AES";
    private static String AES_KEYS          = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_AES_KEYS); //"walkthrough2015aes08keys31";

    public static String base64Encode(byte[] bytes){
		String result = null;
		if(null != bytes){
			result = encoder.encode(bytes);
		}
		return result;
	}
	
	public static byte[] base64Decode(String base64Code) throws Exception{
		byte[] result = null; 
		if(!base64Code.isEmpty()){
			result = decoder.decodeBuffer(base64Code);
		}
		return result;
	}
	
	//AES Encrypt
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(CIPHER_ALGORITHM);
		kgen.init(128, new SecureRandom(encryptKey.getBytes()));
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(),CIPHER_ALGORITHM));
		return cipher.doFinal(content.getBytes());
	}
	
	public static String aesEncrypt(String content /*,String encryptKey*/ ){
		String result = null;
		try {
			result = base64Encode(aesEncryptToBytes(content, AES_KEYS));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//AES Decrypt
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance(CIPHER_ALGORITHM);
		kgen.init(128, new SecureRandom(decryptKey.getBytes()));
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(),CIPHER_ALGORITHM));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes);
	}
	
	public static String aesDecrypt(String encryptStr /*, String decryptKey*/ ) {
		String result = null;
		try {
			result = encryptStr.isEmpty() ? null : aesDecryptByBytes(base64Decode(encryptStr), AES_KEYS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
