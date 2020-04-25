package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.bullet.Bullet;
import com.ceng453.gameClient.gameObjects.bullet.PlayerBullet;
import com.ceng453.gameClient.scenes.EndOfGameScreen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {

    private Circle spaceShip;
    private SimpleIntegerProperty health;
    private SimpleIntegerProperty score = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty level = new SimpleIntegerProperty(1);
    private Timeline fireBullet;
    private GameEngine gameEngine;

    public Player(GameEngine gameEngine) {
        spaceShip = new Circle(SceneConstants.PLAYER_SPACESHIP_INITIAL_X, SceneConstants.PLAYER_SPACESHIP_INITIAL_Y, 20, Color.AQUA);
        health = new SimpleIntegerProperty(GameConstants.PLAYER_INITIAL_HEALTH);
        this.gameEngine = gameEngine;
        setUpFire();
        startFire();
    }

    public void updateSpaceShipPosition(double x, double y) {
        /* Player can go 2/3 of width only */
        spaceShip.setCenterX(Math.min(Math.max(SceneConstants.PLAYER_X_MIN, x), SceneConstants.PLAYER_X_MAX));
        spaceShip.setCenterY(Math.min(Math.max(SceneConstants.PLAYER_Y_MIN, y), SceneConstants.PLAYER_Y_MAX));
    }

    public void incrementLevel() { // TODO ADD MEMBER FUNCTION WONT WORK WHY?
        level.setValue(level.getValue() + 1);
    }

    private void decrementHealth() {
        health.setValue(health.getValue() - 1);
    }

    private boolean isDead() {
        return health.getValue() < 1;
    }

    public void hitByBullet() {
        decrementHealth();
        if (isDead()){
            gameEngine.stopTheGame();
            SceneConstants.stage.setScene(EndOfGameScreen.createContent(false));
        }
    }

    public void addScore(int newPoints) {
        score.setValue(score.getValue() + newPoints);
    }

    public void setUpFire() {
        int offSet = GameConstants.BULLET_RADIUS + GameConstants.PLAYER_RADIUS;
        fireBullet = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.PLAYER_BULLET_GENERATION_DURATION), e -> {
                    Bullet newBullet = new PlayerBullet(spaceShip.getCenterX(), spaceShip.getCenterY() - offSet, gameEngine);
                    gameEngine.getBulletList().add(newBullet);
                })
        );
        fireBullet.setCycleCount(Timeline.INDEFINITE);
    }

    public void startFire() {
        fireBullet.play();
    }

    public void pauseFire() {
        fireBullet.pause();
    }

    public void stopFire() {
        fireBullet.stop();
    }

}
