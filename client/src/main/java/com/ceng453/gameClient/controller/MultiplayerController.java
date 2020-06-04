package com.ceng453.gameClient.controller;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.NetworkConstants;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MultiplayerController {

    ObjectInputStream readFromServer;
    ObjectOutputStream sentToServer;

    /**
     * This is the constructor of the MultiplayerController. It starts the socket connection with server of the game.
     */
    public MultiplayerController() {
        try {
            Socket socket = new Socket(NetworkConstants.MULTIPLAYER_IP, NetworkConstants.MULTIPLAYER_PORT);
            sentToServer = new ObjectOutputStream(socket.getOutputStream());
            readFromServer = new ObjectInputStream(socket.getInputStream());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method receives messages from the game server.
     *
     * @return returns the received message as string
     */
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

    /**
     * This method sends introduction message to the game server for communicating with player 2.
     */
    public void sendIntroductionMessage() {
        try {
            sentToServer.writeObject(createIntroductionMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method sends position information message to the game server.
     * This message is sent to the player 2 on the game server side.
     */
    public void sendPositionInfo(double x, double y) {
        try {
            sentToServer.writeObject(createLocationMessage(x, y));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method sends game over message to the game server.
     * This message is sent to the player 2 on the game server side.
     */
    public void sendGameOver(boolean isWin, String score) {
        try {
            if (isWin) {
                sentToServer.writeObject(createGameOverMessage("1", score));
            } else {
                sentToServer.writeObject(createGameOverMessage("0", score));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method creates information message which will be sent to the game server.
     *
     * @return created introduction message
     */
    public String createIntroductionMessage() {
        return NetworkConstants.INTRODUCTION_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN +
                GameConstants.username;
    }

    /**
     * This method creates location message which will be sent to the game server.
     *
     * @return created location message
     */
    public String createLocationMessage(double x, double y) {
        return NetworkConstants.LOCATION_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN +
                x + NetworkConstants.PARAM_SEPARATOR_TOKEN + y;
    }

    /**
     * This method creates game over message which will be sent to the game server.
     *
     * @return created game over message
     */
    public String createGameOverMessage(String isWin, String score) {
        return NetworkConstants.GAME_END_SIGNAL +
                NetworkConstants.SIGNAL_PARAM_TOKEN +
                isWin + NetworkConstants.PARAM_SEPARATOR_TOKEN + score;
    }

}
