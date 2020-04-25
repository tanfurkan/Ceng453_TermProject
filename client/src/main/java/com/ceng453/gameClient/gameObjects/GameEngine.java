package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
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
import java.util.List;


@Getter
@Setter
public class GameEngine {

    private final Pane gameScreen;

    private Player player;
    private boolean isStopped = false;
    private List<Alien> alienList;
    private List<Bullet> bulletList;
    public int enemyCount = 0;
    private Timeline gameLoop;

    public GameEngine(Pane givenGameScreen) {
        gameScreen = givenGameScreen;
        player = new Player(this);

        alienList = new ArrayList<>();
        bulletList = new ArrayList<>();

        bindProperties();
        startTrackingMouse();

        createFirstLevel();

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.5), e->{
        if (enemyCount == 0) {
                cleanOldBullets();
                player.incrementLevel();
                switch (player.getLevel().getValue()){
                    case 2:
                        createSecondLevel();
                        player.incrementLevel();
                        break;
                    case 3:
                        createThirdLevel();
                        player.incrementLevel();
                        break;
                    case 4:
                        createFourthLevel();
                        player.incrementLevel();
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
                enemyCount++;
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
                enemyCount++;
            }
        }
    }

    public void createThirdLevel() {

        double firstXPos = SceneConstants.WINDOW_WIDTH / 4.0;
        double lastXPos = SceneConstants.WINDOW_WIDTH * 0.75;
        double yPos = SceneConstants.WINDOW_HEIGHT / 4.0;
        double horizontalMargin = (lastXPos - firstXPos) / GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE;
        double verticalMargin = 100.0;

        for(int j=0; j<GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE; j++) {
            new LevelThreeAlien(firstXPos + j*horizontalMargin, yPos , this);
            enemyCount++;
        }
        for(int j=0; j<GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE; j++) {
            new LevelTwoAlien(firstXPos + j*horizontalMargin, yPos + verticalMargin , this);
            enemyCount++;
        }
        for(int j=0; j<GameConstants.LEVEL_THREE_ALIEN_NUMBER_LINE; j++) {
            new LevelOneAlien(firstXPos + j*horizontalMargin, yPos + 2*verticalMargin , this);
            enemyCount++;
        }
    }

    public void createFourthLevel() {

        double firstXPos = SceneConstants.WINDOW_WIDTH / 4.0;
        double lastXPos = SceneConstants.WINDOW_WIDTH * 0.75;
        double yPos = SceneConstants.WINDOW_HEIGHT / 4.0;
        double horizontalMargin = (lastXPos - firstXPos) / GameConstants.LEVEL_FOUR_ALIEN_NUMBER_LINE;
        double verticalMargin = 100.0;

        for (int j = 0; j < GameConstants.LEVEL_FOUR_ALIEN_NUMBER_LINE; j++) {
            int offset = j%2 == 0 ? 0 : 50;
            new LevelThreeAlien(firstXPos + j * horizontalMargin, yPos + offset, this);
            enemyCount++;
        }
        for(int i =0; i < 2; i++ ){
            for (int j = 0; j < GameConstants.LEVEL_FOUR_ALIEN_NUMBER_LINE; j++) {
                int offset = j%2 == 0 ? 0 : 50;
                new LevelTwoAlien(firstXPos + j * horizontalMargin, yPos + (i+1) * verticalMargin + offset, this);
                enemyCount++;
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

}
