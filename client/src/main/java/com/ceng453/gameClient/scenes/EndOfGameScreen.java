package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.scenes.utils.Title;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import static com.ceng453.gameClient.scenes.GameScreen.makeCenteredLabelWithPadding;
import static com.ceng453.gameClient.scenes.utils.ScreenTemplate.addBackground;

public class EndOfGameScreen {

    /**
     * This method is used for creating a scene that will be shown on stage.
     * It fills the scene with the End Of Game Screen information and sets
     * background and title. If player wins the game it will show "Congratulations"
     * message. Otherwise it will show "Game Over".
     *
     * @return EndOfGame Scene
     */
    public static Scene createContent(boolean isWin, String secondUsername, String firstScore, String secondScore) {

        Pane root = new Pane();
        Label firstPlayerLabel = null, firstScoreLabel = null, secondPlayerLabel = null, secondScoreLabel = null;

        Title title = new Title("Space Shooter", 52);
        title.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - title.getTitleWidth() / 2.0);
        title.setTranslateY(SceneConstants.WINDOW_HEIGHT / 3.0 - title.getTitleHeight() / 1.5);

        Title userMessage = new Title(isWin ? "Congratulation" : "Oops!", 40);
        userMessage.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - userMessage.getTitleWidth() / 2.0);
        userMessage.setTranslateY(440);

        if (secondUsername.isEmpty()) {
            firstPlayerLabel = makeCenteredLabelWithPadding("Your Score", 490, SceneConstants.WINDOW_WIDTH);
            firstScoreLabel = makeCenteredLabelWithPadding(firstScore, 525, SceneConstants.WINDOW_WIDTH);
        } else {
            firstPlayerLabel = makeCenteredLabelWithPadding("Your Score", 490, 250);
            firstScoreLabel = makeCenteredLabelWithPadding(firstScore, 525, 250);
            firstPlayerLabel.setTranslateX(150);
            firstScoreLabel.setTranslateX(150);

            secondPlayerLabel = makeCenteredLabelWithPadding(secondUsername + "'s Score", 490, 250);
            secondScoreLabel = makeCenteredLabelWithPadding(secondScore, 525, 250);
            secondPlayerLabel.setTranslateX(400);
            secondScoreLabel.setTranslateX(400);
        }

        String againText = isWin ? "Play Again" : "Try Again";

        Button goToLeaderBoard = new Button("Go to Leaderboard");
        goToLeaderBoard.setMinHeight(60);
        goToLeaderBoard.setMinWidth(150);
        goToLeaderBoard.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 + 50);
        goToLeaderBoard.setTranslateY(660);

        Button againButton = new Button(againText);
        againButton.setMinHeight(60);
        againButton.setMinWidth(150);
        againButton.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - 75);
        againButton.setTranslateY(580);

        Button goToMenuButton = new Button("Go to Menu");
        goToMenuButton.setMinHeight(60);
        goToMenuButton.setMinWidth(150);
        goToMenuButton.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - 200);
        goToMenuButton.setTranslateY(660);

        againButton.setOnAction(e -> SceneConstants.stage.setScene(GameScreen.createContent()));
        goToMenuButton.setOnAction(e -> SceneConstants.stage.setScene(MainMenuScreen.createContent()));
        goToLeaderBoard.setOnAction(e -> MainMenuScreen.goToLeaderboard());

        addBackground(root);

        if (secondUsername.isEmpty()) {
            root.getChildren().addAll(title, userMessage, againButton, goToMenuButton, goToLeaderBoard,
                    firstPlayerLabel, firstScoreLabel);
        } else {
            root.getChildren().addAll(title, userMessage, againButton, goToMenuButton, goToLeaderBoard,
                    firstPlayerLabel, firstScoreLabel, secondPlayerLabel, secondScoreLabel);
        }

        return new Scene(root, SceneConstants.WINDOW_WIDTH, SceneConstants.WINDOW_HEIGHT);
    }
}
