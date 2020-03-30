package com.ceng453.gameServer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ApiModel(description = "Details about the user")
public class User {

    /**
     * Uniquely generated Id to distinguish the users from each other
     */
    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The unique id of the user")
    private Long id;

    /**
     * Name of the user
     */
    @Column(unique = true)
    @ApiModelProperty(notes = "The unique username of the user")
    private String username;

    /**
     * Password of the user
     */
    @ApiModelProperty(notes = "The password of the user")
    private String password;

    /**
     * This flag is used to determine whether a user is deleted or not.
     * Until the user is deleted, it will be set to false as default.
     */
    @ApiModelProperty(notes = "The deletion flag of the user. If user is deleted, it will be set to true.")
    private boolean isDeleted = false;

    /***
     * This method turns User object to String representation
     * @return String consists of user's attributes
     */
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ",\"username\":\"" + username + '\"' +
                ",\"password\":\"" + password + '\"' +
                ",\"deleted\":" + isDeleted +
                '}';
    }
}
