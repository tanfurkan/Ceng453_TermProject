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

    /**
     * This constructor sets an Player instance.
     * This instance initializes the sprite and class
     * properties for player.
     * @param gameEngine Game engine of the client
     */
    public Player(GameEngine gameEngine) {
        spaceShip = new Circle(SceneConstants.PLAYER_SPACESHIP_INITIAL_X, SceneConstants.PLAYER_SPACESHIP_INITIAL_Y, 20, Color.AQUA);
        health = new SimpleIntegerProperty(GameConstants.PLAYER_INITIAL_HEALTH);
        this.gameEngine = gameEngine;
        setUpFire();
        startFire();
    }

    /**
     * This method is used for updating the position of the player.
     * @param x New X position of player
     * @param y New Y position of player
     */
    public void updateSpaceShipPosition(double x, double y) {
        /* Player can go 2/3 of width only */
        spaceShip.setCenterX(Math.min(Math.max(SceneConstants.PLAYER_X_MIN, x), SceneConstants.PLAYER_X_MAX));
        spaceShip.setCenterY(Math.min(Math.max(SceneConstants.PLAYER_Y_MIN, y), SceneConstants.PLAYER_Y_MAX));
    }

    /**
     * This method is used for incrementing the level of the player.
     */
    public void incrementLevel() {
        level.setValue(level.getValue() + 1);
    }

    /**
     * This method is used for decrementing the health of the player.
     */
    private void decrementHealth() {
        health.setValue(health.getValue() - 1);
    }

    /**
     * This method is used for checking whether the player is dead or not.
     * @return whether the player is dead or not
     */
    private boolean isDead() {
        return health.getValue() < 1;
    }

    /**
     * This method is used for checking whether the player is hit or not.
     * It decreases the health of the player and ends the game if player is dead.
     */
    public void hitByBullet() {
        decrementHealth();
        if (isDead()) {
            gameEngine.stopTheGame();
            SceneConstants.stage.setScene(EndOfGameScreen.createContent(false));
        }
    }

    /**
     * This method is used for adding points to the score of the player.
     * @param newPoints Point that will be added to score of the player
     */
    public void addScore(int newPoints) {
        score.setValue(score.getValue() + newPoints);
    }

    /**
     * This method is used for creating timeline that will
     * create and fire bullets from the player.
     */
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

    /**
     * This method is used to start firing bullets from the player.
     */
    public void startFire() {
        fireBullet.play();
    }

    /**
     * This method is used to stop firing bullets from the player.
     */
    public void stopFire() {
        fireBullet.stop();
    }

}
