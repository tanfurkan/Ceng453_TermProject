package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.constants.NetworkConstants;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class GameSessionController implements Runnable {

    private final Socket socketForPlayer1;
    private final Socket socketForPlayer2;

    private final AtomicBoolean gameActive;
    private final AtomicBoolean firstEndMessage;

    ObjectInputStream readFromFirst, readFromSecond;
    ObjectOutputStream sendToFirst, sendToSecond;

    String messageFromFirst, messageFromSecond;

    GameSessionController(Socket player1, Socket player2) {
        gameActive = new AtomicBoolean(true);
        firstEndMessage = new AtomicBoolean(false);

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


            messageFromFirst = (String) readFromFirst.readObject();
            // System.out.println("FROM FIRST: " + messageFromFirst);
            handleInput(messageFromFirst, sendToSecond);

            messageFromSecond = (String) readFromSecond.readObject();
            // System.out.println("FROM SECOND: " + messageFromSecond);
            handleInput(messageFromSecond, sendToFirst);

            sleep(5000);
            notifyStart();
            sleep(100);

            communicationBridge();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void communicationBridge() {
        Thread firstToSecondPipe = new Thread(() -> {
            /* Duplicate */
            /* pipe(readFromFirst, sendToSecond); */
            while (gameActive.get()) {
                try {
                    messageFromFirst = (String) readFromFirst.readObject();
                    // System.out.println("FROM FIRST: " + messageFromFirst);
                    handleInput(messageFromFirst, sendToSecond);

                } catch (Exception exception) {
                    gameActive.set(false);
                    exception.printStackTrace();
                }
            }
        });
        firstToSecondPipe.start();

        Thread secondToFirstPipe = new Thread(() -> {
            /* Duplicate */
            /* pipe(readFromSecond, sendToFirst); */
            while (gameActive.get()) {
                try {
                    messageFromSecond = (String) readFromSecond.readObject();
                    // System.out.println("FROM SECOND: " + messageFromSecond);
                    handleInput(messageFromSecond, sendToFirst);
                } catch (Exception exception) {
                    gameActive.set(false);
                    exception.printStackTrace();
                }
            }
        });
        secondToFirstPipe.start();
    }


    /*    Duplicate code problem possible solution
    private void pipe(ObjectInputStream readFrom, ObjectOutputStream writeTo) {
        while (gameActive.get()) {
            try {
                String message = (String) readFrom.readObject();
                handleInput(message, writeTo);
            } catch (Exception exception) {
                gameActive.set(false);
                exception.printStackTrace();
            }
        }
    } */

    public void handleInput(String message, ObjectOutputStream sendToOther) {
        String[] signalAndParam;
        signalAndParam = message.split(NetworkConstants.SIGNAL_PARAM_TOKEN);
        if (NetworkConstants.GAME_END_SIGNAL.equalsIgnoreCase(signalAndParam[0])) {
            if (firstEndMessage.get()) {
                gameActive.set(false);
            }
            firstEndMessage.set(true);
        }

        try {
            sendToOther.writeObject(message);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void notifyStart() {
        try {
            String startMessage = createStartMessage();
            sendToFirst.writeObject(startMessage);
            sendToSecond.writeObject(startMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String createStartMessage() {
        return NetworkConstants.START_SIGNAL
                + NetworkConstants.SIGNAL_PARAM_TOKEN + " ";
    }
}
