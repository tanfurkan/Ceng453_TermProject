package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.constants.NetworkConstants;

import java.net.ServerSocket;
import java.net.Socket;

public class MultiPlayerController implements Runnable {

    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(NetworkConstants.LISTEN_PORT);
            serverSocket.setSoTimeout(30 * 60 * 1000); // 30 Minutes Timeout
            while (true) {
                Socket socketForPlayer1 = serverSocket.accept();
                System.out.println("First Player connected.");
                Socket socketForPlayer2 = serverSocket.accept();
                System.out.println("Second Player connected.");
                Thread newSession = new Thread(new GameSessionController(socketForPlayer1, socketForPlayer2));
                newSession.start();
            }
        } catch (Exception exception) {
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
