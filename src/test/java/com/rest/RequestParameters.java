package com.rest;

import io.restassured.config.EncoderConfig;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class RequestParameters {

    /*
    1. query parameters are appended to the url there is certain format for query parameters. will
    get appended after question mark and for multiple parameters in between uses &
     */
    @Test
    public void singleQueryParameters(){
        given()
                .baseUri("https://postman-echo.com")
                // we can use param or queryparam methods
                .param("foo1","bar1")
                .log().all().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    /* we can send multiple query params using two ways
    1. using queryparam() method multiple times
    2. using hashmap and sending as argument to query param as it is overloaded function
     */

    @Test
    public void multipleQueryParameters(){
        HashMap<String, String> map = new HashMap<>();
        map.put("foo1", "bar1");
        map.put("foo2", "bar2");
        given()
                .baseUri("https://postman-echo.com")
//                .param("foo1","bar1")
//                .param("foo2", "bar2")
                .queryParams(map)
                .log().all().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }


    // we can send multi value parameters as it is. as shown below
    @Test
    public void multipleValueQueryParameter(){
        HashMap<String, String> map = new HashMap<>();
        given()
                .baseUri("https://postman-echo.com")
                .param("foo1", "bar1;bar2;bar3")
                .log().all().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }
    /* difference between path and query
    1. query parameter is use as a filter. For e.g. if the response can have a lot of pages, then
    you can query for a specific page number using the query parameter. Of course,if the dev has
    got it implemented.
    2. Path parameter on the other hand can be used for fetching a specific resource.
    For e.g. From the given workspaces, I want to fetch a specific workspace with it's ID.
    I can use it as a path parameter. Of course, if the developer has got it implemented.
     */

    /* 1.path parameter we use for specific information like using id, name
       2. whenever we want to use any value in key value pair in get/post/put we use {key} like this
    to get value
     */
    @Test
    public void validatePathParameters(){
            HashMap<String, String> map = new HashMap<>();
            given()
                    .baseUri("https://reqres.in")
                    .pathParam("userId", 2)
                    .log().all().
            when()
                    .get("/api/users/{userId}").
            then()
                    .log().all()
                    .assertThat()
                    .statusCode(200);
    }

    /*
    1. the non-alphanumeric of key value pair are usually encoded in url
    2. we use form param to send in encoded format
    3. in body we select x-www-form-urlencoded and send as key value pair
    4. we get internal server error if the content type doesnt match so we say rest
    assured not to send default content type using config
     */
    /*
    These are different Form content types defined by W3C. If you want to send simple text/ ASCII data,
    then x-www-form-urlencoded will work. This is the default.But if you have to send non-ASCII
    text or large binary data, the form-data is for that.
    You can use Raw if you want to send plain text or JSON or any other kind of string. Like the
    name suggests, Postman sends your raw string data as it is without modifications.
    The type of data that you are sending can be set by using the content-type header from the drop down.
    Binary can be used when you want to attach non-textual data to the request,
    e.g. a video/audio file, images, or any other binary data file.
     */
    @Test
    public void formUrlEncoded(){
        HashMap<String, String> map = new HashMap<>();
        given()
                .baseUri("https://postman-echo.com")
                .config(config.encoderConfig(EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .formParam("key1", "value1")
                .log().all().
        when()
                .post("/post").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }




}
