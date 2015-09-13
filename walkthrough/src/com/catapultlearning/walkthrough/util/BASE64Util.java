package com.catapultlearning.walkthrough.util;

import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class BASE64Util {
    
    private static BASE64Encoder encoder = new BASE64Encoder();
    private static BASE64Decoder decoder = new BASE64Decoder();

    private BASE64Util(){
        
    }
    
    public static String encodeString(String content){
        String result = null;
        if(content!=null){
            byte[] byteContent = content.getBytes();
            result= encoder.encode(byteContent);
        }
        return result;
    }
    
    public static String decodeString(String content){
        String result = null;
        if(content!=null){
            try {
                byte[] byteContent = decoder.decodeBuffer(content);
                result = new String(byteContent);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
