package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.ErrorConstants;
import com.ceng453.gameClient.constants.NetworkConstants;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class RegisterController {
    public String register(String username, String password){
        try {
            HttpResponse<String> response
                    = Unirest.post(NetworkConstants.API + "api/register")
                    .header("Content-Type","application/json")
                    .header("accept","application/json")
                    .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                    .asString();
            if(response.getStatus() == 200) {
                return "";
            }
            else{
                return response.getBody();
            }
        }
        catch (Exception e){
            return ErrorConstants.NETWORK_ERROR;
        }
    }
}
