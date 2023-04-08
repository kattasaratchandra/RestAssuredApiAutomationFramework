package com.spotify.api.application_api;

import com.spotify.api.EndPoints;
import com.spotify.api.RestResource;
import com.spotify.api.TokenManager;
import com.spotify.pojo.Playlist;
import com.spotify.utils.ConfigLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static com.spotify.api.EndPoints.*;
import static com.spotify.api.SpecBuilder.getRequestSpec;
import static com.spotify.api.SpecBuilder.getResponseSpec;
import static com.spotify.api.TokenManager.getToken;
import static io.restassured.RestAssured.given;

public class PlaylistApi {

    public static Response post(Playlist requestPlaylist){
        return RestResource.post(requestPlaylist, getToken(), USERS + "/" + ConfigLoader.getInstance().getUserId() + PLAYLISTS);
    }

    public static Response post(Playlist requestPlaylist, String accessToken){
        return RestResource.post(requestPlaylist, accessToken, USERS + "/" + ConfigLoader.getInstance().getUserId() + PLAYLISTS);
    }

    public static Response get(String playlistId){
        return RestResource.get(PLAYLISTS + "/" + playlistId, getToken());
    }

    public static Response update(Playlist playlist, String playlistId){
        return RestResource.update(playlist, PLAYLISTS+ "/" +playlistId, getToken());
    }
}
