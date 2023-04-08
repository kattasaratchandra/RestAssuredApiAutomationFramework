package com.spotify.api;

import com.spotify.pojo.Playlist;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.api.EndPoints.API;
import static com.spotify.api.EndPoints.TOKEN;
import static com.spotify.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response post(Object Playlist, String accessToken, String path){
        return given(getRequestSpec())
                    .header("Authorization", "Bearer " + accessToken)
                    .body(Playlist).
                when()
                    .post(path).
                then()
                    .spec(getResponseSpec())
                    .extract()
                    .response();
    }

    public static Response postAccount(HashMap<String, String> formParams){
        return  given()
                    .spec(getAccountRequestSpec())
                    .formParams(formParams).
                when()
                    .post(API + TOKEN).
                then()
                    .spec(getResponseSpec())
                    .extract()
                    .response();
    }

    public static Response get(String path, String accessToken){
        return given(getRequestSpec())
                    .header("Authorization", "Bearer " + accessToken).
                when()
                    .get(path).
                then()
                    .spec(getResponseSpec())
                    .extract()
                    .response();
    }

    public static Response update(Object playlist, String path, String accessToken){
        return given(getRequestSpec())
                    .header("Authorization", "Bearer " + accessToken)
                    .body(playlist).
                when().
                    put(path).
                then()
                    .spec(getResponseSpec())
                    .extract()
                    .response();
    }
}
