package com.catapultlearning.walkthrough.web.authentication;

import javax.servlet.http.HttpServletRequest;

import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.util.ContextUtil;

public abstract class AuthenticationValve {
	private static final String RUI_LOGIN  				= "/login";
	private static final String RUI_REGISTER  			= "/register";
	private static final String RUI_PASSWORD			= "/password";
	private static final String RUI_USERS				= "/users";
    private AuthenticationValve nextValve;
    public void setNextValve(AuthenticationValve nextValve) {
        this.nextValve = nextValve;
    }
    
    public AuthenticationValve getNextValve() {
        return nextValve;
    }

    public Boolean doAuthentication() {
        HttpServletRequest request = ContextUtil.getContext().getWebContext().getRequest();
        Boolean result = Boolean.TRUE;
        result = this.processInnerAuthentication(request);
        if(result){
            AuthenticationValve nextValve = this.getNextValve();
            if(nextValve!=null){
                result = nextValve.doAuthentication();
            }
        }
        return result;
    }
    
    protected abstract Boolean processInnerAuthentication(HttpServletRequest request) ;
    
	//Check Login request
    protected Boolean isLoginRequest(String requestRUI){
    	Boolean result = Boolean.FALSE;
    	if(null != requestRUI && requestRUI.contains(RUI_LOGIN)){
    		result = Boolean.TRUE;
    	}
    	return result;
    }
    
    //Check Mobile Register and forget password request
    protected Boolean isMobileRegisterRequest(String requestRUI){
    	Boolean result = Boolean.FALSE;
    	if(null != requestRUI && (requestRUI.contains(RUI_REGISTER) || requestRUI.contains(RUI_PASSWORD))){
    		result = Boolean.TRUE;
    	}
    	return result;
    }
    
    //Check Web password request
    protected Boolean isWebEmailLinkRequest(String requestRUI){
    	Boolean result = Boolean.FALSE;
    	if(null != requestRUI && (requestRUI.lastIndexOf(RUI_REGISTER)>0) || requestRUI.lastIndexOf(RUI_PASSWORD)>0){
    		result = Boolean.TRUE;
    	}
    	return result;
    }
    
    //Check Web password request
    protected Boolean isWebPasswordRequest(String requestRUI){
    	Boolean result = Boolean.FALSE;
    	if(null != requestRUI && (requestRUI.lastIndexOf(RUI_USERS)>0)){
    		result = Boolean.TRUE;
    	}
    	return result;
    }
}
