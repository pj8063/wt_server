package com.catapultlearning.walkthrough.web.json;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.catapultlearning.walkthrough.constants.WalkthroughPropertiesKeyConstants;
import com.catapultlearning.walkthrough.util.PropertiesUtil;

public class CustomDateDeserializer extends JsonDeserializer<Date> {
    
    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String dateFormat = "yyyy-MM-dd";
        dateFormat = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_DATE_FORMAT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String content = jsonParser.getText();
        Date date = null;
        if(content!=null && !StringUtils.EMPTY.equals(content)){
            try {
                date = simpleDateFormat.parse(content);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new  RuntimeException(e);
            }
        }
        return date;
    }

}
