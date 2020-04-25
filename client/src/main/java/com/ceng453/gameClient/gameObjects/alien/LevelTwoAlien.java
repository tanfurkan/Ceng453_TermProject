package com.ceng453.gameClient.gameObjects.alien;

import com.ceng453.gameClient.gameObjects.GameEngine;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import static com.ceng453.gameClient.constants.GameConstants.*;

public class LevelTwoAlien extends Alien {
    public LevelTwoAlien(double xPos, double yPos, GameEngine gameEngine) {
        super(xPos, yPos, LEVEL_TWO_ALIEN_HEALTH, LEVEL_TWO_ALIEN_SCORE, LEVEL_TWO_ALIEN_BULLET_SPEED, LEVEL_TWO_ALIEN_COLOR, gameEngine);
        Image texture = new Image(LevelTwoAlien.class.getResource("/pictures/astronaut.jpg").toExternalForm());
        getEnemyShip().setFill(new ImagePattern(texture));
    }
}
