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

    /**
     * This constructor sets an Alien instance.
     *
     * @param xPos        X position of the alien
     * @param yPos        Y position of the alien
     * @param health      Health of the alien
     * @param score       Score that will be given when alien is dead
     * @param bulletSpeed Bullet speed of the alien
     * @param alienColor  Color and shading color of the alien
     * @param gameEngine  Game engine of the client
     */
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

    /**
     * This method is used for checking whether the alien is hit or not.
     * It decreases the health of the alien and kills it if alien is dead.
     */
    public void hitByBullet() {
        decrementHealth();
        if (isDead()) killAlien();
    }

    /**
     * This method is used for decrementing the health of the alien.
     */
    public void decrementHealth() {
        health--;
    }

    /**
     * This method is used for checking whether the alien is dead or not.
     *
     * @return whether the alien is dead or not
     */
    public boolean isDead() {
        return health < 1;
    }

    /**
     * This method is used for killing the alien.
     * It removes the alien from the screen and deletes
     * this instance from the list of aliens.
     * Finally it decrements the enemyCount on the game engine.
     */
    public void killAlien() {
        stopFire();
        gameEngine.removeElementFromScreen(enemyShip);
        gameEngine.getPlayer().addScore(score);
        gameEngine.getAlienList().remove(this);
        gameEngine.decrementEnemyCount();
    }

    /**
     * This method is used for creating timeline that will
     * create and fire bullets from the alien.
     */
    public void setUpFire() {
        int offSet = GameConstants.BULLET_RADIUS + GameConstants.ALIEN_RADIUS;
        double random = Math.random() * 6 + 1;
        fireBullet = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.ALIEN_BULLET_GENERATION_DURATION + random), e -> {
                    Bullet newBullet = new AlienBullet(enemyShip.getCenterX(), enemyShip.getCenterY() + offSet, bulletSpeed, gameEngine);
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
