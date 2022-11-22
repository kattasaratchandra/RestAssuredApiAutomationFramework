package com.rest;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class AutomateHeaders {
    Properties props;

    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);
    }

    /*
    we can send headers multiple ways
    1. using directly 'header()' method on given() sending the arguments as key and values
    2. creating header object and using reference of that object in header() method as arguement
     */
    @Test
    public void MultipleHeadersUsingHeaderInTheRequest(){
        //2.
        Header headerName = new Header("headerName", "value1");
        Header xMockRequestHeaders = new Header("x-mock-match-request-headers", "headerName");
        given()
                .baseUri("https://7440d29e-478a-4116-b3ba-68706c794bcd.mock.pstmn.io")
                //1.
//                .header("headerName", "value1")
//                .header("x-mock-match-request-headers", "headerName").
                //2.
                .header(headerName)
                .header(xMockRequestHeaders).
                log().all().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }


    // we use headers class here instead of header
    @Test
    public void MultipleHeadersUsingHeadersInTheRequest(){
        Header headerName = new Header("headerName", "value1");
        Header xMockRequestHeaders = new Header("x-mock-match-request-headers", "headerName");
        Headers headers = new Headers(headerName, xMockRequestHeaders);
        given()
                .baseUri("https://7440d29e-478a-4116-b3ba-68706c794bcd.mock.pstmn.io")
                .headers(headers).
//                .header(headerName)
//                .header(xMockRequestHeaders).
                log().all().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    // we use hashmap instead headers
    @Test
    public void MultipleHeadersUsingMapInTheRequest(){
        HashMap<String, String> hashMapHeaders = new HashMap<>();
        hashMapHeaders.put("headerName", "value1");
        hashMapHeaders.put("x-mock-match-request-headers", "headerName");
        given()
                .baseUri("https://7440d29e-478a-4116-b3ba-68706c794bcd.mock.pstmn.io")
                // headers method is overloaded we're sending now argument as hashmap
                .headers(hashMapHeaders)
                .log().all().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    /* multi value headers we can do in following ways
       1. by using header with single key and multi values, we cannot use hashmap as duplication of keys happen which
       hashmap won't support, so we use headers object
       2.  by using headers object
     */
    @Test
    public void MultiValueHeadersInTheRequest(){
        Header headerName = new Header("headerName", "value1");
        Header xMockRequestHeaders = new Header("x-mock-match-request-headers", "headerName");
        /* 2.
         we're sending multi value header by using same key with two different values as below and send it
         as arguments for headers reference
         */
        Header multiValue1 = new Header("mutlivalue", "value1");
        Header multiValue2 = new Header("multivalue", "value2");
        Headers headers = new Headers(headerName, xMockRequestHeaders, multiValue1, multiValue2);
        given()
                .baseUri("https://7440d29e-478a-4116-b3ba-68706c794bcd.mock.pstmn.io")
//                .header(headerName)
//                .header(xMockRequestHeaders)
//                //1.
//                .header("mutlivalue", "value1", "value2")
                .headers(headers)
                .log().headers().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
                .statusCode(200);
    }

    // we assert response header by two ways using header() and headers() methods after then().asserThat()
    @Test
    public void assertResponseHeaders(){
        Header headerName = new Header("headerName", "value1");
        Header xMockRequestHeaders = new Header("x-mock-match-request-headers", "headerName");
        Headers headers = new Headers(headerName, xMockRequestHeaders);
        given()
                .baseUri("https://7440d29e-478a-4116-b3ba-68706c794bcd.mock.pstmn.io")
                .headers(headers)
                .log().headers().
        when()
                .get("/get").
        then()
                .log().all()
                .assertThat()
//                // using header for assertion
//                .header("responseHeader", "resvalue1")
                // using headers for mutliple headers assertion
                .headers("responseHeader", "resvalue1", "Content-Length", "31")
                .statusCode(200);
    }

    // we use extract().headers() to extract headers from response
    @Test
    public void extractResponseHeaders() {
        Header headerName = new Header("headerName", "value1");
        Header xMockRequestHeaders = new Header("x-mock-match-request-headers", "headerName");
        Headers headers = new Headers(headerName, xMockRequestHeaders);
        Headers extractedHeaders = given()
                .baseUri("https://7440d29e-478a-4116-b3ba-68706c794bcd.mock.pstmn.io")
                .headers(headers)
                .log().headers().
                when()
                .get("/get").
                then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .headers();
        // from this extractors headers we can get particular head name and value using getName and
        // getValue methods
        System.out.println("name of header:" + extractedHeaders.get("responseHeader").getName());
        System.out.println("value of header:" + extractedHeaders.get("responseHeader").getValue());
        System.out.println("value of header:" + extractedHeaders.getValue("responseHeader"));

        //to print all the response headers we use enchance for loop
        for (Header header : extractedHeaders) {
            System.out.print("headerName:" + header.getName() + ", ");
            System.out.println("headerValue:" + header.getValue());
        }
    }

        // To extract multi value headers we extract by using getValues() method and return type will be list
        @Test
        public void extractMultiValueResponseHeaders(){
            Header headerName = new Header("headerName", "value1");
            Header xMockRequestHeaders = new Header("x-mock-match-request-headers", "headerName");
            Headers headers = new Headers(headerName, xMockRequestHeaders);
            Headers extractedHeaders = given()
                    .baseUri("https://7440d29e-478a-4116-b3ba-68706c794bcd.mock.pstmn.io")
                    .headers(headers)
                    .log().headers().
            when()
                    .get("/get").
            then()
                    .log().all()
                    .assertThat()
                    .statusCode(200)
                    .extract()
                    .headers();
            // list of multiple values of header
            List<String> values = extractedHeaders.getValues("multiValueHeader");
            // using enchance for loop to print the list
            for(String value: values){
                System.out.println(value);
            }

    }




}
