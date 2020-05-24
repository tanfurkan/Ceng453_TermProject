package com.ceng453.gameServer.services;

import com.ceng453.gameServer.dao.NetworkDAO;
import org.springframework.stereotype.Service;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class MultiPlayerService {

    /**
     * Basic queue to keep track of users
     * waiting for another user and mathc them
     */
    Queue<NetworkDAO> playerWaitingQueue = new LinkedList<>();

    private final ReentrantLock mutex = new ReentrantLock();

    public NetworkDAO multiPlayer(NetworkDAO networkDAO) {
        try {
            mutex.lock();
            if (playerWaitingQueue.isEmpty()) {
                playerWaitingQueue.add(networkDAO);
                return new NetworkDAO(null, null, true);
            }
            return playerWaitingQueue.remove();

        } finally {
            mutex.unlock();
        }
    }

}
