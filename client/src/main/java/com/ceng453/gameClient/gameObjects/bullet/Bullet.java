package com.ceng453.gameClient.gameObjects.bullet;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public abstract class Bullet {

    protected Circle bullet;
    protected GameEngine gameEngine;
    protected Timeline bulletMove;
    protected double bulletSpeed;

    public abstract void setUpBulletMove();

    public abstract boolean isOutside();


    public Bullet(double xPos, double yPos, Color bulletColor, GameEngine gameEngine) {
        bullet = new Circle(xPos, yPos, GameConstants.BULLET_RADIUS, bulletColor);
        this.gameEngine = gameEngine;
    }

    public void removeFromScreen() {
        stopMove();
        gameEngine.removeElementFromScreen(bullet);
    }

    public void startMove() {
        bulletMove.play();
    }

    public void stopMove() {
        bulletMove.stop();
    }


}
