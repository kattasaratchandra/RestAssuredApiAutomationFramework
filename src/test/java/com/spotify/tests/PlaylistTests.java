package com.spotify.tests;

import com.spotify.pojo.Error;
import com.spotify.pojo.ErrorRoot;
import com.spotify.pojo.Playlist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    /***
     * using request and response spec builder for reusability and avoid code duplication
     */
    @BeforeClass
    public void beforeClass(){
        String accessToken = "BQAU8wStZojRjiWPtcfcenzM5aWmKMXIvR32dASrbKtHiYtu7fMBAB6gkGUG6Dze8mLlLYEOCnPvMBot0OzN0jCPJEVSYgSaYe2r0ISv9Ny-Ynmpjq8JMNkVGVofox0VWps5_LoqDvL_U52KodpAXCVZLO4uwicgb47iMqw9wk5onAItPUjfzDwXTxaPMcB7zm0NB9yaaaxMel6XbV78h3IWkNllFyDtcBksfXWBuMc-MuE0XobBflvLweE-" +
                "6lvgPU3Aesr3MhFkAgaOBs6-x2P_1UpJ2Uo";
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
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("New Playlist");
        requestPlaylist.setDescription("New playlist description");
        requestPlaylist.setPublic(false);

        Playlist responsePlaylist = given(requestSpecification)
                .body(requestPlaylist).
                when()
                .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
                then()
                .spec(responseSpecification)
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(Playlist.class);
        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }

    @Test
    public void getPlaylistTest(){

        Playlist responsePlaylist = given(requestSpecification).
                when()
                .get("/playlists/0KECGlxB2DsPeUtevwGlKJ").
                then()
                .spec(responseSpecification)
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo("New Playlist"));
        assertThat(responsePlaylist.getDescription(), equalTo("New playlist description"));
        assertThat(responsePlaylist.getPublic(), equalTo(false));
    }

    @Test
    public void updatePlaylistTest(){
        Playlist playlist = new Playlist();
        playlist.setName("New Playlist");
        playlist.setDescription("New playlist description");
        playlist.setPublic(false);
        given(requestSpecification).
                body(playlist).
                when().
        put("/playlists/0KECGlxB2DsPeUtevwGlKJ").
        then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void ShouldNotcreatePlaylistWithExpiredTokenTest(){
        Playlist playlist = new Playlist();
        playlist.setName("New Playlist");
        playlist.setDescription("New playlist description");
        playlist.setPublic(false);

        ErrorRoot responseError = given().baseUri("https://api.spotify.com")
                .basePath("v1")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + "1234")
                .body(playlist)
                .log().all().
        when()
                .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
        then()
                .spec(responseSpecification)
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(ErrorRoot.class);

        assertThat(responseError.getError().getStatus(), equalTo(401));
        assertThat(responseError.getError().getMessage(), equalTo("Invalid access token"));

    }

    @Test
    public void ShouldNotcreatePlaylistWithoutNameTest(){
        Playlist playlist = new Playlist();
        playlist.setDescription("New playlist description");
        playlist.setPublic(false);
        ErrorRoot responseError = given(requestSpecification)
                .body(playlist).
                when()
                .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
                then()
                .spec(responseSpecification)
                .assertThat()
                .contentType(ContentType.JSON)
                .extract()
                .as(ErrorRoot.class);
        assertThat(responseError.getError().getStatus(), equalTo(400));
        assertThat(responseError.getError().getMessage(), equalTo("Missing required field: name"));

    }


}
