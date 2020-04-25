package com.ceng453.gameClient.gameObjects.alien;


import com.ceng453.gameClient.gameObjects.GameEngine;

import static com.ceng453.gameClient.constants.GameConstants.*;

public class LevelOneAlien extends Alien {

    public LevelOneAlien(double xPos, double yPos, GameEngine gameEngine) {
        super(xPos, yPos, LEVEL_ONE_ALIEN_HEALTH, LEVEL_ONE_ALIEN_SCORE, LEVEL_ONE_ALIEN_BULLET_SPEED, LEVEL_ONE_ALIEN_COLOR, gameEngine);
    }
}
