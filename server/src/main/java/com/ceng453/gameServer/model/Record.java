package com.ceng453.gameServer.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@ApiModel(description = "Details about the record")
public class Record {

    // Uniquely generated Id to distinguish the record from each other
    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The unique id of the record")
    private Long id;

    // Score of the user
    @ApiModelProperty(notes = "The score of the record")
    private Long score;

    // Time of the record creation
    // To get time System.currentTimeMillis() will be used
    // This enable us to easily operate on
    @ApiModelProperty(notes = "The date of the record")
    private Long date;

    // ID of the user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ApiModelProperty(notes = "The owner of the record")
    User user;

}
