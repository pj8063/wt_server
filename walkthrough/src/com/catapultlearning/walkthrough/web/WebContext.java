package com.catapultlearning.walkthrough.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class WebContext {

    private HttpServletRequest request;
    private HttpServletResponse response;
    
    public WebContext(HttpServletRequest request, HttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }    
}
