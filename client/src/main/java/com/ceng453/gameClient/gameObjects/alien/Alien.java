package com.ceng453.gameClient.gameObjects.alien;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import com.ceng453.gameClient.gameObjects.bullet.AlienBullet;
import com.ceng453.gameClient.gameObjects.bullet.Bullet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public abstract class Alien {

    private final Circle enemyShip;
    private int health;
    private final int score;
    private Timeline fireBullet;
    private final GameEngine gameEngine;

    public Alien(double xPos, double yPos, int health, int score, Color alienColor, GameEngine gameEngine) {
        enemyShip = new Circle(xPos, yPos, GameConstants.ALIEN_RADIUS, alienColor);
        this.health = health;
        this.score = score;
        this.gameEngine = gameEngine;
        setUpFire();

        gameEngine.addElementToScreen(enemyShip);
        startFire();

    }

    public void decrementHealth() {
        health--;
    }

    public boolean isDead() {
        return health < 1;
    }

    public void kill() {
        stopFire();
        gameEngine.removeElementFromScreen(enemyShip);
        gameEngine.getPlayer().addScore(score);
    }

    public void setUpFire() {
        fireBullet = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.ALIEN_BULLET_DURATION), e -> {
                    Bullet newBullet = new AlienBullet(enemyShip.getCenterX(), enemyShip.getCenterY() + 23, 50, gameEngine);
                    gameEngine.getBulletList().add(newBullet);
                })
        );
        fireBullet.setCycleCount(Timeline.INDEFINITE);
    }

    public void startFire() {
        fireBullet.play();
    }

    public void stopFire() {
        fireBullet.stop();
    }
}
