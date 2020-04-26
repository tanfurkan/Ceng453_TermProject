package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.gameObjects.GameEngine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    /**
     * This method is used for creating a scene that will be shown on stage.
     * It fills the scene with the Game Screen information and sets
     * background. It also adds keyboard listener to the scene to check whether
     * player pressed cheat combo.
     *
     * @return Game Scene
     */
    public static Scene createContent() {

        root = new Pane();

        addBackground();
        addInformationTable();
        fillInformationTable();

        GameEngine gameEngine = new GameEngine(root);
        Scene scene = new Scene(root, SceneConstants.WINDOW_WIDTH, SceneConstants.WINDOW_HEIGHT);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if ((event.getCode() == KeyCode.DIGIT9 || event.getCode() == KeyCode.NUMPAD9) && event.isControlDown() && event.isShiftDown()) {
                gameEngine.killAllActivated();
            }
        });
        return scene;
    }

    /**
     * This method is used for creating information table
     * at the top of the Game Screen.
     */
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

        root.getChildren().add(infoTable);
    }

    /**
     * This method is used for adding background to this screen.
     */
    private static void addBackground() {
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * This method is used for filling the information table with
     * corresponding section titles.
     */
    private static void fillInformationTable() {
        connectedHealth = makeStyledLabel("");
        connectedLevel = makeStyledLabel("");
        connectedScore = makeStyledLabel("");
        addInfoToTable("HEALTH", 0, connectedHealth);
        addInfoToTable("LEVEL", 1, connectedLevel);
        addInfoToTable("SCORE", 2, connectedScore);
    }

    /**
     * This method is used for adding player information
     * into the information table at the top of the screen.
     */
    private static void addInfoToTable(String infoName, int column, Label connectedLabel) {

        Label informationName = makeStyledLabel(infoName);
        informationName.setUnderline(true);

        HBox hBoxInfo = createHBoxWithLabel(informationName);

        HBox hBoxConnectedLabel = createHBoxWithLabel(connectedLabel);
        hBoxConnectedLabel.setMinHeight(40);

        infoTable.add(hBoxInfo, column, 0);
        infoTable.add(hBoxConnectedLabel, column, 1);
    }

    /**
     * This method is used for creating and styling the labels.
     */
    private static Label makeStyledLabel(String labelName) {
        Label label = new Label(labelName);
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        label.setTextFill(Color.WHITE);

        return label;
    }

    /**
     * This method is used for creating Hbox that contains labels.
     */
    private static HBox createHBoxWithLabel(Label label) {
        HBox hBox = new HBox(10);
        hBox.getChildren().add(label);
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    /**
     * This method is used for binding health of player to this screen.
     */
    public static void bindHealth(SimpleIntegerProperty health) {
        connectedHealth.textProperty().bind(health.asString());
    }

    /**
     * This method is used for binding level of player to this screen.
     */
    public static void bindLevel(SimpleIntegerProperty level) {
        connectedLevel.textProperty().bind(level.asString());
    }

    /**
     * This method is used for binding score of player to this screen.
     */
    public static void bindScore(SimpleIntegerProperty score) {
        connectedScore.textProperty().bind(score.asString());
    }

}
