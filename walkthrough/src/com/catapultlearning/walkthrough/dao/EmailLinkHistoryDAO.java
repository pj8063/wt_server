package com.catapultlearning.walkthrough.dao;

import com.catapultlearning.walkthrough.dao.base.BaseDAO;
import com.catapultlearning.walkthrough.model.EmailLinkHistory;

public interface EmailLinkHistoryDAO extends BaseDAO<EmailLinkHistory, Long> {

	EmailLinkHistory getEmailLinkHistoryBySecurityKey(String securityKey);

	EmailLinkHistory getEmailLinkHistoryByUserId(Long userId);

}
