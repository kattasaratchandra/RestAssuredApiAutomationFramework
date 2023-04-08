package com.spotify.api;

import com.spotify.utils.ConfigLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TokenManager {
    static String accessToken;
    static Instant expiryTime;

    public static String  getToken(){
        try {
            if(accessToken == null || Instant.now().isAfter(expiryTime)){
                System.out.println("Renewing Token");
                Response response = renewToken();
                accessToken = response.path("access_token");
                int tokenExpireInSeconds = response.path("expires_in");
                expiryTime = Instant.now().plusSeconds(tokenExpireInSeconds-300);
            }else {
                System.out.println("Token is good to use");
            }
        }
        catch (Exception e){
            throw new RuntimeException("renew token failed");
        }
         return accessToken;
    }


    public static Response renewToken(){
        HashMap<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());
        formParams.put("client_id", ConfigLoader.getInstance().getClientId());
        formParams.put("client_secret", ConfigLoader.getInstance().getClientSecret());

        Response response = RestResource.postAccount(formParams);

        if(response.statusCode() != 200){
            throw new RuntimeException("Abort Renew Token Failed");
        }else {
            return response;
        }

    }
}
