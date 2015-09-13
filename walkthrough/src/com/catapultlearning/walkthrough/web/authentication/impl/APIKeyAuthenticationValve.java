package com.catapultlearning.walkthrough.web.authentication.impl;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.catapultlearning.walkthrough.constants.MessageErrorCodeConstants;
import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.RequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.ResourceBundleConstants;
import com.catapultlearning.walkthrough.constants.SystemResourceConstants;
import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.util.ResourceBundleUtil;
import com.catapultlearning.walkthrough.util.SpringUtil;
import com.catapultlearning.walkthrough.web.Message;
import com.catapultlearning.walkthrough.web.Message.MessageEntry;
import com.catapultlearning.walkthrough.web.authentication.AuthenticationValve;

public class APIKeyAuthenticationValve extends AuthenticationValve {
	private static final String AUTHENTICATION_MAP   =   "authenticationMap";

    @Override
    protected Boolean processInnerAuthentication(HttpServletRequest request){
        Boolean result = Boolean.TRUE;

        //Step 1. Authenticate API key
        String apiKey = this.getAPIKey(request);
        if(null == apiKey || apiKey.isEmpty()){
        	result = Boolean.FALSE;
            MessageEntry message = Message.error(MessageErrorCodeConstants.APIKEY_NOT_SEND, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_APIKEY_NOT_SEND));
            ContextUtil.getContext().setAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE, message);
        }else{
        	//Step 2. Authenticate API key is invalid or valid.
        	HashMap<String, AuthenticationValve> authenticationServiceFactory = SpringUtil.getBean(AUTHENTICATION_MAP);
        	AuthenticationValve authenticationPipe = authenticationServiceFactory.get(apiKey);
        	if(null == authenticationPipe){
        		result = Boolean.FALSE;
        		MessageEntry message = Message.error(MessageErrorCodeConstants.APIKEY_IS_INVALID, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_APIKEY_IS_INVALID));
                ContextUtil.getContext().setAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE, message);
        	}else{
        		result = Boolean.TRUE;
        		//Build apiKey in Context
                ContextUtil.getContext().setAttribute(SystemResourceConstants.API_KEY, apiKey);      
        		//Step 3. Construct authentication chain
        		this.setNextValve(authenticationPipe);
        	}
        }   	
        
        return result;
    }
    
    //Get API key from request header or request parameter
    private String getAPIKey(HttpServletRequest request){
    	String apiKey = request.getHeader(SystemResourceConstants.API_KEY);
    	if(null == apiKey){
    		apiKey = request.getParameter(SystemResourceConstants.API_KEY);
    	}
    	return apiKey;
    }
    
}
