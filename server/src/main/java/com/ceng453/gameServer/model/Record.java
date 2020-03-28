package com.ceng453.gameServer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class Record {

    // Uniquely generated Id to distinguish the record from each other
    @Id
    @GeneratedValue
    private Long id;

    // Score of the user
    private Long score;

    // Time of the record creation
    // To get time System.currentTimeMillis() will be used
    // This enable us to easily operate on
    private Long date;

    // ID of the user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}
