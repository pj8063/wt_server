package com.catapultlearning.walkthrough.util;

import com.catapultlearning.walkthrough.dto.MailSender;


public class MailSenderUtil {

	private static MailSender mailSender = null;
	
	public static MailSender getMailSender(String username,String password,String stmpHostName){
		if(null == mailSender){
			mailSender = new MailSender(username,password,stmpHostName);
		}
		return mailSender;
	}
}
