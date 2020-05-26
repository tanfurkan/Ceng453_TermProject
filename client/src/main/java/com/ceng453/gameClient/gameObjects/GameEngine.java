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
import java.util.concurrent.atomic.AtomicBoolean;

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
    private String secondPlayerUsername = "";
    private Thread senderThread, receiverThread;
    private AtomicBoolean running = new AtomicBoolean(true);

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

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            if (enemyCount == 0) {
                cleanOldBullets();
                getLocalPlayer().incrementLevel();
                switch (getLocalPlayer().getLevel().getValue()) {
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
                        //wait for second player
                        playerList.add(new Player(this, 2));
                        createFifthLevel();
                        startCommunication();
                        //stopTheGame();
                        //SceneConstants.stage.setScene(EndOfGameScreen.createContent(true));
                        break;
                    case 6:
                        endTheGame(true);
                }
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
        leaderboardController.addRecord(GameConstants.username, getLocalPlayer().getScore().longValue());
        isGameActive = false;

        if(!secondPlayerUsername.isEmpty()){ // TODO MAY BE playerList.length > 1 ?
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
        if (isGameActive) {
            List<Alien> toRemove = newArrayList(alienList);
            for (Alien alien : toRemove) {
                alien.killAlien();
            }
        }
    }

    public void startMultiPlayer() {
        startCommunication();
    }

    public void endTheGame(boolean isWin) {
        stopTheGame();
        SceneConstants.stage.setScene(EndOfGameScreen.createContent(isWin));
    }

    public void startCommunication() {
        multiplayerController = new MultiplayerController();

        new Thread(() -> {
            multiplayerController.sendIntroductionMessage();
            handleReceivedMessage(multiplayerController.receiveMessage());

            // TODO REMOVE 3 LINES BELOW
            if (secondPlayerUsername.isEmpty()) {
                System.out.println("USERNAME ERROR");
            }
        });
    }

    public void startCommunicationThreads(){
        startSendingMessages();
        startReceivingMessages();
    }

    public void stopCommunicationThreads(){
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

        // TODO REMOVE LINE BELOW
        System.out.println("SIGNAL->" + signal + "PARAM->" + param);

        if (NetworkConstants.INTRODUCTION_SIGNAL.equals(signal)) {
            System.out.println(param);
            System.out.println(param);
            System.out.println(param);
            System.out.println(param);
            secondPlayerUsername = param;
        } else if(NetworkConstants.START_SIGNAL.equals(signal)){
            startCommunicationThreads();
        }
        else if (NetworkConstants.LOCATION_SIGNAL.equals(signal)) {
            String[] location = param.split(NetworkConstants.LOCATION_TOKEN);
            double x = Double.parseDouble(location[0]);
            double y = Double.parseDouble(location[1]);
            getSecondPlayer().updateSpaceShipPosition(x, y);
        } else if (NetworkConstants.GAME_END_SIGNAL.equals(signal)) {
            endTheGame(param.equals("1"));
        } else {
            System.out.println("UNKNOWN SIGNAL:" + signalAndParam);
        }
    }

    public void startReceivingMessages() {
        senderThread = new Thread(() -> {
            while(running.get()){
                handleReceivedMessage(multiplayerController.receiveMessage());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void startSendingMessages() {
        senderThread = new Thread(() -> {
            while(running.get()){
                multiplayerController.sendPositionInfo(getLocalPlayer().getSpaceShip().getCenterX(), getLocalPlayer().getSpaceShip().getCenterY());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Player getLocalPlayer() {
        return playerList.get(0);
    }

    private Player getSecondPlayer() {
        return playerList.get(1);
    }

}
