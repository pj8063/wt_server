package com.catapultlearning.walkthrough.service.impl;

import com.catapultlearning.walkthrough.service.AuthenticationService;
import com.catapultlearning.walkthrough.web.authentication.AuthenticationValve;
import com.catapultlearning.walkthrough.web.authentication.impl.APIKeyAuthenticationValve;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public Boolean doAuthentication() {
        //Step1: Build the authentication valve pipe first
        AuthenticationValve clientAuthenticationValve = new APIKeyAuthenticationValve();
        Boolean result = clientAuthenticationValve.doAuthentication();
        return result;
    }

}
 