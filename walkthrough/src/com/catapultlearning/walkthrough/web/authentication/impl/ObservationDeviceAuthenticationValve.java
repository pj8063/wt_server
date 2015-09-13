package com.catapultlearning.walkthrough.web.authentication.impl;

import javax.servlet.http.HttpServletRequest;

import com.catapultlearning.walkthrough.constants.MessageErrorCodeConstants;
import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.RequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.ResourceBundleConstants;
import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.util.ResourceBundleUtil;
import com.catapultlearning.walkthrough.web.Message;
import com.catapultlearning.walkthrough.web.Message.MessageEntry;
import com.catapultlearning.walkthrough.web.authentication.AuthenticationValve;

public class ObservationDeviceAuthenticationValve extends AuthenticationValve{

	@Override
	protected Boolean processInnerAuthentication(HttpServletRequest request) {
        Boolean result = Boolean.TRUE;
        //Step 1 Login and register request must validate Device Key
    	if(isNecessaryRequest(request)){
    		//Step 2 validate Device Key is empty
        	String deviceKey = this.getDeviceKey(request);
        	if(null == deviceKey || deviceKey.isEmpty()){
        		result = Boolean.FALSE;
                MessageEntry message = Message.error(MessageErrorCodeConstants.DEVICEKEY_NOT_SEND, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_DEVICEKEY_NOT_SEND));
                //Build friendly message which need send to observation client
                ContextUtil.getContext().setAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE, message);
        	}else{
        		//Step 3 validate device key pass, return TRUE
        		result = Boolean.TRUE;
        		//Build deviceKey in Context
                ContextUtil.getContext().setAttribute(ObservationRequestAndResponseKeyConstants.DEVICE_KEY, deviceKey);        		
        	}		
    	}else{
    		result = Boolean.TRUE;
    	}	
		return result;
	}

	//Get Device key from request header or request parameter
    private String getDeviceKey(HttpServletRequest request){
    	String deviceKey = request.getHeader(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
    	if(null == deviceKey){
    		deviceKey = request.getParameter(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
    	}
    	return deviceKey;
    }
    
    //For Mobile site, Login and register request must validate Device Key
    private Boolean isNecessaryRequest(HttpServletRequest request){
    	Boolean result = Boolean.FALSE;
    	String requestRUI = request.getRequestURI();
        if(isLoginRequest(requestRUI) || isMobileRegisterRequest(requestRUI)){
        	result = Boolean.TRUE;
        }else{
        	result = Boolean.FALSE;
        }
    	return result;
    }
    
}
