package com.catapultlearning.walkthrough.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.catapultlearning.walkthrough.constants.ResourceBundleConstants;


public final class ResourceBundleUtil {
    
    private static final String RESOURCE_PATH = "ApplicationResources";

    private ResourceBundleUtil(){
        
    }
    
    public static String getResourceText(String key){
        ResourceBundle resourceBundle = getResourceBundle();
        String text = StringUtils.EMPTY;
        if(resourceBundle.containsKey(key)){
            text = resourceBundle.getString(key);
        }
        return text;
    }
    
    public static String getResourceText(String key, Object[] parameters){
        ResourceBundle resourceBundle = getResourceBundle();
        String text = StringUtils.EMPTY;
        if(resourceBundle.containsKey(key)){
            text = resourceBundle.getString(key);
        }
        return MessageFormat.format(text, parameters);
    }
    
    private static ResourceBundle getResourceBundle() {
        String locale = null;
        //It is better to getter resource bundle from thread local object in current thread
        return ResourceBundle.getBundle(RESOURCE_PATH, getLocale(locale));
    }
    
    private static Locale getLocale(String locale){
        Locale targetLocale = null;
        // English
        if(ResourceBundleConstants.LOCALE_US.equalsIgnoreCase(locale)){
            targetLocale = Locale.US;
            //Can add other if else section to support other locale
        }else{
            //Default to English
            targetLocale = Locale.US;
        }       
        return targetLocale;
    }
}
