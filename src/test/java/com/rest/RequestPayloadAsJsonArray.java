package com.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RequestPayloadAsJsonArray {
    Properties props;
    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://9da9bae4-fc0c-4fe1-be7a-2641b7e101e8.mock.pstmn.io");
        requestSpecBuilder.addHeader("x-mock-match-request-body", "true");
        //It's important to set content type always when we use post request
        requestSpecBuilder.setContentType("application/json;charset=utf-8");
        // we can set default encoding format using config
//        requestSpecBuilder.setConfig(config.encoderConfig(EncoderConfig.encoderConfig()
//                .appendDefaultContentCharsetToContentTypeIfUndefined(false)));
        requestSpecBuilder.log(LogDetail.ALL);

        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectContentType("application/json;charset=utf-8");
        responseSpecBuilder.expectContentType(ContentType.JSON);
        responseSpecBuilder.expectStatusCode(200);
        responseSpecBuilder.log(LogDetail.ALL);

        RestAssured.responseSpecification = responseSpecBuilder.build();

    }

    /*
    1. if we don't mention any encoding format rest assure uses default encoding format. so check
    the content type in response and request first when debugging any issue
    2. we can tell rest assured not to use encoding format by default using config
    3.
     */
    @Test
    public void validatePostRequestPayloadAsArrayList(){
        HashMap<String,String> Map5001 = new HashMap<>();
        HashMap<String, String> Map5002 = new HashMap<>();
        Map5001.put("id","5001");
        Map5001.put("type","None");
        Map5002.put("id","5002");
        Map5002.put("type","Glazed");
        ArrayList<HashMap<String, String>> lst = new ArrayList<>();
        lst.add(Map5001);
        lst.add(Map5002);
        given().body(lst).
                when()
                .post("/post").
                then()
                .assertThat()
                .body("msg", is(equalTo("Success")));
    }

}
