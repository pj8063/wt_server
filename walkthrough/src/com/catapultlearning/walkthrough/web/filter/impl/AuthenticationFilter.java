package com.catapultlearning.walkthrough.web.filter.impl;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.catapultlearning.walkthrough.constants.BeanNameConstants;
import com.catapultlearning.walkthrough.constants.MessageErrorCodeConstants;
import com.catapultlearning.walkthrough.constants.RequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.service.AuthenticationService;
import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.util.ExceptionUtil;
import com.catapultlearning.walkthrough.util.JsonUtil;
import com.catapultlearning.walkthrough.util.SpringUtil;
import com.catapultlearning.walkthrough.web.Message;
import com.catapultlearning.walkthrough.web.Message.MessageEntry;
import com.catapultlearning.walkthrough.web.filter.BaseFilter;

public class AuthenticationFilter extends BaseFilter {

	 private AuthenticationService authenticationService;
	
	 @Override
	 public void init(FilterConfig filterConfig) throws ServletException {
		 super.init(filterConfig);
	 }
	 
	 @Override
	 protected Boolean beforeFilter(){
		 if(authenticationService==null){
			 authenticationService = SpringUtil.getBean(BeanNameConstants.AUTHENTICATION_SERVICE_BEAN_NAME);
		 }
		 Boolean allowFilter = authenticationService.doAuthentication();
		 if(!allowFilter){
			//Authentication pipe line failed, and send authentication failed message to client
			MessageEntry message = null;
			try{
				message = ContextUtil.getContext().getAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE);
				String jsonString = JsonUtil.convertObjectToJSON(message);
				HttpServletResponse response = ContextUtil.getContext().getWebContext().getResponse();
				response.getWriter().write(jsonString);
			}catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}finally{
				ContextUtil.getContext().removeAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE);
			}
		 }
		 return allowFilter;
	 }
	 
	@Override
    protected void processException(Exception exception){
        /**
         * This exception should occur just in filter method, such as beforeFilter(), afterFilter().
         * Should not in spring MVC method, due to in spring MVC controller, the exception is all 
         * handled in BaseControler.
         */ 
        super.processException(exception);
        String exceptionStackTrace = ExceptionUtil.getExcetionStackTrace(exception);
        MessageEntry messageEntory = Message.error(MessageErrorCodeConstants.INTER_SERVER_ERROR, exceptionStackTrace);
        String jsonString = JsonUtil.convertObjectToJSON(messageEntory);
        HttpServletResponse response = ContextUtil.getContext().getWebContext().getResponse();
        try {
            response.getWriter().write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
            //The exception which is not throw must be logged
            AuthenticationFilter.logger.error(e);
        }
    }
}
