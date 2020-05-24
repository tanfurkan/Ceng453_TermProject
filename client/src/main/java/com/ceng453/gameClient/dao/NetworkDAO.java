package com.ceng453.gameClient.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NetworkDAO {
    /**
     * The IP to other user can connect
     */
    private final String IP;

    /**
     * The PORT to other user can connect
     */
    private final Long PORT;


    /**
     * The boolean to decide whether client going to host or not
     */
    private final boolean isHost;
}
