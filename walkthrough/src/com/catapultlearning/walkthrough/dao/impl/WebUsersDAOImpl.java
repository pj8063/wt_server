package com.catapultlearning.walkthrough.dao.impl;

import java.util.HashMap;

import com.catapultlearning.walkthrough.constants.QueryParameterKeyConstants;
import com.catapultlearning.walkthrough.dao.WebUsersDAO;
import com.catapultlearning.walkthrough.dao.base.MyBatisDAO;
import com.catapultlearning.walkthrough.model.WebUsers;

public class WebUsersDAOImpl extends MyBatisDAO<WebUsers, Long> implements WebUsersDAO {

	@Override
	public WebUsers getByEmail(String email) {
		WebUsers webAccountsView = this.getSqlSession().selectOne(this.getSqlNameSpace() + "getWebAccountByEmail" ,email);
		return webAccountsView;
	}

	@Override
	public WebUsers getByUserId(Long userId) {
		WebUsers webAccountsView = this.getSqlSession().selectOne(this.getSqlNameSpace() + "getWebAccountByUserId" ,userId);
		return webAccountsView;
	}

	@Override
	public WebUsers getWebUserByUserEmailAndPassWord(String email, String passWord) {
	   HashMap<String, Object> parameters = new HashMap<String, Object>();
	   parameters.put(QueryParameterKeyConstants.EMAIL, email);
	   parameters.put(QueryParameterKeyConstants.PASSWORD, passWord);
	   WebUsers webUsers = (WebUsers) this.getSqlSession().selectOne(this.getSqlNameSpace()+ "getWebUserByUserEmailAndPassWord", parameters);
	    return webUsers;
	}
}
