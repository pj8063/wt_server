package com.catapultlearning.walkthrough.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.catapultlearning.walkthrough.util.PropertiesUtil;


public class WebContextListener implements ServletContextListener {

    private static final String KEY_WEB_ROOT_REAL_PATH = "webapp.path";
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //Step1: initialize properties
        PropertiesUtil.loadProperties();
        //Step2: set current web root real path as system property
        setWebRootAsSystemProperty(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //Step1: clear properties
        PropertiesUtil.clearProperties();
    }
    
    private void setWebRootAsSystemProperty(ServletContext servletcnContext){
        String realPath = servletcnContext.getRealPath("/");
        System.setProperty(KEY_WEB_ROOT_REAL_PATH, realPath);
    }
}
