package spotify;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    /***
     * using request and response spec builder for reusability and avoid code duplication
     */
    @BeforeClass
    public void beforeClass(){
        String accessToken = "BQDW7iblimYwoNTQgOB5cwaGKVz_RIwp6iALNp2SG8PN0zvjlwmh_JOd0kkQKrB-yc3FeNxdCIvYYqeZ-_e_vipIMOb0IdSE5Az5879ydK90c9rwe48rnqj0kPW9Ax84e1vJpuKzuNgfvbLjCz7c6Hv4sasuozpnam08URsLnKWUbIOSWUvHkNgERbR7XzlXgPL9Fee6PvMnJ-ZO-GlrE9wZb_GrQ5Q6_lj1sqpHgv4TZj-" +
                "1a2nGkzDW7d0BdFben7BBKe8YaDA8JtqYxPgT-w0_uFEZNxQ";
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri("https://api.spotify.com")
                .setBasePath("v1")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + accessToken)
                .log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder
                .log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();

    }

    @Test
    public void createPlaylistTest(){
        String playlistPayload = "{\n" +
                "    \"name\": \"New Playlist\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        given(requestSpecification)
                .body(playlistPayload).
        when()
                .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
        then()
                .spec(responseSpecification)
                .assertThat()
                .body("name", equalTo("New Playlist"),
                        "description", equalTo("New playlist description"),
                        "public", equalTo(false)
                        )
                .contentType(ContentType.JSON);
    }

    @Test
    public void getPlaylistTest(){
        given(requestSpecification).
                when()
                .get("/playlists/0KECGlxB2DsPeUtevwGlKJ").
                then()
                .spec(responseSpecification)
                .assertThat()
                .body("name", equalTo("New Playlist"),
                        "description", equalTo("New playlist description"),
                        "public", equalTo(false)
                )
                .contentType(ContentType.JSON);

    }

    @Test
    public void updatePlaylistTest(){
        String playlistPayload = "{\n" +
                "    \"name\": \"New Playlist\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        given(requestSpecification).
                body(playlistPayload).
                when().
        put("/playlists/0KECGlxB2DsPeUtevwGlKJ").
        then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(200);


    }

    @Test
    public void ShouldNotcreatePlaylistWithExpiredTokenTest(){
        String playlistPayload = "{\n" +
                "    \"name\": \"New Playlist\",\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        given().baseUri("https://api.spotify.com")
                .basePath("v1")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + "1234")
                .body(playlistPayload)
                .log().all().
        when()
                .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
        then()
                .spec(responseSpecification)
                .assertThat()
                .body("error.status", equalTo(401),
                        "error.message", equalTo("Invalid access token"))
                .contentType(ContentType.JSON);
    }

    @Test
    public void ShouldNotcreatePlaylistWithoutNameTest(){
        String playlistPayload = "{\n" +
                "    \"description\": \"New playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        given(requestSpecification)
                .body(playlistPayload).
                when()
                .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
                then()
                .spec(responseSpecification)
                .assertThat()
                .body("error.status", equalTo(400),
                        "error.message", equalTo("Missing required field: name"))
                .contentType(ContentType.JSON);
    }


}
