package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ResponseSpecificationExample {
    Properties props;
    ResponseSpecification responseSpecification;


    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);
        // created default request specification and added url, header, logs
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://api.getpostman.com");
        requestSpecBuilder.addHeader("X-Api-Key", props.getProperty("key"));
        requestSpecBuilder.log(LogDetail.ALL);
        RestAssured.requestSpecification = requestSpecBuilder.build();
        /*
        we create response specification for common validations. here we have status code, content type
        common validation, so we create response specification for those. And use it after then() as spec
        argument
         */
//        responseSpecification = RestAssured.expect()
//                .statusCode(200)
//                .contentType("application/json; charset=utf-8")
//                .logDetail(LogDetail.ALL);

        // alternate way is using response spec builder
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectContentType("application/json; charset=utf-8");
        responseSpecBuilder.expectStatusCode(200);
//        responseSpecification = responseSpecBuilder.build();

        // we can declare as default response spec builder so that no need to use then()
        RestAssured.responseSpecification = responseSpecBuilder.build();

    }

    @Test
    public void validateGetStatusCode() throws IOException {
        //since we have default specification we don't need to use given()
        get("/workspaces");
                // 1. we use spec and send argument as response specification for common validations
                // 2. commented coz now we declared as default
//                .then().spec(responseSpecification);


    }

    @Test
    public void validateGetResponseBodyUsingHamcrestMatchers(){
        Response response =
                get("/workspaces").
                then()
//                        .log().all();
                        .extract().response();
//        assertThat(response.statusCode(), is(equalTo(200)));
//        assertThat(response.contentType(), is(equalTo("application/json; charset=utf-8")));
        assertThat(response.path("workspaces.name"), hasItems("My Workspace", "Team Workspace", "test"));
    }




}
