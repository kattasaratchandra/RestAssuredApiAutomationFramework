package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AutomatePut {
    Properties props;


    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://api.getpostman.com");
        requestSpecBuilder.addHeader("X-Api-Key", props.getProperty("key"));
        requestSpecBuilder.setContentType(ContentType.JSON);
        requestSpecBuilder.log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectContentType(ContentType.JSON);
        responseSpecBuilder.expectStatusCode(200);
        responseSpecBuilder.log(LogDetail.ALL);

        RestAssured.responseSpecification = responseSpecBuilder.build();

    }

    // we are using put request to update the workspace name
    @Test
    public void validatePutRequestBddStyle(){
        String workspaceId = "73ce84c4-cbae-44a4-9f6b-118b07e58382";
        String payload = "{\n" +
                "    \"workspace\":{\n" +
                "        \"name\": \"myFirstWorkspace\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\":\"Rest Assured created this\"\n" +
                "    }\n" +
                "}";
        given().body(payload).
        when().
                put("/workspaces/" + workspaceId).
        then()
                .assertThat()
                .body("workspace.name", is(equalTo("myFirstWorkspace")),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", is(equalTo(workspaceId)));;
    }

    /*
    1. path parameters are nothing but it will add to url after back slash
    2. query paramters adds to the url after ? mark and uses & for multiplae query parameters
     */
    @Test
    public void validatePutRequestBddStyleUsingPathParameter(){
        String workspaceId = "73ce84c4-cbae-44a4-9f6b-118b07e58382";
        String payload = "{\n" +
                "    \"workspace\":{\n" +
                "        \"name\": \"myNewWorkspace\",\n" +
                "        \"type\": \"personal\",\n" +
                "        \"description\":\"Rest Assured created this\"\n" +
                "    }\n" +
                "}";
        given().
                body(payload).
                pathParam("workspaceId", workspaceId).
        when().
                put("/workspaces/{workspaceId}").
        then()
                .assertThat()
                .body("workspace.name", is(equalTo("myNewWorkspace")),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", is(equalTo(workspaceId)));;
    }



}
