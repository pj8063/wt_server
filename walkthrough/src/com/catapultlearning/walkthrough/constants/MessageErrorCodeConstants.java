package com.catapultlearning.walkthrough.constants;

public final class MessageErrorCodeConstants {

    //Start section for common error code
    public static final String INTER_SERVER_ERROR 						= "00001";
    public static final String APIKEY_NOT_SEND 							= "00002";
    public static final String APIKEY_IS_INVALID 						= "00003";
    //End section for common error code
    
    //Parameter Exception code
    public static final String PARAMETER_EXCEPTION						= "00009";
    //End Parameter Exception code
    
    //Start section for observation error code
    public static final String DEVICEKEY_NOT_SEND 						= "10001";
    public static final String DEVICEKEY_IS_INVALID 					= "10002";
    public static final String TOKEN_NOT_SEND 							= "10003";
    public static final String TOKEN_NOT_FIND 							= "10004";
    public static final String TOKEN_TIME_OUT 							= "10005";
    public static final String TOKEN_NOT_EXIST 							= "10006";
    public static final String TOKEN_IS_INVALID 						= "10007";
    public static final String TOKEN_NOT_GENERATE						= "10008";
    //End section for observation error code
    
    
    //Start section for web error code

    //End section for web error code
    
    
    //Business Exception code
    public static final String EMAIL_NOT_REGISTERED						= "30001";
    public static final String EMAIL_PASSWORD_INVALID					= "30002";
    public static final String USERTOKEN_NOT_EXIST						= "30003";
    public static final String EMAIL_INVALID							= "30004";
    public static final String EMAIL_REGISTERED							= "30005";
    public static final String EMAIL_LINK_INVALID						= "30006";
    public static final String EMAIL_LINK_TIME_OUT						= "30007";
    public static final String EMAIL_LINK_IS_USED						= "30008";
    //End Business Exception code
}
