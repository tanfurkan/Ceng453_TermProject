package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.NetworkConstants;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MultiplayerController {

    ObjectInputStream readFromServer;
    ObjectOutputStream sentToServer;

    public MultiplayerController() {
        try {
            Socket socket = new Socket(NetworkConstants.MULTIPLAYER_IP, NetworkConstants.MULTIPLAYER_PORT);
            readFromServer = new ObjectInputStream(socket.getInputStream());
            sentToServer = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception exception) {
            System.err.println(exception.toString());
        }
    }

    public String receiveMessage() {
        String message;
        try {
            message = (String) readFromServer.readObject();
            return message;
        } catch (Exception exception) {
            System.err.println(exception.toString());
        }
        return NetworkConstants.ERROR_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN + " ";
    }

    public void sendIntroductionMessage() {
        try {
            sentToServer.writeObject(createIntroductionMessage());
        } catch (Exception exception) {
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

    public void sendGameOver(String isWin) {
        try {
            sentToServer.writeObject(createGameOverMessage(isWin));
        } catch (Exception exception) {
            System.err.println(exception.toString());
        }
    }

    public String createIntroductionMessage() {
        return NetworkConstants.INTRODUCTION_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN +
                GameConstants.username;
    }

    public String createLocationMessage(int x, int y) {
        return NetworkConstants.LOCATION_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN +
                x + NetworkConstants.LOCATION_TOKEN + y;
    }

    public String createGameOverMessage(String isWin) {
        return NetworkConstants.GAME_END_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN + isWin;
    }

}
