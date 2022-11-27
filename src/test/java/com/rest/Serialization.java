package com.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Serialization {

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


    /*
    1. rest assure automatically convert the map data to json with the help of jackson library all
    we did is to add jackson dependency to pom.xml
    2. Here we actually do serialisation outside restassured using objectmapper class and send
    map data as json string format.
     */
    @Test
    public void validatePostRequestPayloadAsMap() throws JsonProcessingException {
        // have created nested map according to the file data
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, String> nestedMap = new HashMap<>();
        nestedMap.put("name","myFirstWorkspace");
        nestedMap.put("type","personal");
        nestedMap.put("description","Rest Assured created this");
        map.put("workspace", nestedMap);
        /*
         we're creating object mapper class and using method 'writeValueasString' and send map as
         argument. this convert map to json
         */
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(map);

        given().body(jsonString).
                when()
                .post("/workspaces").
                then()
                .assertThat()
                .body("workspace.name", is(equalTo("myFirstWorkspace")),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    /*
    the method writeValueAsString is method overloading function we send arrays/maps as argument
    it will covert to json string. check example in Request payload as json string array class
     */


    /*
     we're using object node from object mapper class which is same like map and send in two ways,
     we create object by using method createObjectNode() in object mapper class it returns the object.
     1. directly using the create node object
     2. converting the create node to json string using write value as string method
     */
    @Test
    public void validatePostRequestPayloadAsObjectNode() throws JsonProcessingException {
        // have created nested map according to the file data
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode nestedObjectNode = objectMapper.createObjectNode();

        nestedObjectNode.put("name","myFirstWorkspace");
        nestedObjectNode.put("type","personal");
        nestedObjectNode.put("description","Rest Assured created this");
        // using set here as put is depreciated
        objectNode.set("workspace", nestedObjectNode);
        String jsonString = objectMapper.writeValueAsString(objectNode);
        // 1.
        given().body(jsonString).
        //2.
//        given().body(objectNode)
                when()
                .post("/workspaces").
                then()
                .assertThat()
                .body("workspace.name", is(equalTo("myFirstWorkspace")),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }




}
