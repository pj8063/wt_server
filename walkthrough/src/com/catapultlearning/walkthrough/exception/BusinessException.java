package com.catapultlearning.walkthrough.exception;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;
    
    private String message;
    
    private String code;
    
    public BusinessException(String message, String code){
        this.message = message;
        this.code = code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }

	public String getCode() {
		return code;
	}
}
