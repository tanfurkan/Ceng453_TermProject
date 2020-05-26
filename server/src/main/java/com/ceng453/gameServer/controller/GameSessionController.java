package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.constants.NetworkConstants;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameSessionController implements Runnable {

    private final Socket socketForPlayer1;
    private final Socket socketForPlayer2;

    private boolean gameActive;

    ObjectInputStream readFromFirst, readFromSecond;
    ObjectOutputStream sendToFirst, sendToSecond;

    GameSessionController(Socket player1, Socket player2) {
        gameActive = true;

        socketForPlayer1 = player1;
        socketForPlayer2 = player2;
    }

    @Override
    public void run() {
        try {
            sendToFirst = new ObjectOutputStream(socketForPlayer1.getOutputStream());
            sendToSecond = new ObjectOutputStream(socketForPlayer2.getOutputStream());

            readFromFirst = new ObjectInputStream(socketForPlayer1.getInputStream());
            readFromSecond = new ObjectInputStream(socketForPlayer2.getInputStream());



            communicationBridge(); /* Send username to other player */

            notifyStart();

            while (gameActive) {
                communicationBridge();
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void communicationBridge() {
        try {
            String messageFromFirst, messageFromSecond;
            messageFromFirst = (String) readFromFirst.readObject();
            System.out.println("FROM FIRST: " + messageFromFirst);
            handleInput(messageFromFirst, sendToSecond);

            messageFromSecond = (String) readFromSecond.readObject();
            System.out.println("FROM SECOND: " + messageFromSecond);
            handleInput(messageFromSecond, sendToFirst);
        } catch (Exception e) {
            System.err.println(e.toString());
            gameActive = false;
        }

    }

    public void handleInput(String message, ObjectOutputStream sendToOther) {
        String[] signalAndParam;
        signalAndParam = message.split(NetworkConstants.SIGNAL_PARAM_TOKEN);
        if (NetworkConstants.GAME_END_SIGNAL.equalsIgnoreCase(signalAndParam[0])) {
            gameActive = false;
        }

        try {
            sendToOther.writeObject(message);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void notifyStart() {
        try {
            String startMessage = createStartMessage();
            sendToFirst.writeObject(startMessage);
            sendToSecond.writeObject(startMessage);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public String createStartMessage() {
        return NetworkConstants.START_SIGNAL
                + NetworkConstants.SIGNAL_PARAM_TOKEN + " ";
    }
}
