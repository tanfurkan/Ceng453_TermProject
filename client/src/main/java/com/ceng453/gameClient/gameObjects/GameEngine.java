package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.gameObjects.alien.Alien;
import com.ceng453.gameClient.gameObjects.alien.LevelOneAlien;
import com.ceng453.gameClient.gameObjects.bullet.Bullet;
import com.ceng453.gameClient.scenes.GameScreen;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameEngine {

    private final Pane gameScreen;

    private Player player;
    private List<Alien> alienList;
    private List<Bullet> bulletList;


    public GameEngine(Pane givenGameScreen) {
        gameScreen = givenGameScreen;
        player = new Player(this);

        alienList = new ArrayList<>();
        bulletList = new ArrayList<>();

        bindProperties();
        startTrackingMouse();

        createFirstLevel();
    }

    public void createFirstLevel() {
        addElementToScreen(player.getSpaceShip());
        new LevelOneAlien(300, 300, this);
    }

    public void addElementToScreen(Node node) {
        gameScreen.getChildren().add(node);
    }

    public void removeElementFromScreen(Node node) {
        gameScreen.getChildren().remove(node);
    }

    public void stopTheGame() {
        for (Alien alien : alienList
        ) {
            alien.stopFire();
        }
        for (Bullet bullet : bulletList) {
            bullet.stopMove();
        }
        player.stopFire();
        stopTrackingMouse();
    }

    private void bindProperties() {
        GameScreen.bindHealth(player.getHealth());
        GameScreen.bindScore(player.getScore());
        GameScreen.bindLevel(player.getLevel());
    }

    private void startTrackingMouse(){
        gameScreen.setOnMouseMoved(e -> player.updateSpaceShipPosition(e.getX(), e.getY()));
    }

    private void stopTrackingMouse(){
        gameScreen.setOnMouseMoved(e -> {});
    }

}
