package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.controller.LeaderboardController;
import com.ceng453.gameClient.gameObjects.alien.Alien;
import com.ceng453.gameClient.gameObjects.alien.LevelOneAlien;
import com.ceng453.gameClient.gameObjects.alien.LevelThreeAlien;
import com.ceng453.gameClient.gameObjects.alien.LevelTwoAlien;
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
import java.util.Iterator;
import java.util.List;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;


@Getter
@Setter
public class GameEngine {

    private final Pane gameScreen;

    private Player player;
    private boolean isStopped = false;
    private List<Alien> alienList;
    private List<Bullet> bulletList;
    private int enemyCount = 0;
    private Timeline gameLoop;
    private LeaderboardController leaderboardController = new LeaderboardController();

    public GameEngine(Pane givenGameScreen) {
        gameScreen = givenGameScreen;
        player = new Player(this);

        alienList = new ArrayList<>();
        bulletList = new ArrayList<>();

        bindProperties();
        startTrackingMouse();

        createFirstLevel();

        gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            if (enemyCount == 0) {
                cleanOldBullets();
                player.incrementLevel();
                switch (player.getLevel().getValue()) {
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
                        stopTheGame();
                        SceneConstants.stage.setScene(EndOfGameScreen.createContent(true));
                }
            }
        }));

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    public void createFirstLevel() {
        addElementToScreen(player.getSpaceShip());

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

    public void addElementToScreen(Node node) {
        gameScreen.getChildren().add(node);
    }

    public void removeElementFromScreen(Node node) {
        gameScreen.getChildren().remove(node);
    }

    public void stopTheGame() {
        for (Alien alien : alienList
        ) {
            alien.stopFire();
        }
        for (Bullet bullet : bulletList) {
            bullet.stopMove();
        }

        player.stopFire();
        stopTrackingMouse();
        gameLoop.stop();
        leaderboardController.addRecord(GameConstants.username, player.getScore().longValue());
        isStopped = true;
    }

    public void cleanOldBullets() {
        for (Bullet bullet : bulletList)
            bullet.removeBulletFromScreen();
    }

    private void bindProperties() {
        GameScreen.bindHealth(player.getHealth());
        GameScreen.bindScore(player.getScore());
        GameScreen.bindLevel(player.getLevel());
    }

    private void startTrackingMouse() {
        gameScreen.setOnMouseMoved(e -> player.updateSpaceShipPosition(e.getX(), e.getY()));
    }

    private void stopTrackingMouse() {
        gameScreen.setOnMouseMoved(e -> {
        });
    }

    public void incrementEnemyCount() {
        setEnemyCount(getEnemyCount()+1);
    }

    public void decrementEnemyCount() {
        setEnemyCount(getEnemyCount()-1);
    }

    public void killAllActivated(){
        List<Alien> toRemove = newArrayList();
        player.stopFire();
        for (Alien alien : alienList) {
            removeElementFromScreen(alien.getEnemyShip());
            getPlayer().addScore(alien.getScore());
            toRemove.add(alien);
            decrementEnemyCount();
        }
        alienList.removeAll(toRemove);
        player.startFire();
    }


}
