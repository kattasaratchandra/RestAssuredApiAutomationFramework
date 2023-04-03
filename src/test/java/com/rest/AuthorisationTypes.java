package com.rest;

import java.util.Base64;

public class AuthorisationTypes {


    public static void main(String[] args) {

        String usernamePassword = "myUsername:myPassword";
        /***
         * Basic authentication
         * 1. here we encode the username and password as base64 and send it as header in request. the
         * server authorises and send response
         * 2. this is not secure its easy to decode and encode using base64 class in java.util lib
         */

        String base64Encoded = Base64.getEncoder().encodeToString(usernamePassword.getBytes());
        System.out.println("encoded data : " + base64Encoded);
        byte[] decoded = Base64.getDecoder().decode(base64Encoded);
        System.out.println("decoded data : " + new String(decoded));

        // digest authentication
        //1.  the username and password are sent in encripted format using hashing algorithm

        /***
         * bearer token
         * 1. when we send request with username and password the server response with bearer token which
         * is random text that can be used in subsequent requests as authorisation
         * 2. it is a part of oauth 2.0  but can be used independently as well
         * 3. its sent as BEARER space {token} in header
         */




    }


}
