package com.catapultlearning.walkthrough.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catapultlearning.walkthrough.constants.MessageErrorCodeConstants;
import com.catapultlearning.walkthrough.exception.BusinessException;
import com.catapultlearning.walkthrough.exception.ParameterValidationException;
import com.catapultlearning.walkthrough.util.ExceptionUtil;
import com.catapultlearning.walkthrough.web.Message;
import com.catapultlearning.walkthrough.web.Message.MessageEntry;


public abstract class BaseAPIController {

    protected static Logger logger = Logger.getLogger(BaseAPIController.class);
    
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public MessageEntry handleException(HttpServletResponse response, Exception exception){
        //Step1: Log the exception which is not throw
        logger.error(exception);
        //Step2: Build the exception error message
        MessageEntry messageEntory = null;
        if(exception instanceof BusinessException){
        	BusinessException businessException = (BusinessException)exception;
        	messageEntory = Message.error(businessException.getCode(), businessException.getMessage());
        }else if(exception instanceof ParameterValidationException){
        	ParameterValidationException parameterValidationException = (ParameterValidationException)exception;
        	messageEntory = Message.error(MessageErrorCodeConstants.PARAMETER_EXCEPTION, parameterValidationException.getMessage());
        }else{
        	/**
             * This exception should occur just spring MVC API controller, and this method will handle all none defined
             * exception from the spring MVC API controller
             */
            String exceptionStackTrace = ExceptionUtil.getExcetionStackTrace(exception);
            messageEntory = Message.error(MessageErrorCodeConstants.INTER_SERVER_ERROR, exceptionStackTrace);
        }
    	return messageEntory;
    }
}
