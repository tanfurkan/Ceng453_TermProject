package com.ceng453.gameClient.gameObjects.alien;

import com.ceng453.gameClient.gameObjects.GameEngine;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import static com.ceng453.gameClient.constants.GameConstants.*;

public class LevelThreeAlien extends Alien {
    public LevelThreeAlien(double xPos, double yPos, GameEngine gameEngine) {
        super(xPos, yPos, LEVEL_THREE_ALIEN_HEALTH, LEVEL_THREE_ALIEN_SCORE, LEVEL_THREE_ALIEN_BULLET_SPEED, LEVEL_THREE_ALIEN_COLOR, gameEngine);
        Image texture = new Image(LevelThreeAlien.class.getResource("/pictures/astronaut.jpg").toExternalForm());
        getEnemyShip().setFill(new ImagePattern(texture));
    }
}
