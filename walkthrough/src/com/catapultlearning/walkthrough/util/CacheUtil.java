package com.catapultlearning.walkthrough.util;

import java.util.HashMap;


public final class CacheUtil {
    
    private static HashMap<String, Object> innerMap = new HashMap<String, Object>();
    
    private CacheUtil(){
        
    }
    
    public static void putItem(String key, Object item){
        innerMap.put(key, item);
    }
    
    public static Object getItem(String key){
        Object result = innerMap.get(key);
        return result;
    }
    
    public static void removeItemByKey(String key){
        innerMap.remove(key);
    }
    
    public static void clear(){
        innerMap.clear();
    }
}
