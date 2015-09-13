package com.catapultlearning.walkthrough.dao;

import com.catapultlearning.walkthrough.dao.base.BaseDAO;
import com.catapultlearning.walkthrough.model.WebUsers;

public interface WebUsersDAO extends BaseDAO<WebUsers, Long> {

	public WebUsers getByEmail(String email);

	public WebUsers getByUserId(Long userId);
		
	public WebUsers getWebUserByUserEmailAndPassWord(String email, String passWord);
	
}
