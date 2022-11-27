package com.rest;

import com.rest.workspace.Workspace;
import com.rest.workspace.WorkspaceRoot;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RequestPayloadMultipleWays {
    Properties props;

    @BeforeClass
    public void readApiKey() throws IOException {
        FileReader reader = new FileReader(System.getProperty("user.dir") + System.getProperty("file.separator")
                + "ApiKey.properties");
        props = new Properties();
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
    1. we create a file and store payaload in the fille and call it.
    2. if the payload has multiple variations we use different way rather than using file. if we have
    one variation of payload we use file
     */
    @Test
    public void validatePostRequestPayloadAsFile() {
        // creating file
        File file = new File("src/main/resources/createNewWorkspacePayload.json");
        given().body(file).
                when()
                .post("/workspaces").
                then()
                .assertThat()
                .body("workspace.name", is(equalTo("myFirstWorkspace")),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    // we create nested hash maps accordingly. use Object to add map to other map
    /*
    1.here the map data must be converted to json before it is sent as request
    2. rest assured uses jackson library to serialize the map data to json all we need to do
    is add jackson databind dependency in pom.xml file
     */
    @Test
    public void validatePostRequestPayloadAsMap() {
        // have created nested map according to the file data
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, String> nestedMap = new HashMap<>();
        nestedMap.put("name", "myFirstWorkspace");
        nestedMap.put("type", "personal");
        nestedMap.put("description", "Rest Assured created this");
        map.put("workspace", nestedMap);
        given().body(map).
                when()
                .post("/workspaces").
                then()
                .assertThat()
                .body("workspace.name", is(equalTo("myFirstWorkspace")),
                        "workspace.id", matchesPattern("^[a-z0-9-]{36}$"));
    }

    /*
    1. we created the pojo classes for workspace and sent in request
    2. we created data provider as well to rerun tests for multiple data
     */
    @Test(dataProvider = "workspace")
    public void validatePostRequestPayloadAsPojo(String name, String type, String description) {
        Workspace workspace = new Workspace(name, type, description);
        WorkspaceRoot workspaceRoot = new WorkspaceRoot(workspace);
        WorkspaceRoot deSerialiseWorkSpaceRoot =
        given()
                .body(workspaceRoot).
        when()
                .post("/workspaces").
        then()
                .extract()
                .as(WorkspaceRoot.class);
        assertThat(deSerialiseWorkSpaceRoot.getWorkspace().getName(), is(equalTo(workspace.getName())));
        assertThat(deSerialiseWorkSpaceRoot.getWorkspace().getId(), matchesPattern("^[a-z0-9-]{36}$"));

    }

    @DataProvider(name = "workspace")
    public Object[][] workspacePostData() {
        return new Object[][]{
                {"myFirstWorkspace", "personal", "Rest Assured created this team"},
                {"myTeamWorkspace", "team", "Rest Assured created this team"}
        };
    }




    // using data provider for testing on mulitple data.





}
