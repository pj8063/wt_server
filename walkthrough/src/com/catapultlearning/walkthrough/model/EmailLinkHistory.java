package com.catapultlearning.walkthrough.model;

import java.sql.Timestamp;

public class EmailLinkHistory {

	private Long emailLinkHistoryId;
	private Long userId;
	private String securityKey;
	private Timestamp createdTime;
	private Timestamp updatedTime;
	public Long getEmailLinkHistoryId() {
		return emailLinkHistoryId;
	}
	public void setEmailLinkHistoryId(Long emailLinkHistoryId) {
		this.emailLinkHistoryId = emailLinkHistoryId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSecurityKey() {
		return securityKey;
	}
	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
}
