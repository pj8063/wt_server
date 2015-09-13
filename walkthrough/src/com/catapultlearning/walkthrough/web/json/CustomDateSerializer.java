package com.catapultlearning.walkthrough.web.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import com.catapultlearning.walkthrough.constants.WalkthroughPropertiesKeyConstants;
import com.catapultlearning.walkthrough.util.PropertiesUtil;

public class CustomDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date dateValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        String dateFormat = "yyyy-MM-dd";
        dateFormat = PropertiesUtil.getPropertyValue(WalkthroughPropertiesKeyConstants.WALKTHROUGH_DATE_FORMAT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String content = simpleDateFormat.format(dateValue);
        jsonGenerator.writeString(content);
    }
}
