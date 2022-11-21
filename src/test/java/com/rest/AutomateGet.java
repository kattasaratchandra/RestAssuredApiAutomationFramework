package com.rest;

/*
 static imports
 pro:
 1. can use methods directly without the class name for better readability
 cons
 1. have to know method names what to use when as won't be using classe name and they
 won't be suggestions
 */

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AutomateGet {

    public Properties props;

    @Test
    public void validateGetStatusCode() throws IOException {
        given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
        when().get("/workspaces").
        then().log().all()
                .assertThat()
                .statusCode(200);
    }

    /* using before class to get the required api key from properties file which helps in
    hiding the key
     */
    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader=new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props=new Properties();
        props.load(reader);

    }

    @Test
    public void validateGetResponseBodyUsingHamcrestMatchers(){
        given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                when().get("/workspaces").
                then().log().all()
                .assertThat()
                .statusCode(200)
                /* we use hamcrest matchers for assertions, here in body we matched gpath expression(which gives
                   actual value) with expected data. we used hasitems to get array of elements using double quotes
                 */
                .body("workspaces.name", hasItems("My Workspace", "Team Workspace", "test"),
                        "workspaces.type", hasItems("personal", "team", "team"),
                        /*  we use equal to hamcrest matcher to compare one value, is as a decorator
                        for more readability, size() for the count, hasitem to check if the element
                        is present in array
                         */
                        "workspaces[0].name", equalTo("My Workspace"),
                        "workspaces[0].name", is(equalTo("My Workspace")),
                        "workspaces.size()", equalTo(3),
                        "workspaces.name", hasItem("test"));
                /* 1. best part of using hamcrest matchers are if the assertion fail it still continues to assert
                    the rest of the next assertions in a given test method if we use it in body as above
                   2. has item/items checks if element/elements are present or not that's it, If you want me more specific
                   we use contains/containsInAnyOrder
                 */
    }

    @Test
    public void extractResponseBody(){
        // response is an interface, and we use extract().response() to get the response
        Response response = given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                when().get("/workspaces").
                then()
//                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();
        /* the response only prints the body, the header info and status codes are printed
        when we used sout we need to explicitly extract
         */
        System.out.println(response.asString());
    }

    @Test
    public void extractSingleValueFromResponse(){
        // response is an interface, and we use extract().response() to get the response
        // we can convert the response to string using asString()
        String response = given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                when().get("/workspaces").
                then()
//                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response().asString();
        /* four ways we can extract the single value from response
        1. using jsonpath
        JsonPath jsonpath = new JsonPath(response.asString());
        System.out.println("workspace name using jsonpath:" + jsonpath.getString("workspaces[0].name"));
        // 2. using path method on response interface variable directly
        System.out.println("workspace name using path method:" + response.path("workspaces[0].name"));
        */
        //3. using Jsonpath directly on response, but it should be string as return type, so we use asString()
        System.out.println("workspace name using jsonpath: " + JsonPath.from(response).getString("workspaces[0].name"));
        //4. using path method directly on response() and that will return the value as string type
    }


    @Test
    public void hamcrestAndTestngAssertOnExtractedResponse() {
        // response is an interface, and we use extract().response() to get the response
        // we can convert the response to string using asString()
        String name = given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                when().get("/workspaces").
                then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract()
                .response().path("workspaces[0].name");
        // we can assert using hamcrest and as well with testng
        //1. hamcrest
        assertThat(name, equalTo("My Workspace"));
        //2. testng
        Assert.assertEquals(name, "My Workspace");
    }

    @Test
    public void validateResponseBodyHamcrestMatchersLearnings(){
        given()
                .baseUri("https://api.getpostman.com")
                .header("X-Api-Key", props.getProperty("key")).
                when().get("/workspaces").
                then().log().all()
                .assertThat()
                .statusCode(200)
                .body("workspaces.name", hasItems("My Workspace", "Team Workspace"),
                        "workspaces.name", contains("My Workspace", "Team Workspace", "test"),
                        /* 1. the difference between has items and contains are contains check elements are
                            in collection and in strict order, has items checks only all elements are in collection
                           2. containsInAnyOrder checks elements in collection in any order
                           3. mostly used other matchers in hasmcrest as below:*/
//                        "workspaces.name", empty(),
                        "workspaces.name", not(empty()),
                        "workspaces.name", is(not(emptyArray())),
                        "workspaces.name", hasSize(3),
//                        "workspaces.name", everyItem(startsWith("My")));
                        // hamcrest matchers for maps
                        "workspaces[0]", hasKey("id"),
                        "workspaces[0]", hasValue("My Workspace"),
                        "workspaces[0]", hasEntry("id","61011538-571a-4470-8397-9e213771b9e0"),
                        "workspaces[0]", not(equalTo(Collections.EMPTY_MAP)),
                        /* has key checks if key is present in the collection and has value checks the value
                        has entry checks for key and value both
                         */
                        "workspaces.name", allOf(hasSize(3), not(empty())));
                        // all of,checks if all the matchers matches.

    }





}
