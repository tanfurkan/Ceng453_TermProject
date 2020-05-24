package com.ceng453.gameClient.gameObjects.bullet;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import com.ceng453.gameClient.gameObjects.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class AlienBullet extends Bullet {

    public AlienBullet(double xPos, double yPos, double bulletSpeed, GameEngine gameEngine, Color bulletColor) {
        super(xPos, yPos, bulletColor, gameEngine);
        this.bulletSpeed = bulletSpeed;

        setUpBulletMove();
        gameEngine.addElementToScreen(bullet);
        startMove();
    }


    @Override
    public void setUpBulletMove() {
        bulletMove = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.ALIEN_BULLET_MOVE_DURATION), e -> {
                    bullet.setCenterY(bullet.getCenterY() + bulletSpeed);

                    if (isOutside()) {
                        removeBulletFromScreen();
                    } else {
                        for(Player player : gameEngine.getPlayerList()) {
                            if (player.getSpaceShip().getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                                player.hitByBullet();
                                removeBulletFromScreen();
                            }
                        }
                    }
                })
        );
        bulletMove.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public boolean isOutside() {
        return SceneConstants.WINDOW_HEIGHT < bullet.getCenterY();
    }
}
