package com.catapultlearning.walkthrough.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;

public final class ExceptionUtil {

	 private ExceptionUtil(){
	        
	 }
	 
	 public static String getExcetionStackTrace(Exception e){
		 String result = StringUtils.EMPTY;
		 if(e!=null){
			 StringWriter stringWriter = new StringWriter();
			 PrintWriter printWriter = new PrintWriter(stringWriter);
			 e.printStackTrace(printWriter);
			 result = stringWriter.toString();
		 }
		 return result;
	 }
}
