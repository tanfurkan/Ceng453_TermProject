package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.ErrorConstants;
import com.ceng453.gameClient.constants.NetworkConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.scenes.MainMenuScreen;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class LoginController{
    public String login(String username, String password){
        try {
            HttpResponse<JsonNode> response
                    = Unirest.post(NetworkConstants.API + "api/login")
                    .header("Content-Type","application/json")
                    .header("accept","application/json")
                    .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                    .asJson();
            if(response.getStatus() == 200) {
                NetworkConstants.jwtToken = response.getBody().getObject().getString("jwt");
                SceneConstants.stage.setScene(MainMenuScreen.createContent());
                return "";
            }
            else{
                return response.getBody().toString();
            }
        }
        catch (Exception e){
            return ErrorConstants.NETWORK_ERROR;
        }

    }
}
