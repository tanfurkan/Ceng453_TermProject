package com.ceng453.gameServer.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecordDAO {

    /**
     * The username of the record owner
     */
    private String username;

    /**
     * Score of the user
     */
    private Long score;

    /**
     * Time of the record creation
     */
    private Long date;

}
