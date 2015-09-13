package com.catapultlearning.walkthrough.service;



import com.catapultlearning.walkthrough.dto.observation.input.RegisterUserDTO;
import com.catapultlearning.walkthrough.exception.BusinessException;
import com.catapultlearning.walkthrough.exception.ParameterValidationException;
import com.catapultlearning.walkthrough.model.MobileUsers;
import com.catapultlearning.walkthrough.model.WebAccounts;

public interface UserService {

	public void validateMobileUserEmailAndPassword(String username,String password) throws ParameterValidationException, BusinessException;

    public void sendRegisterUserEmailForMobile(String email,Boolean isForgetPassword)throws ParameterValidationException, BusinessException;
    
    public MobileUsers createMobileUser(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException;
    
    public void sendRegisterUserEmailForWeb(String email,Boolean isForgetPassword)throws ParameterValidationException, BusinessException;

	public WebAccounts createWebUser(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException;
	
	public MobileUsers updateMobileUserPassword(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException;
	
	public WebAccounts updateWebUserPassword(RegisterUserDTO registerUserDTO)throws BusinessException, ParameterValidationException;

	public void validateEmailLink(String securityKey, String email,String userId,String time,String clientType,String deviceKey)throws ParameterValidationException, BusinessException;

}
