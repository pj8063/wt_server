package com.catapultlearning.walkthrough.web.authentication.impl;

import javax.servlet.http.HttpServletRequest;

import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.web.authentication.AuthenticationValve;

public class WebAuthenticationPipe extends AuthenticationValve {
	private static final String POST_METHOD = "POST";
	private static final String PUT_METHOD = "PUT";
	
	@Override
	protected Boolean processInnerAuthentication(HttpServletRequest request) {
        if(isRealatePasswordRequest(request)){
    		//For web site authentication no need validate email link or create password or update password
    		//We just build deviceKey in Context
            ContextUtil.getContext().setAttribute(ObservationRequestAndResponseKeyConstants.DEVICE_KEY, getDeviceKey(request));   
        }else{
        	//Construct web authentication chain
        	
        }
		return Boolean.TRUE;
	}

	//Get Device key from request header or request parameter
    private String getDeviceKey(HttpServletRequest request){
    	String deviceKey = request.getHeader(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
    	if(null == deviceKey){
    		deviceKey = request.getParameter(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
    	}
    	return deviceKey;
    }
    
    //For Web site, register user and update password request no need validate.
    private Boolean isRealatePasswordRequest(HttpServletRequest request){
    	Boolean result = Boolean.FALSE;
    	String requestRUI = request.getRequestURI();
    	String requestMethod = request.getMethod();
        if(isWebEmailLinkRequest(requestRUI) || (isWebPasswordRequest(requestRUI) && (POST_METHOD.equals(requestMethod)|| PUT_METHOD.equals(requestMethod)))){
        	result = Boolean.TRUE;
        }else{
        	result = Boolean.FALSE;
        }
    	return result;
    }
}
