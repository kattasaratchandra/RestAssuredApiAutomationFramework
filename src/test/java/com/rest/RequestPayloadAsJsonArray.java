package com.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
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
//        responseSpecBuilder.expectContentType(ContentType.JSON);
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

    /* The above test is using jackson dependency to convert the array to json. now we
    we use jackson outside the rest assured and convert array to json using object mapper class
    and writeValueAsString method
     */
    @Test
    public void validatePostRequestPayloadAsArrayListUsingObjectMapper() throws JsonProcessingException {
        HashMap<String,String> Map5001 = new HashMap<>();
        HashMap<String, String> Map5002 = new HashMap<>();
        Map5001.put("id","5001");
        Map5001.put("type","None");
        Map5002.put("id","5002");
        Map5002.put("type","Glazed");
        ArrayList<HashMap<String, String>> lst = new ArrayList<>();
        lst.add(Map5001);
        lst.add(Map5002);
        ObjectMapper objectMapper = new ObjectMapper();
        String lstString = objectMapper.writeValueAsString(lst);
        given().body(lstString).
                when()
                .post("/post").
                then()
                .assertThat()
                .body("msg", is(equalTo("Success")));
    }

        /*
     we're using array node from object mapper class which is same like map and send in two ways,
     we create object by using method createArrayNode() in object mapper class it returns the object.
     1. directly using the array node object
     2. converting the array node to json string using write value as string method
     */
        @Test
        public void validatePostRequestPayloadAsArrayListUsingArrayNode() throws JsonProcessingException {;
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode5001 = objectMapper.createObjectNode();
            ObjectNode objectNode5002 = objectMapper.createObjectNode();
            objectNode5001.put("id","5001");
            objectNode5001.put("type","None");
            objectNode5002.put("id","5002");
            objectNode5002.put("type","Glazed");
            ArrayNode arrayNode = objectMapper.createArrayNode();
            arrayNode.add(objectNode5001);
            arrayNode.add(objectNode5002);
            String lstString = objectMapper.writeValueAsString(arrayNode);
            given().body(lstString).
                    when()
                    .post("/post").
                    then()
                    .assertThat()
                    .body("msg", is(equalTo("Success")));
        }

    /*
       1.we have created simple pojo class of the payload and sending the object as argument to the body
    */
    @Test
    public void simplePojoExample() throws JsonProcessingException {
        // we can either send data using constructoe or set data by creating object and using setters
//        SimplePojo simplePojo = new SimplePojo("value1", "value2");
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.setKey1("value1");
        simplePojo.setKey2("value2");
//        String payload = "{\n" +
//                "    \"key1\" : \"value1\",\n" +
//                "    \"key2\" : \"value2\"\n" +
//                "}";
        SimplePojo deserialisation = given()
                .body(simplePojo)
                .log().all().
        when()
                .post("/postSimplePojo").
        then()
                .log().all()
        /*
        1. in order to perform deserialisation we extract the response as pojo class.
        2. we use object mapper to convert the pojo to json string and the response to json string
        3. then we use assertion and validate entire json of expected and actual using read tree
        4. To validate entire json we use read tree method but to validate few values we use getter
        and setters of the particular object
         */
                .extract()
                .as(SimplePojo.class);
         // creating object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        // converting the simple pojo to json string and deserialisation object to json string
        String simplePojoString = objectMapper.writeValueAsString(simplePojo);
        String deserialisationString = objectMapper.writeValueAsString(deserialisation);
        // using assertion to validating the entire body
        assertThat(objectMapper.readTree(simplePojoString),
                is(equalTo(objectMapper.readTree(deserialisationString))));

    }




}
