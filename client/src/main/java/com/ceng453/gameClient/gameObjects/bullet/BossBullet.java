package com.ceng453.gameClient.gameObjects.bullet;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import com.ceng453.gameClient.gameObjects.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BossBullet extends Bullet {

    protected double horizontalOffset;

    public BossBullet(double xPos, double yPos, double bulletSpeed, double horizontalOffset, GameEngine gameEngine, Color bulletColor) {
        super(xPos, yPos, bulletColor, gameEngine);
        this.bulletSpeed = bulletSpeed;
        this.horizontalOffset = horizontalOffset;

        setUpBulletMove();
        gameEngine.addElementToScreen(bullet);
        startMove();
    }


    @Override
    public void setUpBulletMove() {
        bulletMove = new Timeline(
                new KeyFrame(Duration.seconds(GameConstants.BOSS_BULLET_MOVE_DURATION), e -> {
                    bullet.setCenterX(bullet.getCenterX() + this.horizontalOffset);
                    bullet.setCenterY(bullet.getCenterY() + this.bulletSpeed);

                    if (isOutside()) {
                        removeBulletFromScreen();
                    } else {
                        for (Player player : gameEngine.getPlayerList()) {
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
        return SceneConstants.WINDOW_HEIGHT < bullet.getCenterY() || SceneConstants.WINDOW_WIDTH < bullet.getCenterX() || 0 > bullet.getCenterX();
    }
}
