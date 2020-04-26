package com.ceng453.gameClient.gameObjects.alien;

import com.ceng453.gameClient.gameObjects.GameEngine;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import static com.ceng453.gameClient.constants.GameConstants.*;

public class LevelOneAlien extends Alien {

    /**
     * This constructor sets an LevelOneAlien instance.
     * @param xPos X position of the alien
     * @param yPos Y position of the alien
     * @param gameEngine Game engine of the client
     */
    public LevelOneAlien(double xPos, double yPos, GameEngine gameEngine) {
        super(xPos, yPos, LEVEL_ONE_ALIEN_HEALTH, LEVEL_ONE_ALIEN_SCORE, LEVEL_ONE_ALIEN_BULLET_SPEED, LEVEL_ONE_ALIEN_COLOR, gameEngine);
        Image texture = new Image(LevelOneAlien.class.getResource("/pictures/astronaut.jpg").toExternalForm());
        getEnemyShip().setFill(new ImagePattern(texture));
    }
}
