package com.ceng453.gameServer.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RecordDAO {

    private String username;
    private Long score;
    private Long date;

}
