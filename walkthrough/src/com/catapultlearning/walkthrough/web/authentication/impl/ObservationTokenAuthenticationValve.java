package com.catapultlearning.walkthrough.web.authentication.impl;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import com.catapultlearning.walkthrough.constants.BeanNameConstants;
import com.catapultlearning.walkthrough.constants.MessageErrorCodeConstants;
import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.RequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.ResourceBundleConstants;
import com.catapultlearning.walkthrough.model.MobileUsers;
import com.catapultlearning.walkthrough.service.MobileUsersService;
import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.util.ResourceBundleUtil;
import com.catapultlearning.walkthrough.util.SpringUtil;
import com.catapultlearning.walkthrough.web.Message;
import com.catapultlearning.walkthrough.web.Message.MessageEntry;
import com.catapultlearning.walkthrough.web.authentication.AuthenticationValve;

public class ObservationTokenAuthenticationValve extends AuthenticationValve {
	private MobileUsersService mobileUsersService;
    public ObservationTokenAuthenticationValve(){
    	mobileUsersService = SpringUtil.getBean(BeanNameConstants.MOBILEUSERS_SERVICE_BEAN_NAME);
    }

    @Override
    protected Boolean processInnerAuthentication(HttpServletRequest request){
        Boolean result = Boolean.TRUE;
        
        //Step 1 Login and register request no need user token.
        if(isUnnecessaryRequest(request)){
            result = Boolean.TRUE;
        }else{
            String userToken = this.getUserToken(request);
            if(null == userToken || userToken.isEmpty()){
            	//Step 2 validate userToken is empty
                result = Boolean.FALSE;
                MessageEntry message = Message.error(MessageErrorCodeConstants.TOKEN_NOT_SEND, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_TOKEN_NOT_SEND));
                //Build friendly message which need send to observation client
                ContextUtil.getContext().setAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE, message);
            }else{
            	//Step 3 validate userToken is existed.
            	MobileUsers mobileUsers = mobileUsersService.findMobileUsersByUserToken(userToken);
            	if(null == mobileUsers){
                	result = Boolean.FALSE;
                    MessageEntry message = Message.error(MessageErrorCodeConstants.TOKEN_NOT_EXIST, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_TOKEN_NOT_EXIST));
                    //Build friendly message which need send to observation client
                    ContextUtil.getContext().setAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE, message);
            	}else{
            		//Step 4 validate userToken is expired.
            		Timestamp tokenExpiration = mobileUsers.getTokenExpiration();
                    //Compare epoch time and current token expiration time, if not equals, the token is invalid.
            		if(null != tokenExpiration){
            			result = Boolean.FALSE;
                        MessageEntry message = Message.error(MessageErrorCodeConstants.TOKEN_IS_INVALID, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_TOKEN_IS_INVALID));
                        //Build friendly message which need send to observation client
                        ContextUtil.getContext().setAttribute(RequestAndResponseKeyConstants.AUTHENTICATION_VALIDATION_ERROR_MESSAGE, message);           		
            		}else{
            			result = Boolean.TRUE;
            			//Build userToken in Context
                        ContextUtil.getContext().setAttribute(ObservationRequestAndResponseKeyConstants.USER_TOKEN, userToken);        		
            		}
            	}
            }
        }
        return result;
    }
    
    //Get user token from request
    private String getUserToken(HttpServletRequest request){
    	String tokenValue = request.getHeader(ObservationRequestAndResponseKeyConstants.USER_TOKEN);
    	if(null == tokenValue){
    		tokenValue = request.getParameter(ObservationRequestAndResponseKeyConstants.USER_TOKEN);
    	}
    	return tokenValue;
    }

    //For Mobile site, Login and register request no need validate user token.
    private Boolean isUnnecessaryRequest(HttpServletRequest request){
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
