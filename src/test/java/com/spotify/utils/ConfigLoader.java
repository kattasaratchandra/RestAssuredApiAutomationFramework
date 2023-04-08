package com.spotify.utils;

import java.util.Properties;

/***
 * 1. the instance for the class is created only once, we can achieve this by creating constructor as private
 * 2. we use getinstance method when we need object for this class
 * 3. created non static property methods using it to get property value
 */
public class ConfigLoader {
    private static ConfigLoader configLoader;
    private Properties properties;

    private ConfigLoader(){
        properties = PropertyUtils.propertyLoader("src/test/resources/Config.properties");
    }

    public static ConfigLoader getInstance(){
        if(configLoader == null){
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getClientId(){
        String prop = properties.getProperty("client_id");
        if(prop != null) return prop;
        else{
            throw new RuntimeException("the property value is not specified");
        }
    }

    public String getGrantType(){
        String prop = properties.getProperty("grant_type");
        if(prop != null) return prop;
        else{
            throw new RuntimeException("the property value is not specified");
        }
    }

    public String getRefreshToken(){
        String prop = properties.getProperty("refresh_token");
        if(prop != null) return prop;
        else{
            throw new RuntimeException("the property value is not specified");
        }
    }

    public String getClientSecret(){
        String prop = properties.getProperty("client_secret");
        if(prop != null) return prop;
        else{
            throw new RuntimeException("the property value is not specified");
        }
    }

    public String getUserId(){
        String prop = properties.getProperty("user_id");
        if(prop != null) return prop;
        else{
            throw new RuntimeException("the property value is not specified");
        }
    }
}
