package com.spotify.utils;

import java.util.Properties;

/***
 * 1. the instance for the class is created only once, we can achieve this by creating constructor as private
 * 2. we use getinstance method when we need object for this class
 * 3. created non static property methods using it to get property value
 */
public class DataLoader {
    private static DataLoader configLoader;
    private Properties properties;

    private DataLoader(){
        properties = PropertyUtils.propertyLoader("src/test/resources/Data.properties");
    }

    public static DataLoader getInstance(){
        if(configLoader == null){
            configLoader = new DataLoader();
        }
        return configLoader;
    }

    public String getUserId(){
        String prop = properties.getProperty("getUserId");
        if(prop != null) return prop;
        else{
            throw new RuntimeException("the property value is not specified");
        }
    }

    public String updateUserId(){
        String prop = properties.getProperty("updatUserId");
        if(prop != null) return prop;
        else{
            throw new RuntimeException("the property value is not specified");
        }
    }

}
