package com.catapultlearning.walkthrough.model;

import java.sql.Timestamp;

public class MobileUsers {
	private Long 				userId; 
	private String 			email;
	private String 			lastName;
	private String 			firstName;
	private String 			passWord;
	private Long 				accountId;
	private String 			deviceId;
	private String 			userToken;
	private Boolean 			isTokenExpired;
	private Timestamp 	tokenExpiration;
	private Timestamp 	lastAppSync;
	private Timestamp 	lastAppLogin;
	private Timestamp 	appRegistrationDate;
	private Boolean 			hasAccount;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public Boolean getIsTokenExpired() {
		return isTokenExpired;
	}
	public void setIsTokenExpired(Boolean isTokenExpired) {
		this.isTokenExpired = isTokenExpired;
	}
	public Timestamp getTokenExpiration() {
		return tokenExpiration;
	}
	public void setTokenExpiration(Timestamp tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}
	public Timestamp getLastAppSync() {
		return lastAppSync;
	}
	public void setLastAppSync(Timestamp lastAppSync) {
		this.lastAppSync = lastAppSync;
	}
	public Timestamp getLastAppLogin() {
		return lastAppLogin;
	}
	public void setLastAppLogin(Timestamp lastAppLogin) {
		this.lastAppLogin = lastAppLogin;
	}
	public Timestamp getAppRegistrationDate() {
		return appRegistrationDate;
	}
	public void setAppRegistrationDate(Timestamp appRegistrationDate) {
		this.appRegistrationDate = appRegistrationDate;
	}
	public Boolean getHasAccount() {
		return hasAccount;
	}
	public void setHasAccount(Boolean hasAccount) {
		this.hasAccount = hasAccount;
	}

}
