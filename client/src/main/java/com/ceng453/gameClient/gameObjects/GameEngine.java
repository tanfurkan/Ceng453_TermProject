package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.constants.GameConstants;
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
                playerList.get(0).incrementLevel();
                switch (playerList.get(0).getLevel().getValue()) {
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
                        //stopTheGame();
                        //SceneConstants.stage.setScene(EndOfGameScreen.createContent(true));
                        break;
                    case 6:
                        stopTheGame();
                        SceneConstants.stage.setScene(EndOfGameScreen.createContent(true));
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
        addElementToScreen(playerList.get(0).getSpaceShip());

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

        for(Player player : playerList) {
            player.stopFire();
        }

        stopTrackingMouse();
        gameLoop.stop();
        leaderboardController.addRecord(GameConstants.username, playerList.get(0).getScore().longValue());
        isGameActive = false;
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
        GameScreen.bindHealth(playerList.get(0).getHealth());
        GameScreen.bindScore(playerList.get(0).getScore());
        GameScreen.bindLevel(playerList.get(0).getLevel());
    }

    /**
     * This method is used to start tracking the mouse movement.
     */
    private void startTrackingMouse() {
        gameScreen.setOnMouseMoved(e -> playerList.get(0).updateSpaceShipPosition(e.getX(), e.getY()));
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

    public void startCommunication() {
        multiplayerController = new MultiplayerController();
        multiplayerController.sendIntroductionMessage();
        
        startReceivingMessages();
        startSendingMessages();
    }

    public void startReceivingMessages() {

    }

    public void startSendingMessages() {

    }


}
