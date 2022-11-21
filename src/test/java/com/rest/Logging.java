package com.rest;

import io.restassured.config.LogConfig;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class Logging {
    Properties props;

    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);
    }

    /* log.all logs entire request and response. we usually use log() before assertion
    so that it won't affect the log
     */
    @Test
    public void requestResponseLoggingAll(){
        given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
//                log().all().
        // we can only log headers, parameters, cookies, body
                log().headers().
        when()
                .get("/workspaces").
        then()
//                .log().all()
                .log().body()
                .assertThat()
                .statusCode(200);
    }

    // we use iferror method in case if want to log only when there is error
    @Test
    public void logOnlyIfError(){
        given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                log().all().
        when()
                .get("/workspaces").
        then()
                .log().ifError()
                .assertThat()
                .statusCode(200);
    }

    // we use 'if validation fails' for assertion failures so that it logs when failed
    @Test
    public void logOnlyIfValidationFails(){
        given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                /*
                 As we're using 'if validation fails' separately for request and response to avoid
                 code duplication we can use config and enable logging of request and response if
                 validation fails
                 */
                config(config.logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails())).
//                log().ifValidationFails().
        when()
                .get("/workspaces").
        then()
//                .log().ifValidationFails()
                .assertThat()
                .statusCode(200);
    }

    /* we can avoid logging certain headers by using blacklist header method and passing
    header name as argument */
    @Test
    public void logBlacklistHeaders(){
        Set<String> headers = new HashSet<>();
        headers.add("X-Api-Key");
        headers.add("Accept");
        given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                /* to black list one header we use like this for multiple we have to send collections
                by using blacklist headers as method
                 */
//                config(config.logConfig(LogConfig.logConfig().blacklistHeader("X-Api-Key"))).
                config(config.logConfig(LogConfig.logConfig().blacklistHeaders(headers))).
                log().all().
        when()
                .get("/workspaces").
        then()
                .assertThat()
                .statusCode(200);
    }




}
