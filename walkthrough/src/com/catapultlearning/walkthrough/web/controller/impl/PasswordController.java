package com.catapultlearning.walkthrough.web.controller.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.RequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.ResourceBundleConstants;
import com.catapultlearning.walkthrough.constants.SystemResourceConstants;
import com.catapultlearning.walkthrough.exception.BusinessException;
import com.catapultlearning.walkthrough.exception.ParameterValidationException;
import com.catapultlearning.walkthrough.service.UserService;
import com.catapultlearning.walkthrough.util.ResourceBundleUtil;

@RequestMapping("/users")
@Controller
public class PasswordController {

	protected static Logger logger = Logger.getLogger(PasswordController.class);
	
    @Autowired
    private UserService userService;
    
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception exception) {
        exception.printStackTrace();
        logger.error(exception);
        String message = StringUtils.EMPTY;
        ModelAndView mav = new ModelAndView(RequestAndResponseKeyConstants.ERROR_PAGE);
        if(exception instanceof BusinessException){
        	BusinessException businessException = (BusinessException)exception;
        	message = businessException.getMessage();
        }else if(exception instanceof ParameterValidationException){
        	ParameterValidationException parameverValidationException = (ParameterValidationException)exception;
        	message = parameverValidationException.getMessage();
        }else{
        	message = ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_SYSTEM_ERROR_MESSAGE);
        }
        mav.addObject(RequestAndResponseKeyConstants.MESSAGE, message);
        return mav;
    }
    
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView initialCreatePasswordPage(@RequestParam Map<String, String> parameters) throws BusinessException, ParameterValidationException{
    	String apiKey = parameters.get(SystemResourceConstants.API_KEY);
    	String securityKey = parameters.get(RequestAndResponseKeyConstants.SECURITY_KEY);
    	String email = parameters.get(RequestAndResponseKeyConstants.EMAIL);
    	String clientType = parameters.get(SystemResourceConstants.CLIENT_TYPE);
    	String deviceKey = parameters.get(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
    	String time = parameters.get(RequestAndResponseKeyConstants.TIME);
    	String userId = parameters.get(RequestAndResponseKeyConstants.USER_ID);
    	userService.validateEmailLink(securityKey,email,userId,time,clientType,deviceKey);
    	ModelAndView mav = new ModelAndView();
    	mav.addObject(RequestAndResponseKeyConstants.EMAIL, email);
		mav.addObject(ObservationRequestAndResponseKeyConstants.DEVICE_KEY, deviceKey);
		mav.addObject(RequestAndResponseKeyConstants.SECURITY_KEY, securityKey);
		mav.addObject(SystemResourceConstants.API_KEY, apiKey);
		mav.addObject(SystemResourceConstants.CLIENT_TYPE, clientType);
		mav.addObject(RequestAndResponseKeyConstants.TIME, time);
		mav.addObject(RequestAndResponseKeyConstants.USER_ID, userId);
		mav.setViewName(RequestAndResponseKeyConstants.CREATE_PASSWORD_PAGE);
    	return mav;
    }
    
    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public ModelAndView initialUpdatePasswordPage(@RequestParam Map<String, String> parameters) throws BusinessException, ParameterValidationException{
    	String apiKey = parameters.get(SystemResourceConstants.API_KEY);
    	String securityKey = parameters.get(RequestAndResponseKeyConstants.SECURITY_KEY);
    	String email = parameters.get(RequestAndResponseKeyConstants.EMAIL);
    	String clientType = parameters.get(SystemResourceConstants.CLIENT_TYPE);
    	String deviceKey = parameters.get(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
    	String time = parameters.get(RequestAndResponseKeyConstants.TIME);
    	String userId = parameters.get(RequestAndResponseKeyConstants.USER_ID);
    	
    	userService.validateEmailLink(securityKey,email,userId,time,clientType,deviceKey);
    	
    	ModelAndView mav = new ModelAndView();
    	mav.addObject(RequestAndResponseKeyConstants.EMAIL, email);
		mav.addObject(ObservationRequestAndResponseKeyConstants.DEVICE_KEY, deviceKey);
		mav.addObject(RequestAndResponseKeyConstants.SECURITY_KEY, securityKey);
		mav.addObject(SystemResourceConstants.API_KEY, apiKey);
		mav.addObject(SystemResourceConstants.CLIENT_TYPE, clientType);
		mav.addObject(RequestAndResponseKeyConstants.USER_ID, userId);
		mav.addObject(RequestAndResponseKeyConstants.TIME, time);
		mav.setViewName(RequestAndResponseKeyConstants.FORGET_PASSWORD_PAGE);
    	return mav;
    	
    }

}
