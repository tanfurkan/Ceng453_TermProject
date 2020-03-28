package com.ceng453.gameServer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ApiModel(description = "Details about the user")
public class User {
    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The unique id of the user")
    private Long id;

    @Column(unique = true)
    @ApiModelProperty(notes = "The unique username of the user")
    private String username;

    @ApiModelProperty(notes = "The password of the user")
    private String password;

    @ApiModelProperty(notes = "The deletion flag of the user. If user is deleted, it will be set to true.")
    private boolean isDeleted = false;

}
