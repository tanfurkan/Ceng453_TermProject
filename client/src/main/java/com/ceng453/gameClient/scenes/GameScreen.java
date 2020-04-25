package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameScreen {
    private static Pane root = null;
    private static GridPane infoTable = null;
    private static Label connectedHealth = null;
    private static Label connectedLevel = null;
    private static Label connectedScore = null;

    public static Scene createContent() {

        root = new Pane();

        addBackground();
        addInformationTable();
        fillInformationTable();

        GameEngine gameEngine = new GameEngine(root);

        root.setOnMouseMoved(e-> gameEngine.getPlayer().updateSpaceShipPosition(e.getX(),e.getY()));

        return new Scene(root, SceneConstants.WINDOW_WIDTH, SceneConstants.WINDOW_HEIGHT);
    }

    private static void addInformationTable() {
        infoTable = new GridPane();

        ColumnConstraints healthColumn = new ColumnConstraints();
        ColumnConstraints levelColumn = new ColumnConstraints();
        ColumnConstraints scoreColumn = new ColumnConstraints();
        healthColumn.setPercentWidth(32);
        levelColumn.setPercentWidth(32);
        scoreColumn.setPercentWidth(32);

        infoTable.setHgap(20);
        infoTable.setMinWidth(SceneConstants.WINDOW_WIDTH);
        infoTable.setAlignment(Pos.CENTER);
        infoTable.getColumnConstraints().addAll(healthColumn, levelColumn, scoreColumn);

        infoTable.setPadding(new Insets(20, 0, 0, 0));

        //TODO NEXT 2 LINES WILL BE REMOVED
        infoTable.setGridLinesVisible(true);
        //infoTable.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));

        root.getChildren().add(infoTable);
    }

    private static void addBackground() {
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private static void fillInformationTable() {
        connectedHealth = makeStyledLabel("");
        connectedLevel = makeStyledLabel("");
        connectedScore = makeStyledLabel("");
        addInfoToTable("HEALTH", 0, connectedHealth);
        addInfoToTable("LEVEL", 1, connectedLevel);
        addInfoToTable("SCORE", 2, connectedScore);
    }


    private static void addInfoToTable(String infoName, int column, Label connectedLabel) {

        Label informationName = makeStyledLabel(infoName);
        informationName.setUnderline(true);

        HBox hBoxInfo = createHBoxWithLabel(informationName);

        HBox hBoxConnectedLabel = createHBoxWithLabel(connectedLabel);
        hBoxConnectedLabel.setMinHeight(40);

        infoTable.add(hBoxInfo, column, 0);
        infoTable.add(hBoxConnectedLabel, column, 1);
    }

    private static Label makeStyledLabel(String labelName) {
        Label label = new Label(labelName);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        label.setTextFill(Color.WHITE);

        return label;
    }

    private static HBox createHBoxWithLabel(Label label) {
        HBox hBox = new HBox(10);
        hBox.getChildren().add(label);
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    public static void bindHealth(SimpleIntegerProperty health){
        connectedHealth.textProperty().bind(health.asString());
    }

    public static void bindLevel(SimpleIntegerProperty level){
        connectedLevel.textProperty().bind(level.asString());
    }

    public static void bindScore(SimpleIntegerProperty score){
        connectedScore.textProperty().bind(score.asString());
    }

    public static Pane getGameScene(){
        return root;
    }

}
