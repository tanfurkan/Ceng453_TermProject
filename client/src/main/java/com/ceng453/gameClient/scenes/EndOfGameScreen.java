package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.scenes.utils.Title;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static com.ceng453.gameClient.scenes.utils.ScreenTemplate.addBackground;
import static com.ceng453.gameClient.scenes.utils.ScreenTemplate.addTitle;

public class EndOfGameScreen {

    private static Pane root;

    public static Scene createContent(boolean isWin) {

        root = new Pane();

        Title userMessage = new Title(isWin ? "Congratulation" : "Oops!", 40);
        userMessage.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - userMessage.getTitleWidth() / 2.0);
        userMessage.setTranslateY(480);

        String againText = isWin ? "Play Again" : "Try Again";

        Button goToLeaderBoard = new Button("Go to Leaderboard");
        goToLeaderBoard.setMinHeight(60);
        goToLeaderBoard.setMinWidth(150);
        goToLeaderBoard.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 + 50);
        goToLeaderBoard.setTranslateY(620);

        Button againButton = new Button(againText);
        againButton.setMinHeight(60);
        againButton.setMinWidth(150);
        againButton.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - 75);
        againButton.setTranslateY(540);

        Button goToMenuButton = new Button("Go to Menu");
        goToMenuButton.setMinHeight(60);
        goToMenuButton.setMinWidth(150);
        goToMenuButton.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - 200);
        goToMenuButton.setTranslateY(620);

        againButton.setOnAction(e -> SceneConstants.stage.setScene(GameScreen.createContent()));
        goToMenuButton.setOnAction(e -> SceneConstants.stage.setScene(MainMenuScreen.createContent()));
        goToLeaderBoard.setOnAction(e -> SceneConstants.stage.setScene(LeaderboardScreen.createContent()));

        addTitle(root);
        addBackground(root);
        root.getChildren().addAll(userMessage, againButton, goToMenuButton, goToLeaderBoard);

        return new Scene(root, SceneConstants.WINDOW_WIDTH, SceneConstants.WINDOW_HEIGHT);
    }
}
