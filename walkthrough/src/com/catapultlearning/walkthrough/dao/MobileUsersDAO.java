package com.catapultlearning.walkthrough.dao;



import com.catapultlearning.walkthrough.dao.base.BaseDAO;
import com.catapultlearning.walkthrough.model.MobileUsers;

public interface MobileUsersDAO extends BaseDAO<MobileUsers, Long>{
	
	public MobileUsers getMobileUsersByEmail(String email);
		
	public MobileUsers getMobileUsersByEmailAndDeviceKey(String userName, String deviceId);
		
	public MobileUsers getMobileUsersByUserToken(String userToken);
	
	public MobileUsers updateMobileUserPassword(MobileUsers mobileUser);

}
