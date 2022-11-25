package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomatePost {
    Properties props;


    /*
    1. In before class we actually get the api key from properties and use it in all the tests
    2. using request spec builder have set common requests and appended before executing the tests
    3. using response spec builder have set common validations/assertions for all the test cases
     */
    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://api.getpostman.com");
        requestSpecBuilder.addHeader("X-Api-Key", props.getProperty("key"));
        //It's important to set content type always when we use post request
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectContentType(ContentType.JSON);
        responseSpecBuilder.expectStatusCode(200);
        responseSpecBuilder.log(LogDetail.ALL);

        RestAssured.responseSpecification = responseSpecBuilder.build();

    }

    @Test
    public void validateGetStatusCode() throws IOException {
        /*
        1. this will get all the workspaces
        2. we have used spec builders for given() and then() and declared in before class so no
        need to use here
        3. want to add other requests/responses we can use given() and then() the rest assured will
        append along with commons above in before class
         */
        get("/workspaces");
    }

    /*
    1. In post request we usually send body so its always suggested to add header content type in
    post request
    2. here we're creating new workspace using post request
     */
    @Test
    public void validatePostRequestBddStyle(){
        String payload = "{\n" +
                "    \"workspace\":{\n" +
                "        \"name\": \"myFirstWorkspace\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\":\"Rest Assured created this\"\n" +
                "    }\n" +
                "}";
        given().body(payload).
        when()
                .post("/workspaces").
        then()
                .assertThat()
                .body("workspace.name", is(equalTo("myFirstWorkspace")),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    /*
    1. non  bdd style is nothing but we don't use given, when and then
    2. we use response return type and path method to replace the then() part
     */
    @Test
    public void validatePostRequestNonBddStyle(){
        String payload = "{\n" +
                "    \"workspace\":{\n" +
                "        \"name\": \"myFirstWorkspace\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\":\"Rest Assured created this\"\n" +
                "    }\n" +
                "}";
        Response response =with().
                body(payload)
                .post("/workspaces");
        assertThat(response.path("workspace.name"),is(equalTo("myFirstWorkspace")));
        assertThat(response.path("workspace.id"),matchesPattern("^[a-z0-9-]{36}$"));

    }




}
