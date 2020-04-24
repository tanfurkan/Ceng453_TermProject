package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.constants.GameConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {

    private Circle spaceShip;
    private int health;
    private int score;
    private int level = 1;

    public Player(){
        spaceShip = new Circle(SceneConstants.PLAYER_SPACESHIP_INITIAL_X, SceneConstants.PLAYER_SPACESHIP_INITIAL_Y, 20, Color.AQUA);
        health = GameConstants.PLAYER_INITIAL_HEALTH;
    }

    public void updateSpaceShipPosition(double x, double y){
        /* Player can go 2/3 of width only */
        spaceShip.setCenterX(Math.min(Math.max(SceneConstants.PLAYER_X_MIN, x),SceneConstants.PLAYER_X_MAX));
        spaceShip.setCenterY(Math.min(Math.max(SceneConstants.PLAYER_Y_MIN, y),SceneConstants.PLAYER_Y_MAX));
    }


}
