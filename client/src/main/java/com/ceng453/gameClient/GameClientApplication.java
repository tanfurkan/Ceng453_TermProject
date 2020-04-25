package com.ceng453.gameClient;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.scenes.AuthenticationScreen;
import com.ceng453.gameClient.scenes.GameScreen;
import com.ceng453.gameClient.scenes.MainMenuScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameClientApplication extends Application {
    @Override
    public void start(Stage stage) {
        SceneConstants.stage = stage;
        Scene scene = GameScreen.createContent();
        stage.setMaxWidth(SceneConstants.WINDOW_WIDTH);
        stage.setMaxHeight(SceneConstants.WINDOW_HEIGHT);
        stage.setTitle("Space Shooter");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}