package com.catapultlearning.walkthrough.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public final class PropertiesUtil {

    private static Logger logger = Logger.getLogger(PropertiesUtil.class);
    private static Properties properties;
    private static final String propertiesFilePath = "/walkthrough.properties";
    
    private PropertiesUtil(){
        
    }
    
    public static void loadProperties(){
        try {
            clearProperties();
            properties = PropertiesLoaderUtils.loadAllProperties(propertiesFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
    
    public static String getPropertyValue(String propertyKey){
        String propertyValue = null;
        if(properties!=null){
            propertyValue = properties.getProperty(propertyKey);
        }
        return propertyValue;
    }
    
    public static void clearProperties(){
        if(properties!=null){
            properties.clear();
            properties = null;
        }
    }
}
