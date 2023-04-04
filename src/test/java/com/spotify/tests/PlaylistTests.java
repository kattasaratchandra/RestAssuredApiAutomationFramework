package com.spotify.tests;

import com.spotify.api.application_api.PlaylistApi;
import com.spotify.pojo.Error;
import com.spotify.pojo.ErrorRoot;
import com.spotify.pojo.Playlist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.spotify.api.SpecBuilder.getRequestSpec;
import static com.spotify.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    @Test
    public void createPlaylistTest(){
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("New Playlist");
        requestPlaylist.setDescription("New playlist description");
        requestPlaylist.setPublic(false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.statusCode(), equalTo(201));
        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }

    @Test
    public void getPlaylistTest(){

        Response response = PlaylistApi.get("0KECGlxB2DsPeUtevwGlKJ");
        assertThat(response.statusCode(), equalTo(200));
        Playlist responsePlaylist = response.as(Playlist.class);

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

        Response response = PlaylistApi.update(playlist, "0KECGlxB2DsPeUtevwGlKJ");
        assertThat(response.statusCode(), equalTo(200));

    }

    @Test
    public void ShouldNotcreatePlaylistWithExpiredTokenTest(){
        Playlist playlist = new Playlist();
        playlist.setName("New Playlist");
        playlist.setDescription("New playlist description");
        playlist.setPublic(false);

        Response response = PlaylistApi.post(playlist, "1234");
        ErrorRoot responseError = response.as(ErrorRoot.class);

        assertThat(responseError.getError().getStatus(), equalTo(401));
        assertThat(responseError.getError().getMessage(), equalTo("Invalid access token"));

    }

    @Test
    public void ShouldNotcreatePlaylistWithoutNameTest(){
        Playlist playlist = new Playlist();
        playlist.setDescription("New playlist description");
        playlist.setPublic(false);

        Response response = PlaylistApi.post(playlist);
        ErrorRoot responseError = response.as(ErrorRoot.class);

        assertThat(responseError.getError().getStatus(), equalTo(400));
        assertThat(responseError.getError().getMessage(), equalTo("Missing required field: name"));

    }


}
