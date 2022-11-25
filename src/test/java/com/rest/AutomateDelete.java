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

public class AutomateDelete {
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


    @Test
    public void validateDeleteRequest(){
        String workspaceId = "7e51eca5-5f9c-4ef7-b01e-07ca1cc39801";
        given().
                pathParam("workspaceId", workspaceId).
        when().
                delete("/workspaces/{workspaceId}").
        then()
                .assertThat()
                .body("workspace.id", matchesPattern("^[a-z0-9-]{36}$"),
                        "workspace.id", is(equalTo(workspaceId)));;
    }
}
