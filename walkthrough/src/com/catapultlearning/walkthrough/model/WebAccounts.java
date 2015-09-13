package com.catapultlearning.walkthrough.model;

import java.sql.Timestamp;

public class WebAccounts {

	private Long webAccountId;
	private Long userId;
	private String password;
	private Timestamp createdDate;
	private Timestamp lastLogin;
	private Boolean isActive;
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Long getWebAccountId() {
		return webAccountId;
	}
	public void setWebAccountId(Long webAccountId) {
		this.webAccountId = webAccountId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}
}
