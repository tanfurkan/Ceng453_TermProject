package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.alien.Alien;
import com.ceng453.gameClient.gameObjects.alien.LevelOneAlien;
import com.ceng453.gameClient.gameObjects.alien.LevelTwoAlien;
import com.ceng453.gameClient.gameObjects.bullet.Bullet;
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

    public GameEngine(Pane givenGameScreen) {
        gameScreen = givenGameScreen;
        player = new Player(this);

        alienList = new ArrayList<>();
        bulletList = new ArrayList<>();

        bindProperties();
        startTrackingMouse();

        createFirstLevel();
        Timeline gameLoop = new Timeline(new KeyFrame(Duration.seconds(0.5), e->{
            if(enemyCount < 1){
                int currentLevel = player.getLevel().getValue();
                switch (currentLevel){
                    case 1:
                        createSecondLevel();
                        player.incrementLevel();
                    case 2:
                        //creteThirdLevel;
                        player.incrementLevel();
                    case 3:
                        //createFourthLevel;
                        player.incrementLevel();
                    case 4:
                        stopTheGame();
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

        for(int i=0; i<2; i++) {
            for(int j=0; j<GameConstants.LEVEL_ONE_ALIEN_NUMBER_LINE; j++) {
                new LevelOneAlien(firstXPos + j*horizontalMargin, yPos + i*verticalMargin, this);
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

        for(int i=0; i<2; i++) {
            for(int j=0; j<GameConstants.LEVEL_TWO_ALIEN_NUMBER_LINE; j++) {
                int offset = j%2 == 0 ? 0 : 50;
                new LevelTwoAlien(firstXPos + j*horizontalMargin, yPos + i*verticalMargin + offset, this);
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
        isStopped = true;
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
