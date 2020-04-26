package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.ErrorConstants;
import com.ceng453.gameClient.constants.NetworkConstants;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class RegisterController {
    /**
     * This method sends POST request to /api/register to add given credentials to the database.
     *
     * @param username Username of the current player
     * @param password Password of the current player
     * @return if successful returns empty string.
     *         if not returns Network Error or response error message.
     */
    public String register(String username, String password) {
        try {
            HttpResponse<String> response
                    = Unirest.post(NetworkConstants.API + "api/register")
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .body("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}")
                    .asString();
            if (response.getStatus() == 200) {
                return "";
            } else {
                return response.getBody();
            }
        } catch (Exception e) {
            return ErrorConstants.NETWORK_ERROR;
        }
    }
}
