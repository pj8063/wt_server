package com.catapultlearning.walkthrough.util;

import java.util.HashMap;

import com.catapultlearning.walkthrough.web.WebContext;


public final class ContextUtil {
    
    public static final String WEB_CONTEXT = "webContext";

    private static ThreadLocal<ContextUtil> threadLocal = new ThreadLocal<ContextUtil>();
    private HashMap<String, Object> innerMap;
    
    private ContextUtil(){
        innerMap = new HashMap<String, Object>();
    }
    
    public static ContextUtil getContext(){
        ContextUtil context = threadLocal.get();
        if(context==null){
            context = new ContextUtil();
            threadLocal.set(context);
        }
        return context;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key){
        T target = null;
        if(innerMap!=null && innerMap.containsKey(key)){
            target = (T)innerMap.get(key);
        }
        return target;
    }
    
    public void setAttribute(String key, Object value){
        if(innerMap!=null){
            innerMap.put(key, value);
        }
    }
    
    public void removeAttribute(String key){
        if(innerMap!=null){
            if(innerMap.containsKey(key)){
                innerMap.remove(key);
            }
        }
    }
    
    public void clearContext(){
        ContextUtil context = threadLocal.get();
        if(context!=null){
            threadLocal.remove();
            innerMap.clear();
            context = null;
        }
    }
    
    public WebContext getWebContext(){
        WebContext webContext = this.getAttribute(WEB_CONTEXT);
        return webContext;
    }
}
