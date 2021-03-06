package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.constants.NetworkConstants;

import java.net.ServerSocket;
import java.net.Socket;

public class MultiPlayerController implements Runnable {

    private ServerSocket serverSocket;
    private boolean exceptionFlag = true;

    /**
     * This method overrides the run function of Java Thread class.
     * It creates a sockets to start communicating with both players.
     * After the creation of the sockets, it starts a new session for
     * the currently connected players.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(NetworkConstants.LISTEN_PORT);
            serverSocket.setSoTimeout(30 * 60 * 1000); // 30 Minutes Timeout
            while (exceptionFlag) {
                Socket socketForPlayer1 = serverSocket.accept();
                System.out.println("First Player connected.");
                Socket socketForPlayer2 = serverSocket.accept();
                System.out.println("Second Player connected.");
                Thread newSession = new Thread(new GameSessionController(socketForPlayer1, socketForPlayer2));
                newSession.start();
            }
        } catch (Exception exception) {
            exceptionFlag = false;
            exception.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (Exception exception) {
                System.out.println("Communication Server could not closed.");
                exception.printStackTrace();
            }
        }
    }
}
