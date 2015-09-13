package com.catapultlearning.walkthrough.web.controller.impl;

import java.util.HashMap;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.dto.observation.input.RegisterUserDTO;
import com.catapultlearning.walkthrough.exception.BusinessException;
import com.catapultlearning.walkthrough.exception.ParameterValidationException;
import com.catapultlearning.walkthrough.service.MobileUsersService;
import com.catapultlearning.walkthrough.service.UserService;
import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.web.Message;
import com.catapultlearning.walkthrough.web.Message.MessageEntry;
import com.catapultlearning.walkthrough.web.controller.BaseAPIController;

@RequestMapping("/users")
@Controller
public class UserController extends BaseAPIController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MobileUsersService mobileUsersService;

    /**
     * 
     * @api {post} /users/login User login
     * @apiName login
     * @apiGroup Users 
     * @apiVersion 0.1.0 
     * @apiDescription User login api for validate user name and password and create user token.
     *  
     * @apiParam   {Object} LoginUser Login User information including email and password.
     * @apiParam   {String} Loginuser.email email address 
     * @apiParam   {String} Loginuser.password  password 
     * @apiParamExample {json} Request-Example:
     * {
     * "userName":"loginuser@augmentum.com",
	 * "password": "123456"
	 * }
     * @apiSuccess {Object} package
     * @apiSuccess {String} package.statusCode Status code showing success 200 
     * @apiSuccess {Object} package.data 
     * @apiSuccess {String} package.data.tokenValue User token value 
     * @apiSuccessExample {json} Success-Response: 
	 * {
	 * "statusCode":200,
	 * "data":{
	 * 		  "tokenValue":"82320a81857e4a01af117f2ec9ba6b6f"
	 * 		  }
	 * }
     *   
     *  @apiError {Object} package
     *  @apiError {String} package.statusCode Code showing error was encountered 500 
     *  @apiError {String} package.errorCode Code used to identify category the following message belongs to 
     *  @apiError {String} package.message  Error message 
     *  @apiErrorExample {json} Error-Response: 
	 *	{
	 *		"statusCode":500,
	 *		"errorCode":"30001",
	 *		"message":"User name is not registered"
	 *	}
	 *	 	
     * */
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, headers="apiKey=observation")
    @ResponseBody
    public MessageEntry login(@RequestParam(value="email" ,required=false) String email,  @RequestParam(value="password" ,required=false) String passWord) throws ParameterValidationException, BusinessException,Exception{
    	//Step 1 Validate user email and password.
    	userService.validateMobileUserEmailAndPassword(email, passWord);

		//Step 2 Get deviceKey
    	String deviceKey = ContextUtil.getContext().getAttribute(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
		
		//Step 3 Create user token and update into "mobileaccounts" table
		String userToken = mobileUsersService.createUserTokenByUserEmailAndDeviceKey(email, deviceKey);
		
		//Step 4 Put userToken to message map object.
		HashMap<String,String> tokenMap = new HashMap<String,String>();
		tokenMap.put(ObservationRequestAndResponseKeyConstants.USER_TOKEN, userToken);
		MessageEntry message = Message.ok(tokenMap);
    	return message;
    	
    }
    
    /**
     * 
     * @throws BusinessException 
     * @throws ParameterValidationException 
     * @api {put} /users/logout User logout
     * @apiName logout
     * @apiGroup Users 
     * @apiVersion 0.1.0 
     * @apiDescription User logout api for log out system and expired user token.
     *  
     * @apiParam   {String} tokenValue user token value
     * @apiParamExample {json} Request-Example:
     * "tokenValue":"82320a81857e4a01af117f2ec9ba6b6f"
     * 
     * @apiSuccess {Object} package
     * @apiSuccess {String} package.statusCode Status code showing success 200 
     * @apiSuccess {Object} package.data 
     * @apiSuccess {String} package.data.tokenValue User token value 
     * @apiSuccessExample {json} Success-Response: 
	 * {
	 * "statusCode":200,
	 * "data":{
	 * 		  "tokenValue":""
	 * 		  }
	 * }
	 * 
     *  @apiError {Object} package
     *  @apiError {String} package.statusCode Code showing error was encountered 500 
     *  @apiError {String} package.errorCode Code used to identify category the following message belongs to 
     *  @apiError {String} package.message  Error message 
     *  @apiErrorExample {json} Error-Response: 
	 *	{
	 *		"statusCode":500,
	 *		"errorCode":"30003",
	 *		"message":"Token is not exist"
	 *	}
	 * 	
     * */
    @RequestMapping(value = "/logout", method = {RequestMethod.PUT}, headers="apiKey=observation")
    @ResponseBody
    public MessageEntry logout() throws ParameterValidationException, BusinessException{    	
		//Step1 Get user token
		String userToken = ContextUtil.getContext().getAttribute(ObservationRequestAndResponseKeyConstants.USER_TOKEN);
		
    	//Step2 Expired user token, set token Expiration is current time. 
		mobileUsersService.inValidUserToken(userToken);
		
    	//Step3 Return message: tokenValue is empty string.
    	HashMap<String,String> newMap = new HashMap<String,String>();
		MessageEntry message = Message.ok(newMap);
        return message;		
        
    }
    
    
    /**
     * @apiDefine success
     * @apiSuccess {String} statusCode The flag which represents success or fail.
     * @apiSuccess {Object} data The result data. 
     * 
     * @apiSuccessExample Success-Response:
     *  HTTP/1.1 200 OK
     *  {
     *   "statusCode":200,
     *   "data":{ "userId": "1"}
     *  }
     */
    
    /**
     * @apiDefine fail
     * @apiError APPkeyInvalid The APPkey is invalid.
     * @apiError EmailInvalid The email is invalid or illegal.
     * @apiErrorExample {json} Error-response:
     *  HTTP/1.1 200 OK
     *  {
     *   "message": "email",
     *   "errorCode": "EmailInvalid",
     *   "statusCode": 500
     *  } 
     */
    
    
    /**
     * @api {post} /users/register Send email to user.
     * @apiName sendEmail
     * @apiGroup User 
     * @apiVersion 0.1.0
     * @apiDescription This api is used to create user.
     * @apiParam {String} email Email of the user
     * @apiParam {String} APPkey APPkey of the user.
     * @apiParam {String} isReset isReset of the user.
     * @apiParamExample {json} Parameter Example:
     * {
     *  "email":"scottli@augmentum.com.cn",
     * }
     * 
     *@apiUse success
     *@apiUse fail  
     */
    
    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = {"apiKey=observation"})
    @ResponseBody
    public MessageEntry sendRegisterUserEmailForMobile(@RequestParam(value="email") String email) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.sendRegisterUserEmailForMobile(email,Boolean.FALSE);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST, headers = {"apiKey=web"})
    @ResponseBody
    public MessageEntry sendRegisterUserEmailForWeb(@RequestParam(value="email") String email) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.sendRegisterUserEmailForWeb(email,Boolean.FALSE);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
    
    
    /**
     * @api {post} /users Create a user.
     * @apiName createUser
     * @apiGroup User 
     * @apiVersion 0.1.0
     * @apiDescription This api is used to set password.
     * @apiParam {String} userId UserId of the user.
     * @apiParam {String} email Email of the user.
     * @apiParam {String} password Password of the user.
     * @apiParam {String} repeatingPassword Repeating password of the user.
     * @apiParam {String} key    Key of the client.
     * @apiParamExample {json} Parameter Example:
     * {
     *  "userId":"1",
     *  "email":"scottli@augmentum.com.cn",
     *  "password":"abc123_",
     *  "repeatingPassword":"abc123_",
     *  "key":"scottli"
     * }
     * 
     * @apiUse success
     * 
     * @apiError KeyInvalid The Key is invalid.
     * @apiError PasswordInvalid Two times password is different or password is invalid.
     * @apiErrorExample {json} Error-response:
     *  HTTP/1.1 200 OK
     *  {
     *   "message": "password",
     *   "errorCode": "PasswordInvalid",
     *   "statusCode": 500
     *  } 
     *  
     */
    
    @RequestMapping(method = RequestMethod.POST,headers = {"clientType=mobile"})
    @ResponseBody
    public MessageEntry createMobileUser(@RequestBody RegisterUserDTO registerUserDTO ) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.createMobileUser(registerUserDTO);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
    
    @RequestMapping(method = RequestMethod.POST,headers = {"clientType=web"})
    @ResponseBody
    public MessageEntry createWebUser(@RequestBody RegisterUserDTO registerUserDTO ) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.createWebUser(registerUserDTO);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
    
    @RequestMapping(method = RequestMethod.PUT,headers = {"clientType=mobile"})
    @ResponseBody
    public MessageEntry updateMobileUserPassword(@RequestBody RegisterUserDTO registerUserDTO ) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.updateMobileUserPassword(registerUserDTO);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
    
    @RequestMapping(method = RequestMethod.PUT,headers = {"clientType=web"})
    @ResponseBody
    public MessageEntry updateWebUserPassword(@RequestBody RegisterUserDTO registerUserDTO ) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.updateWebUserPassword(registerUserDTO);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
    
    @RequestMapping(value = "/password", method = RequestMethod.POST, headers = {"apiKey=observation"})
    @ResponseBody
    public MessageEntry sendForgetPasswordEmailForMobile(@RequestParam(value="email") String email) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.sendRegisterUserEmailForMobile(email,Boolean.TRUE);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
    
    @RequestMapping(value = "/password", method = RequestMethod.POST, headers = {"apiKey=web"})
    @ResponseBody
    public MessageEntry sendForgetPasswordEmailForWeb(@RequestParam(value="email") String email) throws BusinessException, ParameterValidationException, AddressException, MessagingException{
    	userService.sendRegisterUserEmailForWeb(email,Boolean.TRUE);
    	HashMap<String, String> data = new HashMap<String, String>();
    	MessageEntry message = Message.ok(data);
    	return message;
    }
}
