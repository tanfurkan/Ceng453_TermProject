package com.ceng453.gameClient.gameObjects.bullet;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AlienBullet extends Bullet {

    public AlienBullet(double xPos, double yPos, int bulletSpeed, GameEngine gameEngine) {
        super(xPos, yPos, GameConstants.ALIEN_BULLET_COLOR, gameEngine);
        this.bulletSpeed = bulletSpeed;

        setUpBulletMove();
        gameEngine.addElementToScreen(bullet);
        startMove();
    }


    @Override
    public void setUpBulletMove() {
        bulletMove = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.ALIEN_BULLET_DURATION), e -> {
                    bullet.setCenterY(bullet.getCenterY() + bulletSpeed);

                    if (isOutside()) removeFromScreen();
                    else {
                        if (gameEngine.getPlayer().getSpaceShip().getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                            gameEngine.stopTheGame();
                            System.out.println("BOM");
                        }
                    }
                })
        );
        bulletMove.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public boolean isOutside() {
        return SceneConstants.WINDOW_HEIGHT < bullet.getCenterX();
    }
}
