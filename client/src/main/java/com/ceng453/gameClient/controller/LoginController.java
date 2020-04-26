package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.ErrorConstants;
import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.NetworkConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.scenes.MainMenuScreen;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class LoginController {
    /**
     * This method sends POST request to /api/login to give authentication to the current player.
     *
     * @param username Username of the current player
     * @param password Password of the current player
     * @return if successful returns the JwtToken of the current session.
     *         if not returns Network Error message.
     */
    public String login(String username, String password) {
        try {
            HttpResponse<String> response
                    = Unirest.post(NetworkConstants.API + "api/login")
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                    .asString();
            if (response.getStatus() == 200) {
                GameConstants.username = username;
                NetworkConstants.jwtToken = response.getBody();
                SceneConstants.stage.setScene(MainMenuScreen.createContent());
                return "";
            } else if (response.getStatus() == 403) {
                return ErrorConstants.BAD_CREDENTIALS_ERROR;
            } else
                return response.getBody();

        } catch (Exception e) {
            return ErrorConstants.NETWORK_ERROR;
        }

    }
}
