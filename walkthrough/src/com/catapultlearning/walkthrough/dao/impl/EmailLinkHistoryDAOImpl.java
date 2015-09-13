package com.catapultlearning.walkthrough.dao.impl;

import com.catapultlearning.walkthrough.dao.EmailLinkHistoryDAO;
import com.catapultlearning.walkthrough.dao.base.MyBatisDAO;
import com.catapultlearning.walkthrough.model.EmailLinkHistory;

public class EmailLinkHistoryDAOImpl extends MyBatisDAO<EmailLinkHistory, Long> implements EmailLinkHistoryDAO {

	@Override
	public EmailLinkHistory getEmailLinkHistoryBySecurityKey(String securityKey) {
		EmailLinkHistory emailLinkHistory = this.getSqlSession().selectOne(this.getSqlNameSpace() + "getEmailLinkHistoryBySecurityKey" ,securityKey);
		return emailLinkHistory;
	}

	@Override
	public EmailLinkHistory getEmailLinkHistoryByUserId(Long userId) {
		EmailLinkHistory emailLinkHistory = this.getSqlSession().selectOne(this.getSqlNameSpace() + "getEmailLinkHistoryByUserId" ,userId);
		return emailLinkHistory;
	}

    
}
