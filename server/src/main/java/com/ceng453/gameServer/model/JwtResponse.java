package com.ceng453.gameServer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@ApiModel(description = "Details about the JWTResponse to login action.")
public class JwtResponse {

    @ApiModelProperty(notes = "The JWT Token")
    private final String jwt;
}
