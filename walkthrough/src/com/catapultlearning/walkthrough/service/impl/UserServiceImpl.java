package com.catapultlearning.walkthrough.service.impl;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.catapultlearning.walkthrough.constants.MessageErrorCodeConstants;
import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.RequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.ResourceBundleConstants;
import com.catapultlearning.walkthrough.constants.SystemResourceConstants;
import com.catapultlearning.walkthrough.constants.WalkthroughPropertiesKeyConstants;
import com.catapultlearning.walkthrough.dao.EmailLinkHistoryDAO;
import com.catapultlearning.walkthrough.dao.MobileUsersDAO;
import com.catapultlearning.walkthrough.dao.WebAccountsDAO;
import com.catapultlearning.walkthrough.dao.WebUsersDAO;
import com.catapultlearning.walkthrough.dto.MailSender;
import com.catapultlearning.walkthrough.dto.observation.input.RegisterUserDTO;
import com.catapultlearning.walkthrough.exception.BusinessException;
import com.catapultlearning.walkthrough.exception.ParameterValidationException;
import com.catapultlearning.walkthrough.model.EmailLinkHistory;
import com.catapultlearning.walkthrough.model.MobileUsers;
import com.catapultlearning.walkthrough.model.WebAccounts;
import com.catapultlearning.walkthrough.model.WebUsers;
import com.catapultlearning.walkthrough.service.UserService;
import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.util.MD5Util;
import com.catapultlearning.walkthrough.util.MailSenderUtil;
import com.catapultlearning.walkthrough.util.PropertiesUtil;
import com.catapultlearning.walkthrough.util.ResourceBundleUtil;

public class UserServiceImpl implements UserService {
	private static final Long STANDARD_PASSWORD_LENGTH = Long.valueOf(8);
	private static final Long DEFAULT_LONG_VALUE = Long.valueOf(0);
    private MobileUsersDAO mobileUsersDAO;
    private WebUsersDAO webUsersDAO;
    private EmailLinkHistoryDAO emailLinkHistoryDAO;
    private WebAccountsDAO webAccountsDAO;
    
    public void setWebAccountsDAO(WebAccountsDAO webAccountsDAO) {
		this.webAccountsDAO = webAccountsDAO;
	}
    public void setMobileUsersDAO(MobileUsersDAO mobileUsersDAO) {
        this.mobileUsersDAO = mobileUsersDAO;
    }
	public void setWebUsersDAO(WebUsersDAO webUsersDAO) {
		this.webUsersDAO = webUsersDAO;
	}
    public void setEmailLinkHistoryDAO(EmailLinkHistoryDAO emailLinkHistoryDAO) {
		this.emailLinkHistoryDAO = emailLinkHistoryDAO;
	}
    
    @Override
	public void validateMobileUserEmailAndPassword(String email, String passWord) throws ParameterValidationException, BusinessException {    	
    	//Step1. Parameters validate
    	validateEmailAndPassworNonEmpty( email,  passWord);
		
		//Step2. Business validate email is registered
    	validateUserEmailRegistered(email);
    	
		//Step3 Business Validate user name and password.
    	validateUserEmailAndPasswordValid(email, passWord);
	}
    
    // Validate user email and password is empty, and throws parameter exception.
    private void validateEmailAndPassworNonEmpty(String email, String passWord) throws ParameterValidationException{
		ParameterValidationException parameverValidationException = new ParameterValidationException();
		if(null == email || email.isEmpty()){
    		parameverValidationException.add(RequestAndResponseKeyConstants.EMAIL, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS_EMPTY));    		
		}
		if(null == passWord || passWord.isEmpty()){
    		parameverValidationException.add(RequestAndResponseKeyConstants.PASSWORD, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_PASSWORD_IS_EMPTY));
		}
		if(null != parameverValidationException.getMessage() && !parameverValidationException.getMessage().isEmpty()){
			throw parameverValidationException;
		}
    }
    
    //Validate user email is registered, and throws Business exception.
    private void validateUserEmailRegistered(String email) throws BusinessException{
		MobileUsers mobileUsers = mobileUsersDAO.getMobileUsersByEmail(email);
		if(null == mobileUsers ){
	  		BusinessException businessException = new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_NOT_REGISTERED, new Object[]{email}), MessageErrorCodeConstants.EMAIL_NOT_REGISTERED);
    		throw businessException;
		}    	
    }
    
    //Validate user email and password is valid or invalid, and throws Business exception.
    private void validateUserEmailAndPasswordValid(String email, String passWord) throws BusinessException{
    	WebUsers webUser = webUsersDAO.getWebUserByUserEmailAndPassWord(email, passWord);
		if(null == webUser ){
	  		BusinessException businessException = new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_PASSWORD_INVALID), MessageErrorCodeConstants.EMAIL_PASSWORD_INVALID);
    		throw businessException;
		}
    }
    
    @Override
	public void sendRegisterUserEmailForMobile(String email,Boolean isForgetPassword) throws ParameterValidationException,BusinessException {
		String content = StringUtils.EMPTY;
		
		Long time = System.currentTimeMillis();
		String securityKey = getSecurityKey(email,time);
		String deviceKey = ContextUtil.getContext().getAttribute(ObservationRequestAndResponseKeyConstants.DEVICE_KEY);
		String path = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_HOST_PATH);
		// Validate email.
		Long userId = validateEmailForMobile(email,isForgetPassword);
		
		// Validate The last time send email is effective 
		createEmailLinkHistory(userId,isForgetPassword,time,securityKey);
		
		// Send email to set password.
		String username = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_EMAIL_USERNAME);
		String password = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_EMAIL_PASSWORD);
		String host = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_EMAIL_HOST);
		
		MailSender mailSender = MailSenderUtil.getMailSender(username,password,host);
		if(isForgetPassword){
			content = "<a href= "+path+"/users/password?apiKey="+SystemResourceConstants.WEB_API +"&securityKey=" +securityKey+ "&email="+ email +"&deviceKey="+ deviceKey + "&clientType=mobile" + "&time="+ time + " style='text-decoration:none'>"+ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_LINK_MESAGE)+"</a>";
		} else {
			content = "<a href= "+path+"/users/register?apiKey="+SystemResourceConstants.WEB_API +"&securityKey=" +securityKey+ "&email="+ email +"&deviceKey="+ deviceKey + "&clientType=mobile" + "&time="+ time + " style='text-decoration:none'>"+ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_LINK_MESAGE)+"</a>";
		}
		String subject = ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_SUBJECT);
		mailSender.send(email,subject, content);
	}

	private String getSecurityKey(String email,Long time){
		String securityKey = StringUtils.EMPTY;
		securityKey = MD5Util.getMD5EncodeString(email + time);
		return securityKey;
	}
	
	private Long validateEmailForMobile(String email, Boolean isForgetPassword) throws BusinessException, ParameterValidationException {
		Long userId = null;
		ParameterValidationException parameterException = new ParameterValidationException();
		// Step 1 : Validate email can't be empty.
		if(email == null || StringUtils.EMPTY.equals(email)){
			parameterException.add(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS__NOT_EMPTY), MessageErrorCodeConstants.EMAIL_INVALID);
		} 
		if(!parameterException.isEmpty()){
			throw parameterException;
		}
		MobileUsers mobileUsers = mobileUsersDAO.getMobileUsersByEmail(email);
		// Step 2 : Validate email must be exist in DB.
		if(null == mobileUsers){
			// Email is invalid!
			throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS_INVALID),MessageErrorCodeConstants.EMAIL_INVALID);
		} else {
			// Step 3 : Create password or reset password.
			if(isForgetPassword){
				// Forget password!
				Long accountId = mobileUsers.getAccountId();
				if(accountId == null){
					// Email is not register.
					throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_NOT_REGISTERED),MessageErrorCodeConstants.EMAIL_NOT_REGISTERED);
				} else {
					userId = mobileUsers.getUserId();
				}
			} else {
				// Create password!
				Long accountId = mobileUsers.getAccountId();
				if(accountId!= null){
					// Email is registered!
					throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS_REGISTERED),MessageErrorCodeConstants.EMAIL_REGISTERED);
				} else {
					userId = mobileUsers.getUserId();
				}
			}
		}
		
		return userId;
	}
    
	private EmailLinkHistory createEmailLinkHistory(Long userId,Boolean isForgetPassword,Long time,String securityKey){
		//  Get email link history by user id.
		EmailLinkHistory emailLinkHistory = emailLinkHistoryDAO.getEmailLinkHistoryByUserId(userId);
		if(null != emailLinkHistory){
			// if email link record found, and then update the security key by the last security. So our last click register is valid.
			emailLinkHistory.setSecurityKey(securityKey);
			emailLinkHistory.setCreatedTime(new Timestamp(time));
			emailLinkHistory.setUserId(userId);
			emailLinkHistory.setUpdatedTime(null);
			emailLinkHistory = emailLinkHistoryDAO.update(emailLinkHistory);
		} else {
			// if not email link history record in DB, then insert a record to DB. 
			emailLinkHistory = new EmailLinkHistory();
			emailLinkHistory.setSecurityKey(securityKey);
			emailLinkHistory.setCreatedTime(new Timestamp(time));
			emailLinkHistory.setUserId(userId);
			emailLinkHistory.setUpdatedTime(null);
			emailLinkHistory = emailLinkHistoryDAO.create(emailLinkHistory);
		}
		
		return emailLinkHistory;
	}
	
	private EmailLinkHistory updateEmailLinkHistory(Long userId){
		Long nowTime = new Date().getTime();
		EmailLinkHistory emailLinkHistory = emailLinkHistoryDAO.getEmailLinkHistoryByUserId(userId);
		emailLinkHistory.setUpdatedTime(new Timestamp(nowTime));
		emailLinkHistory.setUserId(userId);
		emailLinkHistory = emailLinkHistoryDAO.update(emailLinkHistory);
		
		return emailLinkHistory;
	}
	
	private void validateSecurityKey(String securityKey) throws BusinessException{
		Long nowTime = System.currentTimeMillis();
		EmailLinkHistory emailLinkHistory = emailLinkHistoryDAO.getEmailLinkHistoryBySecurityKey(securityKey);
		
		// Step 1 : Validate security key
		if(null == emailLinkHistory){
			throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_LINK_INVALID),MessageErrorCodeConstants.EMAIL_LINK_INVALID);
		}
		
		// Step 2 : Validate email link can use once.
		if(null != emailLinkHistory.getUpdatedTime()){
			throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_LINK_IS_USED),MessageErrorCodeConstants.EMAIL_LINK_IS_USED);
		}
		
		Long time = emailLinkHistory.getCreatedTime().getTime();
		String limitTime = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_EMAIL_TIMEOUT_DAYS);
		
		// Step 3 : Validate time out.
		if((nowTime - time) / (24 * 3600  * 1000) > Long.parseLong(limitTime)){
			throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_LINK_TIME_OUT), MessageErrorCodeConstants.EMAIL_LINK_TIME_OUT);
		}
		
	}
	
	@Override
	public MobileUsers createMobileUser(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException {
		MobileUsers mobileUsers = new MobileUsers();
		
		String securityKey = registerUserDTO.getSecurityKey();
		String email = registerUserDTO.getEmail();
		String deviceKey = registerUserDTO.getDeviceKey();
		String clientType = SystemResourceConstants.MOBILE;
		Long userId = DEFAULT_LONG_VALUE;
		Long time = registerUserDTO.getTime();
		
		validateEmailLink(securityKey,email,userId.toString(),time.toString(),clientType,deviceKey);
		validatePassword(registerUserDTO);
	
		String password = registerUserDTO.getPassword();
		
		mobileUsers.setEmail(email);
		mobileUsers.setPassWord(password);
		mobileUsers.setDeviceId(deviceKey);
		mobileUsers.setTokenExpiration(new Timestamp(0));
		mobileUsers.setUserToken(StringUtils.EMPTY);
		mobileUsers.setAppRegistrationDate(new Timestamp(time));
			
		mobileUsers = mobileUsersDAO.create(mobileUsers);
		
		mobileUsers = mobileUsersDAO.getMobileUsersByEmail(email);
		
		userId = mobileUsers.getUserId();
		// Update password history. 
		updateEmailLinkHistory(userId);
		
		return mobileUsers;
	}
	
	@Override
	public MobileUsers updateMobileUserPassword(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException {
		String securityKey = registerUserDTO.getSecurityKey();
		String email = registerUserDTO.getEmail();
		String deviceKey = registerUserDTO.getDeviceKey();
		String clientType = SystemResourceConstants.MOBILE;
		Long userId = DEFAULT_LONG_VALUE;
		Long time = registerUserDTO.getTime();
		
		validateEmailLink(securityKey,email,userId.toString(),time.toString(),clientType,deviceKey);
		
		validatePassword(registerUserDTO);
		
		String password = registerUserDTO.getPassword();
		
		MobileUsers mobileUser = mobileUsersDAO.getMobileUsersByEmail(email);
		mobileUser.setPassWord(password);
		mobileUser.setEmail(email);
		
		mobileUser = mobileUsersDAO.updateMobileUserPassword(mobileUser);
		
		userId = mobileUser.getUserId();
		updateEmailLinkHistory(userId);
		return mobileUser;
	}
	
	private void validatePassword(RegisterUserDTO registerUserDTO) throws ParameterValidationException{
		ParameterValidationException parameterException = new ParameterValidationException();
		String password = registerUserDTO.getPassword();
		String confirmPassword = registerUserDTO.getConfirmPassword();
		
	   // Validate passed and then validate password.
	    if(null == password || password.trim().length() == 0){
			parameterException.add(RequestAndResponseKeyConstants.PASSWORD, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_PASSWORD_IS_EMPTY));
	    }
	    
		if(!confirmPassword.equals(password)){
			parameterException.add(RequestAndResponseKeyConstants.PASSWORD, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_PASSWORD_IS_DIFFERENT));
		}
		
		if(password.trim().length() != STANDARD_PASSWORD_LENGTH){
			parameterException.add(RequestAndResponseKeyConstants.PASSWORD, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_PASSWORD_IS_INVALID));	
		}
			
		if(!parameterException.isEmpty()){
			throw parameterException;
		} 
	}

	@Override
	public void sendRegisterUserEmailForWeb(String email,Boolean isForgetPassword) throws ParameterValidationException,BusinessException {
		String content = StringUtils.EMPTY;
		
		Long time = System.currentTimeMillis();
		String securityKey = getSecurityKey(email,time);
		String path = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_HOST_PATH);
		
		// Validate email.
		Long userId = validateEmailForWeb(email,isForgetPassword);
		
		// Validate The last time send email is effective 
		createEmailLinkHistory(userId,isForgetPassword,time,securityKey);
		
		// Send email to set password.
		String username = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_EMAIL_USERNAME);
		String password = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_EMAIL_PASSWORD);
		String host = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_EMAIL_HOST);
		
		MailSender mailSender = MailSenderUtil.getMailSender(username,password,host);
		if(isForgetPassword){
			content = "<a href= "+path+"/users/password?apiKey="+SystemResourceConstants.WEB_API +"&securityKey=" +securityKey+ "&userId="+ userId + "&clientType=web" + " style='text-decoration:none'>"+ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_LINK_MESAGE)+"</a>";
		} else {
			content = "<a href= "+path+"/users/register?apiKey="+SystemResourceConstants.WEB_API +"&securityKey=" +securityKey+ "&userId="+ userId + "&clientType=web" + " style='text-decoration:none'>"+ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_LINK_MESAGE)+"</a>";
		}
		String subject = ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_SUBJECT);
		mailSender.send(email,subject, content);
	}

	private Long validateEmailForWeb(String email, Boolean isForgetPassword) throws BusinessException, ParameterValidationException {
		Long userId = null;
		ParameterValidationException parameterException = new ParameterValidationException();
		if(email == null || StringUtils.EMPTY.equals(email)){
			parameterException.add(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS__NOT_EMPTY), MessageErrorCodeConstants.EMAIL_INVALID);
		}
		// Step 1 : Validate email can't be empty.
		if(!parameterException.isEmpty()){
			throw parameterException;
		}
		WebUsers webAccount = webUsersDAO.getByEmail(email);
		// Step 2 : Validate email must be exist in DB.
		if(null == webAccount){
			// Email is invalid!
			throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS_INVALID),MessageErrorCodeConstants.EMAIL_INVALID);
		}else {
			// Step 3 : Create password or reset password.
			if(isForgetPassword){
				// Forget password!
				if(!webAccount.getHasAccount()){
					// Email is not register.
					throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_NOT_REGISTERED),MessageErrorCodeConstants.EMAIL_NOT_REGISTERED);
				} else {
					userId = webAccount.getUserId();
				}
			} else {
				// Create password!
				if(webAccount.getHasAccount()){
					// Email is registered!
					throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS_REGISTERED),MessageErrorCodeConstants.EMAIL_REGISTERED);
				} else {
					userId = webAccount.getUserId();
				}
			}
		}
		
		return userId;
	}

	

	@Override
	public WebAccounts createWebUser(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException {
		String securityKey = registerUserDTO.getSecurityKey();
		String email = registerUserDTO.getEmail();
		String deviceKey = registerUserDTO.getDeviceKey();
		String clientType = SystemResourceConstants.WEB;
		Long userId = registerUserDTO.getUserId();
		Long time = DEFAULT_LONG_VALUE;
		
		validateEmailLink(securityKey,email,userId.toString(),time.toString(),clientType,deviceKey);
		validatePassword(registerUserDTO);
	
		String password = registerUserDTO.getPassword();
		Long now = System.currentTimeMillis();
		
		WebAccounts webAccount = new WebAccounts();
		
		webAccount.setUserId(userId);
		webAccount.setPassword(password);
		webAccount.setLastLogin(new Timestamp(0));
		webAccount.setCreatedDate(new Timestamp(now));
		webAccount.setIsActive(Boolean.TRUE);
			
		webAccount = webAccountsDAO.create(webAccount);
		
		// Update password history. 
		updateEmailLinkHistory(userId);
		
		return webAccount;
	}

	@Override
	public WebAccounts updateWebUserPassword(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException {
		String securityKey = registerUserDTO.getSecurityKey();
		String email = registerUserDTO.getEmail();
		String deviceKey = registerUserDTO.getDeviceKey();
		String clientType = SystemResourceConstants.WEB;
		Long userId = registerUserDTO.getUserId();
		Long time = DEFAULT_LONG_VALUE;
		
		validateEmailLink(securityKey,email,userId.toString(),time.toString(),clientType,deviceKey);
		validatePassword(registerUserDTO);
		
		String password = registerUserDTO.getPassword();
		
		WebAccounts webAccount = new WebAccounts();
		webAccount.setUserId(userId);
		webAccount.setPassword(password);
		webAccount = webAccountsDAO.update(webAccount);
		
		// Update password history. 
		updateEmailLinkHistory(userId);
		
		return webAccount;
	}

	@Override
	public void validateEmailLink(String securityKey, String email,String userId,String time,String clientType,String deviceKey) throws ParameterValidationException, BusinessException {
		// Step 1 : validate client type,security key can't be empty
		ParameterValidationException parameterException = new ParameterValidationException();
		if(null == clientType || StringUtils.EMPTY.equals(clientType)){
			parameterException.add(SystemResourceConstants.CLIENT_TYPE,ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_CLIENT_TYPE_IS_EMPTY));
		}
		if(null == securityKey || StringUtils.EMPTY.equals(securityKey)){
			parameterException.add(RequestAndResponseKeyConstants.SECURITY_KEY,ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_SECURITY_KEY_IS_EMPTY));
		}
		
		// Step 2 : Validate client type is web or mobile.
		if(!SystemResourceConstants.MOBILE.equals(clientType) && !SystemResourceConstants.WEB.equals(clientType)){
			parameterException.add(SystemResourceConstants.CLIENT_TYPE,ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_CLIENT_TYPE_INVALID));
		}
		
		// Step 3 :if client type is mobile and then validate device key and email. if client type is web and then validate user id.
		if(SystemResourceConstants.MOBILE.equals(clientType)){
			if(null == email || StringUtils.EMPTY.equals(email)){
				parameterException.add(RequestAndResponseKeyConstants.EMAIL,ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS__NOT_EMPTY));
			}
			
			if(null == time || StringUtils.EMPTY.equals(time)){
				parameterException.add(RequestAndResponseKeyConstants.TIME,ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_TIME_IS__NOT_EMPTY));
			}
			
			if(null == deviceKey || StringUtils.EMPTY.equals(deviceKey)){
				parameterException.add(ObservationRequestAndResponseKeyConstants.DEVICE_KEY,ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_DEVICE_KEY_IS_EMPTY));
			}
		} else {
			if(null == userId || StringUtils.EMPTY.equals(userId)){
				parameterException.add(RequestAndResponseKeyConstants.USER_ID,ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS__NOT_EMPTY));
			}
		}
		
		if(!parameterException.isEmpty()){
			throw parameterException;
		}
		
		if(SystemResourceConstants.MOBILE.equals(clientType)){
			// Step 4: Validate email is valid for mobile.
			MobileUsers mobileUser = mobileUsersDAO.getMobileUsersByEmail(email);
			if(null == mobileUser){
				throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS_INVALID),MessageErrorCodeConstants.EMAIL_INVALID);
			} else {
				// Step 5 : Validate security key.
				validateSecurityKey(securityKey);
			}
		} else {
			// Step 4 : Validate user id is valid for web.
			WebUsers webUsers = webUsersDAO.getByUserId(Long.parseLong(userId));
			if(null == webUsers){
				throw new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_EMAIL_IS_INVALID),MessageErrorCodeConstants.EMAIL_INVALID);
			} else {
				// Step 5 : Validate security key.
				validateSecurityKey(securityKey);
			}
		}
	}

}
