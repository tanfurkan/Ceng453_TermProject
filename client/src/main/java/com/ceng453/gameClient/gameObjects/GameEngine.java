package com.ceng453.gameClient.gameObjects;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameEngine {

    private final Pane gameScreen;

    public GameEngine(Pane givenGameScreen) {
        gameScreen = givenGameScreen;
    }

    public void createFirstLevel() {

        Circle circle1 = new Circle(200.f, 100.f, 20.f, Color.WHITE);
        Circle circle2 = new Circle(100.f, 200.f, 20.f, Color.WHITE);
        gameScreen.getChildren().addAll(circle1, circle2);
    }

}
