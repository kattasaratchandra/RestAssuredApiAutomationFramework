package com.spotify.api.application_api;

import com.spotify.pojo.Playlist;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.spotify.api.SpecBuilder.getRequestSpec;
import static com.spotify.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class PlaylistApi {

    static String access = "BQDsncZYejZCkJGzQ0iXqs1zuuiryZ5c3I2_rUtufFUiKeeXOpstZpOdz9FOfOWjHoYexGojhZ08FrxWibRN5acK0ChLShdo_KxCoPN6DhRvfA6Pi7Hk5WsRNWyNsELt9c9RmFjbwy9bd72r2DdZ_GKPMC2-ZwW4ZfY-gMQg3HA0hyJcds_2XBbY3MwP49zpRzdhobo6nvin-dhjg-C686cr5zl0MoqRBB8OJGJrqAy7MMV8PW3O_tHg8GQTIRTgki5BznxgHTr" +
            "-yT0he9nEmVeGvwECV-s";
    public static Response post(Playlist requestPlaylist){
        return given(getRequestSpec())
                .header("Authorization", "Bearer " + access)
                .body(requestPlaylist).
                when()
        .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
                then()
                .spec(getResponseSpec())
                .extract()
                .response();
    }

    public static Response post(Playlist requestPlaylist, String accessToken){
        return given(getRequestSpec())
                .body(requestPlaylist)
                .header("Authorization", "Bearer " + accessToken).
        when()
                .post("/users/31zrr46y63qhylvgsrjzsr6emgwi/playlists").
                then()
                .spec(getResponseSpec())
                .extract()
                .response();
    }

    public static Response get(String playlistId){
        return given(getRequestSpec())
                .header("Authorization", "Bearer " + access).
                when()
                .get("/playlists/" + playlistId).
                then()
                .spec(getResponseSpec())
                .extract()
                .response();
    }

    public static Response update(Playlist playlist, String playlistId){
        return given(getRequestSpec())
                .header("Authorization", "Bearer " + access).
                body(playlist).
                when().
                put("/playlists/" +playlistId).
                then()
                .spec(getResponseSpec())
                .extract()
                .response();
    }
}
