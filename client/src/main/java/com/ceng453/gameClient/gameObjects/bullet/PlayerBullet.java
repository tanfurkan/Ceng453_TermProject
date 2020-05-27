package com.ceng453.gameClient.gameObjects.bullet;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import com.ceng453.gameClient.gameObjects.Player;
import com.ceng453.gameClient.gameObjects.alien.Alien;
import com.ceng453.gameClient.gameObjects.alien.Boss;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PlayerBullet extends Bullet {

    protected Player owner;

    public PlayerBullet(double xPos, double yPos, GameEngine gameEngine, Player player, Color bulletColor) {
        super(xPos, yPos, bulletColor, gameEngine);
        this.bulletSpeed = GameConstants.PLAYER_BULLET_SPEED;
        this.owner = player;
        setUpBulletMove();
        gameEngine.addElementToScreen(bullet);
        startMove();
    }


    @Override
    public void setUpBulletMove() {
        bulletMove = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.PLAYER_BULLET_MOVE_DURATION), e -> {
                    bullet.setCenterY(bullet.getCenterY() - bulletSpeed);

                    try {
                        if (isOutside()) {
                            removeBulletFromScreen();
                        } else {
                            for (Alien alien : gameEngine.getAlienList()
                            ) {
                                if (alien.getEnemyShip().getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                                    if (alien.isBoss()) {
                                        Boss boss = (Boss) alien;
                                        boss.hitByBullet(this.owner);
                                    } else {
                                        alien.hitByBullet();
                                    }
                                    removeBulletFromScreen();
                                }
                            }
                        }
                    } catch (Exception ignored) {
                    }
                })
        );
        bulletMove.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public boolean isOutside() {
        return bullet.getCenterY() < SceneConstants.SPACE_LIMIT;
    }

}
