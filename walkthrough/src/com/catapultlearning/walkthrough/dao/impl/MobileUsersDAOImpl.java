package com.catapultlearning.walkthrough.dao.impl;

import java.util.HashMap;

import com.catapultlearning.walkthrough.constants.QueryParameterKeyConstants;
import com.catapultlearning.walkthrough.dao.MobileUsersDAO;
import com.catapultlearning.walkthrough.dao.base.MyBatisDAO;
import com.catapultlearning.walkthrough.model.MobileUsers;

public class MobileUsersDAOImpl extends MyBatisDAO<MobileUsers, Long> implements MobileUsersDAO{

	public MobileUsers getMobileUsersByEmail(String email) {
        MobileUsers mobileUsers = (MobileUsers)this.getSqlSession().selectOne(this.getSqlNameSpace()+ "getMobileUsersByEmail", email);
        return mobileUsers;
	}

	public MobileUsers getMobileUsersByEmailAndDeviceKey(String email, String deviceKey) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(QueryParameterKeyConstants.EMAIL, email);
        parameters.put(QueryParameterKeyConstants.DEVICE_KEY, deviceKey);
        MobileUsers mobileUsers = (MobileUsers) this.getSqlSession().selectOne(this.getSqlNameSpace()+ "getMobileUsersByEmailAndDeviceKey", parameters);
		return mobileUsers;
	}

	public MobileUsers getMobileUsersByUserToken(String userToken) {
        MobileUsers mobileUsers = (MobileUsers)this.getSqlSession().selectOne(this.getSqlNameSpace()+ "getMobileUsersByUserToken", userToken);
        return mobileUsers;
	}

	@Override
	public MobileUsers updateMobileUserPassword(MobileUsers mobileUser) {
		this.getSqlSession().update(this.getSqlNameSpace() + "updateMobileUserPassword", mobileUser);
		return mobileUser;
	}

}
