package com.catapultlearning.walkthrough.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.catapultlearning.walkthrough.constants.MessageErrorCodeConstants;
import com.catapultlearning.walkthrough.constants.ObservationRequestAndResponseKeyConstants;
import com.catapultlearning.walkthrough.constants.ResourceBundleConstants;
import com.catapultlearning.walkthrough.constants.WalkthroughPropertiesKeyConstants;
import com.catapultlearning.walkthrough.dao.MobileUsersDAO;
import com.catapultlearning.walkthrough.exception.BusinessException;
import com.catapultlearning.walkthrough.exception.ParameterValidationException;
import com.catapultlearning.walkthrough.model.MobileUsers;
import com.catapultlearning.walkthrough.service.MobileUsersService;
import com.catapultlearning.walkthrough.util.CacheUtil;
import com.catapultlearning.walkthrough.util.MD5Util;
import com.catapultlearning.walkthrough.util.PropertiesUtil;
import com.catapultlearning.walkthrough.util.ResourceBundleUtil;

public class MobileUsersServiceImpl implements MobileUsersService{
	private MobileUsersDAO mobileUsersDAO;
    public void setMobileUsersDAO(MobileUsersDAO mobileUsersDAO) {
        this.mobileUsersDAO = mobileUsersDAO;
    }
    
	public String createUserTokenByUserEmailAndDeviceKey(String email ,String deviceKey) {
		//Step1 Get current time format yyyy-MM-dd HH:mm:ss
		String dateFormat = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_TIME_FORMAT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String currentTimeStr = simpleDateFormat.format(System.currentTimeMillis());
        Timestamp currentTime = Timestamp.valueOf(currentTimeStr);
        
        //Step 2 MD5 encode user token
        String userToken = MD5Util.getAESMD5EncodeString(email+"&"+deviceKey+"&"+currentTimeStr);
		
		//Step 3 Create MobileAccounts
		MobileUsers mobileUsers = this.createOrUpdateUserToken(email, deviceKey, userToken, currentTime) ;
		
		//Step 4 Add token to Cache
		CacheUtil.putItem(userToken, mobileUsers); 
    	
    	return userToken;
	}

	//Create or update user token.
    public MobileUsers createOrUpdateUserToken(String email, String deviceId, String userToken, Timestamp lastAppLogin){
 		MobileUsers mobileUsers = mobileUsersDAO.getMobileUsersByEmailAndDeviceKey(email, deviceId);
 		if(null == mobileUsers){
 			//Create MobileUsers
 			mobileUsers = new MobileUsers();
 			mobileUsers.setEmail(email);
 			mobileUsers.setDeviceId(deviceId);
 			mobileUsers.setUserToken(userToken);
 			mobileUsers.setTokenExpiration(null);
 			mobileUsersDAO.create(mobileUsers);
 		}else{
 			//Update MobileUsers
 			mobileUsers.setLastAppLogin(lastAppLogin);
 			mobileUsers.setUserToken(userToken);
 			mobileUsers.setTokenExpiration(null);
 			mobileUsersDAO.update(mobileUsers);		
 		}
 		return mobileUsers;
     }

	//Update token expiration for expired user token
	public void inValidUserToken(String userToken)  throws ParameterValidationException, BusinessException {
		
		//Step 1. Parameters validate
		ParameterValidationException parameverValidationException = new ParameterValidationException();
		if(null == userToken || userToken.isEmpty()){
    		parameverValidationException.add(ObservationRequestAndResponseKeyConstants.USER_TOKEN, ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_TOKEN_IS_EMPTY));  
			throw parameverValidationException;
		}
		
		//Step 2. Business validate
 		MobileUsers mobileUsers = mobileUsersDAO.getMobileUsersByUserToken(userToken);
		if(null == mobileUsers ){
	  		BusinessException businessException = new BusinessException(ResourceBundleUtil.getResourceText(ResourceBundleConstants.WALKTHROUGH_TOKEN_NOT_EXIST), MessageErrorCodeConstants.USERTOKEN_NOT_EXIST);
    		throw businessException;
		}
		
   		//Step 3. Get current time format yyyy-MM-dd HH:mm:ss
		String dateFormat = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_TIME_FORMAT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String currentTime = simpleDateFormat.format(System.currentTimeMillis());
        Timestamp tokenExpiration = Timestamp.valueOf(currentTime);
		mobileUsers.setTokenExpiration(tokenExpiration);
		mobileUsersDAO.update(mobileUsers);	
		
		//Step 4. Clear token in cache
		CacheUtil.removeItemByKey(mobileUsers.getUserToken());
	}

	//Find mobile user by user token
	public MobileUsers findMobileUsersByUserToken(String userToken){
        //Get token from local cache
    	MobileUsers mobileUsers = (MobileUsers)CacheUtil.getItem(userToken);
        //If not find token from cache, then get token from database, and put it to local cache
        if(null == mobileUsers){
        	mobileUsers = mobileUsersDAO.getMobileUsersByUserToken(userToken);
            if(null != mobileUsers){
            	CacheUtil.putItem(userToken, mobileUsers);
            }
        }
        return mobileUsers;
	}

}
