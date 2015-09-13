package com.catapultlearning.walkthrough.model;

import java.sql.Timestamp;

public class WebUsers {

	private String email;
	private Long accountId;
	private String password;
	private Long userId;
	private String lastName;
	private String firstName;
	private Timestamp webRegistrationDate;
	private Boolean hasAccount;
	private Timestamp lastWebLogin;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Timestamp getWebRegistrationDate() {
		return webRegistrationDate;
	}
	public void setWebRegistrationDate(Timestamp webRegistrationDate) {
		this.webRegistrationDate = webRegistrationDate;
	}
	public Boolean getHasAccount() {
		return hasAccount;
	}
	public void setHasAccount(Boolean hasAccount) {
		this.hasAccount = hasAccount;
	}
	public Timestamp getLastWebLogin() {
		return lastWebLogin;
	}
	public void setLastWebLogin(Timestamp lastWebLogin) {
		this.lastWebLogin = lastWebLogin;
	}
}
