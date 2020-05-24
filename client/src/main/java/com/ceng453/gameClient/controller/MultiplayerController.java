package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.NetworkConstants;
import com.ceng453.gameClient.dao.NetworkDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

import java.io.IOException;

public class MultiplayerController {
    public MultiplayerController() {
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

    public NetworkDAO connect(String IP, String PORT) {
        try {
            HttpResponse<NetworkDAO> response
                    = Unirest.post(NetworkConstants.API + "api/multiplayer")
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .body("{\"ip\":\"" + IP + "\", \"port\":\"" + PORT + "\", \"isHost\": false}")
                    .asObject(NetworkDAO.class);
            if (response.getStatus() == 200) {
                return response.getBody();
            } else if (response.getStatus() == 403) {
                return null;
            } else
                return null;

        } catch (Exception e) {
            return null;
        }
    }
}
