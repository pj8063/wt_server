package com.catapultlearning.walkthrough.dto;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator{

	private String username;
	private String password;
	
	public MailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassworld() {
		return password;
	}

	public void setPassworld(String passworld) {
		this.password = passworld;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}
