package com.catapultlearning.walkthrough.web.authentication.impl;

import javax.servlet.http.HttpServletRequest;

import com.catapultlearning.walkthrough.web.authentication.AuthenticationValve;


public class ObservationAuthenticationPipe extends AuthenticationValve {

	@Override
	protected Boolean processInnerAuthentication(HttpServletRequest request) {
		//Need build the chain for the observation client authentication
        AuthenticationValve requestDeviceAuthenticationValve = new ObservationDeviceAuthenticationValve();
        AuthenticationValve tokenAuthenticationValve = new ObservationTokenAuthenticationValve();
        this.setNextValve(requestDeviceAuthenticationValve);
        requestDeviceAuthenticationValve.setNextValve(tokenAuthenticationValve);
		return Boolean.TRUE;
	}

}
