package com.ceng453.gameClient.gameObjects.alien;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import com.ceng453.gameClient.gameObjects.bullet.AlienBullet;
import com.ceng453.gameClient.gameObjects.bullet.Bullet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import lombok.Getter;

@Getter
public abstract class Alien {

    private final Circle enemyShip;
    private int health;
    private final int score;
    private final double bulletSpeed;
    private Timeline fireBullet;
    private final GameEngine gameEngine;

    public Alien(double xPos, double yPos, int health, int score, double bulletSpeed, Color alienColor, GameEngine gameEngine) {
        enemyShip = new Circle(xPos, yPos, GameConstants.ALIEN_RADIUS);
        enemyShip.setStroke(alienColor);
        enemyShip.setEffect(new DropShadow(+25d, 0d, +2d, alienColor));
        this.health = health;
        this.score = score;
        this.bulletSpeed = bulletSpeed;
        this.gameEngine = gameEngine;
        setUpFire();

        gameEngine.addElementToScreen(enemyShip);
        gameEngine.getAlienList().add(this);

        startFire();

    }

    public void hitByBullet() {
        decrementHealth();
        if (isDead()) killAlien();
    }

    public void decrementHealth() {
        health--;
    }

    public boolean isDead() {
        return health < 1;
    }

    public void killAlien() {
        stopFire();
        gameEngine.removeElementFromScreen(enemyShip);
        gameEngine.getPlayer().addScore(score);
        gameEngine.getAlienList().remove(this);
        gameEngine.setEnemyCount(gameEngine.getEnemyCount()-1);
    }

    public void setUpFire() {
        int offSet = GameConstants.BULLET_RADIUS + GameConstants.ALIEN_RADIUS;
        fireBullet = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.ALIEN_BULLET_GENERATION_DURATION), e -> {
                    Bullet newBullet = new AlienBullet(enemyShip.getCenterX(), enemyShip.getCenterY() + offSet, bulletSpeed, gameEngine);
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
