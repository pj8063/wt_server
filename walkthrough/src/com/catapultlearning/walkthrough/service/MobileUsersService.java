package com.catapultlearning.walkthrough.service;

import java.sql.Timestamp;

import com.catapultlearning.walkthrough.exception.BusinessException;
import com.catapultlearning.walkthrough.exception.ParameterValidationException;
import com.catapultlearning.walkthrough.model.MobileUsers;

public interface MobileUsersService {
	
	public String createUserTokenByUserEmailAndDeviceKey(String email ,String deviceKey);
	
	public MobileUsers createOrUpdateUserToken(String email, String deviceId, String userToken, Timestamp lastAppLogin);
	
	public void inValidUserToken(String userToken) throws ParameterValidationException, BusinessException ;
	
	public MobileUsers findMobileUsersByUserToken(String userToken);

}
