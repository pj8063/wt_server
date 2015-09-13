package com.catapultlearning.walkthrough.util;

import java.security.MessageDigest;

public final class MD5Util {

    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    private MD5Util(){
        
    }
    
    public static String getMD5EncodeString(String content){
        String result = null;
        if(content!=null){
            try{
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digest = md.digest(content.getBytes()); 
                int length = digest.length;
                char str[] = new char[length * 2];
                int k = 0;
                for (int i = 0; i < length; i++) {
                    byte temp = digest[i];
                    str[k++] = hexDigits[temp >>> 4 & 0xf];
                    str[k++] = hexDigits[temp & 0xf];
                }
                result = new String(str);
            }catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return result;
    }
    
    public static String getAESMD5EncodeString(String content){
        String result = null;
        if(content!=null){
        	result = AESUtil.aesEncrypt(content);
        	result = getMD5EncodeString(result);
        }
        return result;

    }
}
