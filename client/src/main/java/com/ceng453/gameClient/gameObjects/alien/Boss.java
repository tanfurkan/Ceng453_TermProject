package com.ceng453.gameClient.gameObjects.alien;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import com.ceng453.gameClient.gameObjects.Player;
import com.ceng453.gameClient.gameObjects.bullet.AlienBullet;
import com.ceng453.gameClient.gameObjects.bullet.Bullet;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Boss extends Alien {
    /**
     * This constructor sets an Alien instance.
     *
     * @param xPos        X position of the alien
     * @param yPos        Y position of the alien
     * @param gameEngine  Game engine of the client
     */
    public Boss(double xPos, double yPos, GameEngine gameEngine) {
        super(xPos, yPos, GameConstants.BOSS_HEALTH, GameConstants.BOSS_SCORE, GameConstants.BOSS_BULLET_SPEED, GameConstants.BOSS_ALIEN_COLOR, gameEngine, true);
    }

    /**
     * This method is used for killing the alien.
     * It removes the alien from the screen and deletes
     * this instance from the list of aliens.
     * Finally it decrements the enemyCount on the game engine.
     */
    @Override
    public void killAlien() {
        stopFire();
        this.getGameEngine().removeElementFromScreen(this.getEnemyShip());
        this.getGameEngine().getAlienList().remove(this);
        this.getGameEngine().decrementEnemyCount();
    }

    /**
     * This method is used for checking whether the boss is hit or not.
     * It decreases the health of the boss and kills it if alien is boss.
     * It also adds point to the player that hit it.
     */
    public void hitByBullet(Player player) {
        decrementHealth();
        player.addScore(this.getScore());
        if (isDead()) killAlien();
    }

    /**
     * This method is used for creating timeline that will
     * create and fire bullets from the boss.
     */
    @Override
    public void setUpFire() {
        int offSet = GameConstants.BULLET_RADIUS + GameConstants.BOSS_RADIUS;
        double random = Math.random() * 6 + 1;
        this.setFireBullet(new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.BOSS_BULLET_GENERATION_DURATION + random), e -> {
                    Bullet newBullet = new AlienBullet(this.getEnemyShip().getCenterX(), this.getEnemyShip().getCenterY() + offSet, this.getBulletSpeed(), this.getGameEngine(), GameConstants.BOSS_BULLET_COLOR);
                    this.getGameEngine().getBulletList().add(newBullet);
                }))
        );
        this.getFireBullet().setCycleCount(Timeline.INDEFINITE);
    }
}
