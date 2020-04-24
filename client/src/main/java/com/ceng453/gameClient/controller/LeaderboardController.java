package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.NetworkConstants;
import com.ceng453.gameClient.dao.RecordDAO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.Optional;

public class LeaderboardController {
    public LeaderboardController(){
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
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
        Unirest.setDefaultHeader("Authorization", "Bearer "+NetworkConstants.jwtToken);
    }
    public Optional<RecordDAO[]> getRecords(String pageLimit, String timeRange){
        try {
            HttpResponse<RecordDAO[]> response;
            response = Unirest.get(NetworkConstants.API + "api/leaderboard_" + timeRange + "/")
                    .queryString("pageLimit", pageLimit)
                    .asObject(RecordDAO[].class);
            return Optional.ofNullable(response.getBody());
        }
        catch (UnirestException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
