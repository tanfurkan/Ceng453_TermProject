package com.ceng453.gameServer.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecordDAO {

    /**
     * The username of the record owner
     */
    private final String username;

    /**
     * Score of the user
     */
    private final Long score;

    /**
     * Time of the record creation
     */
    private final Long date;

}
