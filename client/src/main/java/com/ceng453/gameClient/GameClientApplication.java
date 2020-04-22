package com.ceng453.gameClient;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.scenes.LoginScreen;
import com.ceng453.gameClient.scenes.MainMenuScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameClientApplication extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = LoginScreen.createContent();
        stage.setTitle("Space Shooter");
        stage.setScene(scene);
        stage.show();
        SceneConstants.stage = stage;
    }
}