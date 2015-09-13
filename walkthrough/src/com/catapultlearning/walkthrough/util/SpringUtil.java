package com.catapultlearning.walkthrough.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

public final class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName){
        T result = null;
        result = (T)applicationContext.getBean(beanName);
        return result;
    }
    
    public static void refreshContext(){
        if (applicationContext instanceof AbstractRefreshableApplicationContext){
            AbstractRefreshableApplicationContext refreshableApplicationContext = (AbstractRefreshableApplicationContext)applicationContext;
            refreshableApplicationContext.refresh();
        }
    }
}
