package com.catapultlearning.walkthrough.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.catapultlearning.walkthrough.util.ContextUtil;
import com.catapultlearning.walkthrough.web.WebContext;

public abstract class BaseFilter implements Filter {

	private static final String STATIC_RESOURCE_FLAG = "/static"; 
	
    protected static Logger logger;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
      //Empty implementation here
        logger = Logger.getLogger(this.getClass());
    }
    

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest)servletRequest;
    	Boolean isStaticResurceRequest = this.isStaticRescorceRequest(request);
    	if(isStaticResurceRequest){
    		//Static resource allow pass the filter directly
    		filterChain.doFilter(servletRequest, servletResponse);
    	}else{
    		ContextUtil context = ContextUtil.getContext();
            try{
            	HttpServletResponse response = (HttpServletResponse)servletResponse;
                //Step 1: Bind attributes into current thread context
                bindAttributesToThreadContext(context, request, response);
                //Step 2: Do the process before filter, and decide whether allow to continue the filter 
                Boolean allowFilter = beforeFilter();
                if(allowFilter){
                  //Step 3: Process filter
                  filterChain.doFilter(servletRequest, servletResponse);
                }
                //Step 4: Do the process after filter
                afterFilter();
            }catch (Exception e) {
                processException(e);
            }finally{
                processFinally();
            }
    	}
    }
    
    
    protected Boolean beforeFilter(){
        return Boolean.TRUE;
    }
    
    protected void afterFilter(){
        //Empty implementation here
    }
    
    protected void processException(Exception e){
        e.printStackTrace();
        logger.error(e);
    }
    
    protected void processFinally(){
        ContextUtil context = ContextUtil.getContext();
        context.clearContext();
        context = null;
    }
    
    protected void bindAttributesContext(ContextUtil context, HttpServletRequest request, HttpServletResponse response){
        
    }
    
    private void bindAttributesToThreadContext(ContextUtil context, HttpServletRequest request, HttpServletResponse response){
        this.setWebContext(context, request, response);
        this.bindAttributesContext(context, request, response);
    }
    
    protected void setWebContext(ContextUtil context, HttpServletRequest request, HttpServletResponse response){
        if(context!=null){
            WebContext webContext = new WebContext(request, response);
            context.setAttribute(ContextUtil.WEB_CONTEXT, webContext);
        }
    }
    
    private Boolean isStaticRescorceRequest(HttpServletRequest request){
    	Boolean result = Boolean.FALSE;
    	String requestRUI = request.getRequestURI();
    	if(requestRUI!=null && requestRUI.contains(STATIC_RESOURCE_FLAG)){
    		result = Boolean.TRUE;
    	}
    	return result;
    }
    
    @Override
    public void destroy() {
      //Empty implementation here
    }
}
