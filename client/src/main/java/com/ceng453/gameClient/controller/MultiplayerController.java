package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.ErrorConstants;
import com.ceng453.gameClient.constants.NetworkConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import javafx.util.Pair;

import java.io.IOException;

public class MultiplayerController {
    public MultiplayerController(){
        Unirest.setObjectMapper(new ObjectMapper() {
            private final com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Unirest.setDefaultHeader("accept", "application/json");
        Unirest.setDefaultHeader("Content-Type", "application/json");
        Unirest.setDefaultHeader("Authorization", "Bearer " + NetworkConstants.jwtToken);
    }
    public String connect(String host, String port) {
        try {
            HttpResponse<Pair<?,?>> response
                    = Unirest.post(NetworkConstants.API + "api/multiplayer")
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .body("{\"host\":\"" + host + "\", \"port\":\"" + port + "\"}")
                    .asObject(Pair.class);
            if (response.getStatus() == 200) {
                NetworkConstants.MULTIPLAYER_HOST = response.getBody().toString();
                return "";
            } else if (response.getStatus() == 403) {
                return ErrorConstants.BAD_CREDENTIALS_ERROR;
            } else
                return response.getBody().toString();

        } catch (Exception e) {
            return ErrorConstants.NETWORK_ERROR;
        }

    }
}
