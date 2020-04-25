package com.ceng453.gameClient.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
