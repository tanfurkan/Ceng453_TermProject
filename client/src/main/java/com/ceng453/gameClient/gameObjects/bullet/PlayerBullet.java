package com.ceng453.gameClient.gameObjects.bullet;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import com.ceng453.gameClient.gameObjects.alien.Alien;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PlayerBullet extends Bullet {

    public PlayerBullet(double xPos, double yPos, GameEngine gameEngine) {
        super(xPos, yPos, GameConstants.PLAYER_BULLET_COLOR, gameEngine);
        this.bulletSpeed = GameConstants.PLAYER_BULLET_SPEED;

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
                                    alien.hitByBullet();
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
