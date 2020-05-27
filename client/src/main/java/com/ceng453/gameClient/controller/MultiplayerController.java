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
            sentToServer = new ObjectOutputStream(socket.getOutputStream());
            readFromServer = new ObjectInputStream(socket.getInputStream());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String receiveMessage() {
        String message;
        try {
            message = (String) readFromServer.readObject();
            return message;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return NetworkConstants.ERROR_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN + " ";
    }

    public void sendIntroductionMessage() {
        try {
            sentToServer.writeObject(createIntroductionMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendPositionInfo(double x, double y) {
        try {
            sentToServer.writeObject(createLocationMessage(x, y));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendGameOver(String isWin) {
        try {
            sentToServer.writeObject(createGameOverMessage(isWin));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String createIntroductionMessage() {
        return NetworkConstants.INTRODUCTION_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN +
                GameConstants.username;
    }

    public String createLocationMessage(double x, double y) {
        return NetworkConstants.LOCATION_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN +
                x + NetworkConstants.LOCATION_TOKEN + y;
    }

    public String createGameOverMessage(String isWin) {
        return NetworkConstants.GAME_END_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN + isWin;
    }

}
