package com.ceng453.gameServer.dao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * JWT Response model that will be returned when a login action succeeds
 */
@Getter
@Setter
@RequiredArgsConstructor
@ApiModel(description = "Details about the JWTResponse to login action.")
public class JwtResponse {

    /**
     * Generated JWT token are stored here and returned with JwtResponse.
     */
    @ApiModelProperty(notes = "The JWT Token")
    private final String jwt;
}
