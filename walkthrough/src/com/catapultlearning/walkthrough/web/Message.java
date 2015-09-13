package com.catapultlearning.walkthrough.web;

import java.util.HashMap;

public class Message {

    private final static Integer OK = 200;
    private final static Integer ERROR = 500;
    
    public static abstract class MessageEntry {

        private Integer statusCode;

        public MessageEntry(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }
        
        public Integer getStatusCode() {
            return statusCode;
        }
    }
    
    private static class SuccessMessage extends MessageEntry {
        
        private Object data;
        
        public SuccessMessage(Integer statusCode, Object data) {
            super(statusCode);
            this.data = data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }
    }
    
    private static  class ErrorMessage extends MessageEntry {

        private String errorCode;
        
        private String message;
        
        public ErrorMessage(Integer statusCode, String errorCode, String message) {
            super(statusCode);
            this.errorCode = errorCode;
            this.message = message;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
    
    public static MessageEntry ok(Object data){
        if(data==null){
            data = new HashMap<String, String>(); 
        }
        MessageEntry messageEntry = new SuccessMessage(OK, data);
        return messageEntry;
    }
    
    public static MessageEntry error(String errorCode, String message){
        MessageEntry messageEntry = new ErrorMessage(ERROR, errorCode, message);
        return messageEntry;
    }
}
