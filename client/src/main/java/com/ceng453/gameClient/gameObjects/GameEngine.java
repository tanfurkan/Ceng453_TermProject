package com.ceng453.gameClient.gameObjects;

import com.ceng453.gameClient.scenes.GameScreen;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameEngine {

    private final Pane gameScreen;

    private Player player;

    public GameEngine(Pane givenGameScreen) {
        gameScreen = givenGameScreen;
        player = new Player();
        bindProperties();
        createFirstLevel();
    }

    public void createFirstLevel() {
        addElementToScreen(player.getSpaceShip());
    }

    public void addElementToScreen(Node node){
        gameScreen.getChildren().add(node);
    }

    private void bindProperties(){
        GameScreen.bindHealth(new SimpleIntegerProperty(player.getHealth()));
        GameScreen.bindScore(new SimpleIntegerProperty(player.getScore()));
        GameScreen.bindLevel(new SimpleIntegerProperty(player.getLevel()));
    }
}
