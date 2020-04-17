package com.example.error;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Map<String, Object> errorAttributes =  super.getErrorAttributes(webRequest, includeStackTrace);

        Object timestamp = errorAttributes.get("timestamp");
        if(timestamp == null){
            errorAttributes.put("timestamp", formatter.format(new Date()));
        }
        else{
            errorAttributes.put("timestamp", formatter.format((Date) timestamp));
        }

        errorAttributes.put("version", "1.3");

        return errorAttributes;
    }
}
