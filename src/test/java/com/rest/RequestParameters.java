package com.rest;

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
}
