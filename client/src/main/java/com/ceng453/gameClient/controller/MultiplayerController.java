package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.NetworkConstants;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MultiplayerController {

    private Socket socket;

    ObjectInputStream readFromServer;
    ObjectOutputStream sentToServer;

    public MultiplayerController() {
        try {
            socket = new Socket(NetworkConstants.MULTIPLAYER_IP, NetworkConstants.MULTIPLAYER_PORT);
            readFromServer = new ObjectInputStream(socket.getInputStream());
            sentToServer = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception exception) {
            System.err.println(exception.toString());
        }
    }

    public void sendIntroductionMessage() {
        try {
            sentToServer.writeObject(createIntroductionMessage());
        } catch (Exception exception){
            System.err.println(exception.toString());
        }
    }

    public void sendPositionInfo(int x, int y) {
        try {
            sentToServer.writeObject(createLocationMessage(x, y));
        } catch (Exception exception) {
            System.err.println(exception.toString());
        }
    }

    public void sendGameOver() {
        try {
            sentToServer.writeObject(createGameOverMessage());
        } catch (Exception exception) {
            System.err.println(exception.toString());
        }
    }

    public String createIntroductionMessage() {
        return NetworkConstants.INTRODUCTION_SIGNAL + "|" + GameConstants.username;
    }

    public String createLocationMessage(int x, int y) {
        return NetworkConstants.LOCATION_SIGNAL + "|" + x + "-" + y;
    }

    public String createGameOverMessage() {
        return NetworkConstants.GAME_END_SIGNAL + "| ";
    }

}
