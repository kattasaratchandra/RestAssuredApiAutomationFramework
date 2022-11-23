package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class RequestSpecificationExample {

    Properties props;
    RequestSpecification requestSpecification, requestSpecification1;

    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);

        requestSpecification = given().
                baseUri("https://api.getpostman.com").
                header("X-Api-Key", props.getProperty("key"));

        /* alternate way to request specification is request spec builder
        1.once we setup as below we declare request specification to request spec builder.build()
        2.in order to work we have to define like this in test case given().spec(requestspecification)
         */
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://api.getpostman.com");
        requestSpecBuilder.addHeader("X-Api-Key", props.getProperty("key"));
        requestSpecBuilder.log(LogDetail.ALL);

        requestSpecification1 = requestSpecBuilder.build();

        // we can declare request specification as default so that no need to give given() in test
        //all test cases can use as default, as request specification is static which can be used across
        RestAssured.requestSpecification = requestSpecBuilder.build();

    }



    /*
     three uses of request specification
     1. to reuse and avoid code duplication
     2. to define in non bdd style
     3. the test cases can append other headers as well according to the test case.
     */
    @Test
    public void validateGetStatusCode() throws IOException {

        /* after declaring request specification as above we can use interface variable
         request specification in two ways
        1. in given
        2. in spec
         */
        //1.
//        given(requestSpecification).
        /*
        1. converting the test case to non bdd style using request specification and response
        2. keeping then that will be covered in response specification
         */
        Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
        //2.
//        given().spec(requestSpecification).
//        when()
//                .get("/workspaces").
//        then()
//                .log().all()
//                .assertThat()
//                .statusCode(200);
    }

    /*
        1. in both test cases we can see some repeated code we can declare that as request
        specification and reuse it to avoid code duplication
        2. have created @beforeClass and declared request specification so that it get defined
        before running all the tests
     */

    @Test
    public void validateGetResponseBodyUsingHamcrestMatchers(){
//        given()
//                .baseUri("https://api.getpostman.com")
//                .header("X-Api-Key", props.getProperty("key")).

        Response response = requestSpecification.get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("workspaces.name"), hasItems("My Workspace", "Team Workspace", "test"));

//        // using spec
//        given().spec(requestSpecification).
//        when()
//                .get("/workspaces").
//        then()
//                .log().all()
//                .assertThat()
//                .statusCode(200)
//                .body("workspaces.name", hasItems("My Workspace", "Team Workspace", "test"));
    }

    // using request spec builder here
    @Test
    public void validateGetStatusCodeUsingRequestSpecBuilder() throws IOException {
        // the only difference is here we have to define like this in test case given().spec(requestspecification)
        // we can instead give in given as well given(requestspecification)
        Response response =
                // commenting the given() coz we're using the default request spec builder
//                given().spec(requestSpecification1)
                get("/workspaces").then().log().all().extract().response();
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // in order to query the request specification url, headers, parameters, cookies
    @Test
    public void queryRequestSpecification(){
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier
                .query(RestAssured.requestSpecification);
        System.out.println(queryableRequestSpecification.getBaseUri());
    }

}
