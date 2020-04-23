package com.ceng453.gameClient;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.scenes.AuthenticationScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameClientApplication extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = AuthenticationScreen.createContent();
        stage.setTitle("Space Shooter");
        stage.setScene(scene);
        stage.show();
        SceneConstants.stage = stage;
    }
}