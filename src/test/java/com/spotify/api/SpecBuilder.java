package com.spotify.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilder {
    static String accessToken = "BQAU8wStZojRjiWPtcfcenzM5aWmKMXIvR32dASrbKtHiYtu7fMBAB6gkGUG6Dze8mLlLYEOCnPvMBot0OzN0jCPJEVSYgSaYe2r0ISv9Ny-Ynmpjq8JMNkVGVofox0VWps5_LoqDvL_U52KodpAXCVZLO4uwicgb47iMqw9wk5onAItPUjfzDwXTxaPMcB7zm0NB9yaaaxMel6XbV78h3IWkNllFyDtcBksfXWBuMc-MuE0XobBflvLweE-" +
            "6lvgPU3Aesr3MhFkAgaOBs6-x2P_1UpJ2Uo";
    public static RequestSpecification getRequestSpec(){
        return new RequestSpecBuilder().setBaseUri("https://api.spotify.com")
                .setBasePath("/v1")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }
    public static RequestSpecification getAccountRequestSpec(){
        return new RequestSpecBuilder().setBaseUri("https://accounts.spotify.com")
                .setContentType(ContentType.URLENC)
                .log(LogDetail.ALL)
                .build();
    }


    public static ResponseSpecification getResponseSpec(){
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }
}
