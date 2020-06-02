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

    /**
     * This is the constructor of GameSessionController.
     * It sets the given sockets to player 1 and 2.
     */
    GameSessionController(Socket player1, Socket player2) {
        gameActive = new AtomicBoolean(true);
        firstEndMessage = new AtomicBoolean(false);

        socketForPlayer1 = player1;
        socketForPlayer2 = player2;
    }

    /**
     * This method overrides the run function of Java Thread class.
     * It creates a communication between player 1 and 2. It introduces
     * the players to each other and creates pipes that let them communicate via server.
     */
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

    /**
     * This method starts the communication pipes between player 1 and 2.
     */
    public void communicationBridge() {
        Thread firstToSecondPipe = new Thread(() -> pipe(readFromFirst, sendToSecond));
        firstToSecondPipe.start();

        Thread secondToFirstPipe = new Thread(() -> pipe(readFromSecond, sendToFirst));
        secondToFirstPipe.start();
    }


    /**
     * This method creates a thread called pipe that is used to
     * receive messages from other player and send messages to other player.
     *
     * @param readFrom holds which stream the thread will receive the message from
     * @param writeTo  holds which stream the thread will send the message to
     */
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
    }

    /**
     * This method handles the input that is received from other player.
     * Depending on the message's type, it will send it directly to output stream
     * or it will first initiate end messaging sequence.
     *
     * @param message     received message from other player
     * @param sendToOther holds which stream the thread will send the response to
     */
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

    /**
     * This method creates a start message for both player sockets to receive
     * and start their games.
     */
    public void notifyStart() {
        try {
            String startMessage = createStartMessage();
            sendToFirst.writeObject(startMessage);
            sendToSecond.writeObject(startMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * This method is used to create game start message to acknowledge both
     * players are connected and ready to start playing final level.
     */
    public String createStartMessage() {
        return NetworkConstants.START_SIGNAL
                + NetworkConstants.SIGNAL_PARAM_TOKEN + " ";
    }
}
