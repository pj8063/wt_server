package com.catapultlearning.walkthrough.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class JsonUtil {

    private JsonUtil(){
        
    }
    
    public static String convertObjectToJSON(Object object){
        String result = StringUtils.EMPTY;
        ObjectMapper objectMapper = new ObjectMapper();
        if(object!=null){
            try {
                result = objectMapper.writeValueAsString(object);
            } catch (JsonGenerationException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (JsonMappingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
