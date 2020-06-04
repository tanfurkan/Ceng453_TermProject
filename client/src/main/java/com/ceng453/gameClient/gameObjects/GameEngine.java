package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.NetworkConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.controller.LeaderboardController;
import com.ceng453.gameClient.controller.MultiplayerController;
import com.ceng453.gameClient.gameObjects.alien.*;
import com.ceng453.gameClient.gameObjects.bullet.Bullet;
import com.ceng453.gameClient.scenes.EndOfGameScreen;
import com.ceng453.gameClient.scenes.GameScreen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;
import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;


@Getter
@Setter
public class GameEngine {

    private final Pane gameScreen;

    private List<Player> playerList;
    private boolean isGameActive;
    private List<Alien> alienList;
    private List<Bullet> bulletList;
    private int enemyCount = 0;
    private Timeline gameLoop;
    private LeaderboardController leaderboardController = new LeaderboardController();

    private MultiplayerController multiplayerController;
    private Thread senderThread, receiverThread;

    private String secondPlayerUsername = "";
    private String secondPlayerScore = "";

    private AtomicBoolean running = new AtomicBoolean(true);
    private AtomicBoolean waitingMultiplayer = new AtomicBoolean(true);
    private AtomicBoolean onGameEnd = new AtomicBoolean(false);
    private AtomicBoolean gameEndStatus = new AtomicBoolean(false);

    private AtomicBoolean showPlayerFound = new AtomicBoolean(false);
    private AtomicBoolean onStart = new AtomicBoolean(false);

    private Semaphore waitEndSignal = new Semaphore(0);

    private int stage; /* Different than user level */
    private boolean stageUpdate = false;

    /**
     * This constructor sets an GameEngine instance.
     * This instance initializes the game for player,
     * creates first level and sets the main game loop.
     * Game loop is used for level transition and ending game.
     *
     * @param givenGameScreen Game screen of the client
     */
    public GameEngine(Pane givenGameScreen) {
        gameScreen = givenGameScreen;
        playerList = new ArrayList<>();
        playerList.add(new Player(this, 1));

        alienList = new ArrayList<>();
        bulletList = new ArrayList<>();

        isGameActive = true;

        bindProperties();
        startTrackingMouse();

        createFirstLevel();
        stage = 2;

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            if (onGameEnd.get()) {
                endTheGame();
            }
            if (onStart.get()) {
                startCommunicationThreads();
                onStart.set(false);
                GameScreen.clearMessages();

                getSecondPlayer().startFire();
                getLocalPlayer().startFire();
                createFifthLevel();
            }
            if (showPlayerFound.get()) {
                showPlayerFound.set(false);
                GameScreen.showUserFound(secondPlayerUsername);
            }
            // TODO LINE BELOW CAN CHANGE BUT SHOULD BE TESTED AFTER CHANGE
            // if(enemyCount == 0) stageUpdate = true;
            if (enemyCount == 0 && getLocalPlayer().getLevel().getValue() < 5) stageUpdate = true;
            if (stageUpdate) {
                getLocalPlayer().stopFire();
                cleanOldBullets();
                switch (stage) {
                    case 2:
                        createSecondLevel();
                        break;
                    case 3:
                        createThirdLevel();
                        break;
                    case 4:
                        createFourthLevel();
                        break;
                    case 5:
                        GameScreen.showWaitingMessage();
                        playerList.add(new Player(this, 2));
                        getSecondPlayer().stopFire();
                        startMultiPlayer();
                        break;
                }
                stage++;
                stageUpdate = false;
                if (getLocalPlayer().getLevel().getValue() < 4) getLocalPlayer().startFire();
                if (getLocalPlayer().getLevel().getValue() < 5) getLocalPlayer().incrementLevel();
            }
        }));

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    /**
     * This method is used for creating the first level.
     * It creates the aliens and sets them on the game screen.
     */
    public void createFirstLevel() {
        addElementToScreen(getLocalPlayer().getSpaceShip());

        double firstXPos = SceneConstants.WINDOW_WIDTH / 4.0;
        double lastXPos = SceneConstants.WINDOW_WIDTH * 0.75;
        double yPos = SceneConstants.WINDOW_HEIGHT / 4.0;
        double horizontalMargin = (lastXPos - firstXPos) / GameConstants.LEVEL_ONE_ALIEN_NUMBER_LINE;
        double verticalMargin = 100.0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < GameConstants.LEVEL_ONE_ALIEN_NUMBER_LINE; j++) {
                new LevelOneAlien(firstXPos + j * horizontalMargin, yPos + i * verticalMargin, this);
                incrementEnemyCount();
            }
        }
    }

    /**
     * This method is used for creating the second level.
     * It creates the aliens and sets them on the game screen.
     */
    public void createSecondLevel() {

        double firstXPos = SceneConstants.WINDOW_WIDTH / 4.0;
        double lastXPos = SceneConstants.WINDOW_WIDTH * 0.75;
        double yPos = SceneConstants.WINDOW_HEIGHT / 4.0;
        double horizontalMargin = (lastXPos - firstXPos) / GameConstants.LEVEL_ONE_ALIEN_NUMBER_LINE;
        double verticalMargin = 100.0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < GameConstants.LEVEL_TWO_ALIEN_NUMBER_LINE; j++) {
                int offset = j % 2 == 0 ? 0 : 50;
                new LevelTwoAlien(firstXPos + j * horizontalMargin, yPos + i * verticalMargin + offset, this);
                incrementEnemyCount();
            }
        }
    }

    /**
     * This method is used for creating the third level.
     * It creates the aliens and sets them on the game screen.
     */
    public void createThirdLevel() {

        double firstXPos = SceneConstants.WINDOW_WIDTH / 4.0;
        double lastXPos = SceneConstants.WINDOW_WIDTH * 0.75;
        double yPos = SceneConstants.WINDOW_HEIGHT / 4.0;
        double horizontalMargin = (lastXPos - firstXPos) / GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE;
        double verticalMargin = 100.0;

        for (int j = 0; j < GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE; j++) {
            new LevelThreeAlien(firstXPos + j * horizontalMargin, yPos, this);
            incrementEnemyCount();
        }
        for (int j = 0; j < GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE; j++) {
            new LevelTwoAlien(firstXPos + j * horizontalMargin, yPos + verticalMargin, this);
            incrementEnemyCount();
        }
        for (int j = 0; j < GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE; j++) {
            new LevelOneAlien(firstXPos + j * horizontalMargin, yPos + 2 * verticalMargin, this);
            incrementEnemyCount();
        }
    }

    /**
     * This method is used for creating the fourth level.
     * It creates the aliens and sets them on the game screen.
     */
    public void createFourthLevel() {

        double firstXPos = SceneConstants.WINDOW_WIDTH / 4.0;
        double lastXPos = SceneConstants.WINDOW_WIDTH * 0.75;
        double yPos = SceneConstants.WINDOW_HEIGHT / 4.0;
        double horizontalMargin = (lastXPos - firstXPos) / GameConstants.LEVEL_FOUR_ALIEN_NUMBER_LINE;
        double verticalMargin = 100.0;

        for (int j = 0; j < GameConstants.LEVEL_FOUR_ALIEN_NUMBER_LINE; j++) {
            int offset = j % 2 == 0 ? 0 : 50;
            new LevelThreeAlien(firstXPos + j * horizontalMargin, yPos + offset, this);
            incrementEnemyCount();
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < GameConstants.LEVEL_FOUR_ALIEN_NUMBER_LINE; j++) {
                int offset = j % 2 == 0 ? 0 : 50;
                new LevelTwoAlien(firstXPos + j * horizontalMargin, yPos + (i + 1) * verticalMargin + offset, this);
                incrementEnemyCount();
            }
        }

    }

    /**
     * This method is used for creating the fifth level.
     * It creates the aliens and sets them on the game screen.
     */
    public void createFifthLevel() {
        addElementToScreen(playerList.get(1).getSpaceShip());

        double xPos = SceneConstants.WINDOW_WIDTH / 2.0;
        double yPos = SceneConstants.WINDOW_HEIGHT / 3.0;
        new Boss(xPos, yPos, this);
        incrementEnemyCount();
    }

    /**
     * This method is used for adding nodes to the game screen.
     *
     * @param node Node that will be added to game screen.
     */
    public void addElementToScreen(Node node) {
        gameScreen.getChildren().add(node);
    }

    /**
     * This method is used for removing nodes from the game screen.
     *
     * @param node Node that will be deleted from game screen.
     */
    public void removeElementFromScreen(Node node) {
        gameScreen.getChildren().remove(node);
    }

    /**
     * This method is used to end the current game.
     * It ends the current game and sends the last score of player
     * to the game server.
     */
    public void stopTheGame() {
        for (Alien alien : alienList
        ) {
            alien.stopFire();
        }
        for (Bullet bullet : bulletList) {
            bullet.stopMove();
        }

        for (Player player : playerList) {
            player.stopFire();
        }

        stopTrackingMouse();
        gameLoop.stop();
        isGameActive = false;

        if (playerList.size() > 1) {
            /* This waits for end signal together with secondUser point */
            try {
                waitEndSignal.acquire();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            /* Submit Score to Backend */
            if (getLocalPlayer().getScore().longValue() >= Long.parseLong(secondPlayerScore)) {
                leaderboardController.addRecord(GameConstants.username, getLocalPlayer().getScore().longValue() + GameConstants.bonusPoint);
            } else leaderboardController.addRecord(GameConstants.username, getLocalPlayer().getScore().longValue());

            stopCommunicationThreads();
        }
    }

    /**
     * This method is used to clear the bullets on the game screen.
     * It is generally used on level transitions.
     */
    public void cleanOldBullets() {
        for (Bullet bullet : bulletList)
            bullet.removeBulletFromScreen();
    }

    /**
     * This method is used to bind the current health,
     * score and level of the player to the game screen.
     */
    private void bindProperties() {
        GameScreen.bindHealth(getLocalPlayer().getHealth());
        GameScreen.bindScore(getLocalPlayer().getScore());
        GameScreen.bindLevel(getLocalPlayer().getLevel());
    }

    /**
     * This method is used to start tracking the mouse movement.
     */
    private void startTrackingMouse() {
        gameScreen.setOnMouseMoved(e -> getLocalPlayer().updateSpaceShipPosition(e.getX(), e.getY()));
    }

    /**
     * This method is used to stop tracking the mouse movement.
     */
    private void stopTrackingMouse() {
        gameScreen.setOnMouseMoved(e -> {
        });
    }

    /**
     * This method is used to increment the enemyCount.
     */
    public void incrementEnemyCount() {
        enemyCount++;
    }

    /**
     * This method is used to decrement the enemyCount.
     */
    public void decrementEnemyCount() {
        enemyCount--;
    }

    /**
     * This method is used to kill all aliens on the
     * current level. This method will be activated if the cheat combo
     * is pressed on keyboard.
     */
    public void killAllActivated() {
        // TODO CODE BELOW LIMITS THE CHEAT LEVEL
        // TO REMOVE THAT CHANGE WITH THE LINE BELOW
        // if(isGameActive) {
        if (isGameActive && getLocalPlayer().getLevel().getValue() < GameConstants.CHEAT_END_LEVEL) {
            List<Alien> toRemove = newArrayList(alienList);
            for (Alien alien : toRemove) {
                alien.killAlien();
            }
        }
    }

    public void startMultiPlayer() {
        startCommunication();
    }

    public void endTheGame() {
        stopTheGame();
        SceneConstants.stage.setScene(EndOfGameScreen.createContent(
                gameEndStatus.get(), secondPlayerUsername, getLocalPlayer().getScore().getValue().toString(), secondPlayerScore));
    }

    public void startCommunication() {
        Thread gamePreparation = new Thread(() -> {
            multiplayerController = new MultiplayerController();
            multiplayerController.sendIntroductionMessage();
            handleReceivedMessage(multiplayerController.receiveMessage()); /* Handle Introduction Message */
            handleReceivedMessage(multiplayerController.receiveMessage()); /* Handle Start Message */
        });
        gamePreparation.start();
    }

    public void startCommunicationThreads() {
        waitingMultiplayer.set(false);
        startSendingMessages();
        startReceivingMessages();
    }

    public void stopCommunicationThreads() {
        running.set(false);
        receiverThread.interrupt();
        senderThread.interrupt();
    }

    public void handleReceivedMessage(String receivedMessage) {
        String signal, param;
        String[] signalAndParam;

        signalAndParam = receivedMessage.split(NetworkConstants.SIGNAL_PARAM_TOKEN);

        signal = signalAndParam[0];
        param = signalAndParam[1];

        if (NetworkConstants.INTRODUCTION_SIGNAL.equals(signal)) {
            secondPlayerUsername = param;
            showPlayerFound.set(true);
        } else if (NetworkConstants.START_SIGNAL.equals(signal)) {
            onStart.set(true);
        } else if (NetworkConstants.LOCATION_SIGNAL.equals(signal)) {
            String[] location = param.split(NetworkConstants.PARAM_SEPARATOR_TOKEN);
            getSecondPlayer().updateSpaceShipPosition(Double.parseDouble(location[0]), Double.parseDouble(location[1]));
        } else if (NetworkConstants.GAME_END_SIGNAL.equals(signal)) {
            String[] gameEnd = param.split(NetworkConstants.PARAM_SEPARATOR_TOKEN);
            secondPlayerScore = gameEnd[1];
            multiplayerController.sendGameOver(gameEnd[0].equals("1"), getLocalPlayer().getScore().getValue().toString());
            gameEndStatus.set(gameEnd[0].equals("1"));
            waitEndSignal.release();
            onGameEnd.set(true);
        } else {
            System.out.println("UNKNOWN SIGNAL:" + receivedMessage);
            // TODO THIS CAUSE INFINITE LOOP MAY BE CLOSE THE GAME ? CODE BELOW
//            waitEndSignal.release();
//            onGameEnd.set(true);
        }
    }

    public void startReceivingMessages() {
        receiverThread = new Thread(() -> {
            while (running.get()) {
                handleReceivedMessage(multiplayerController.receiveMessage());
            }
        });
        receiverThread.start();
    }

    public void startSendingMessages() {
        senderThread = new Thread(() -> {
            while (running.get()) {
                multiplayerController.sendPositionInfo(getLocalPlayer().getSpaceShip().getCenterX(), getLocalPlayer().getSpaceShip().getCenterY());
                try {
                    sleep(NetworkConstants.SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        senderThread.start();

    }

    public Player getLocalPlayer() {
        return playerList.get(0);
    }

    public Player getSecondPlayer() {
        return playerList.get(1);
    }

}
