package com.catapultlearning.walkthrough.exception;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class ParameverValidationException extends Exception {

	private static final String KEY_VALUE_SEPARATOR = "==>";
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final long serialVersionUID = 1L;
    
    private HashMap<String, String> fields = new HashMap<String, String>();

    public HashMap<String, String> getFields() {
        return fields;
    }
    
    public void add(String field, String message){
        fields.put(field, message);
    }
    
    @Override
    public String getMessage(){
    	String message = super.getMessage();
    	if(!fields.isEmpty()){
    		message = StringUtils.EMPTY;
    		for(Entry<String, String> item : fields.entrySet()){
    			String key = item.getKey();
    			String value = item.getValue();
    			String field = key + KEY_VALUE_SEPARATOR + value + LINE_SEPARATOR;
    			message += field;
    		}
    	}
    	return message;
    }
}
