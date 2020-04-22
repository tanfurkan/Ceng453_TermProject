package com.ceng453.gameClient;

import com.ceng453.gameClient.constants.SceneConstants;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameClientApplication extends Application {
    @Override
    public void start(Stage stage) {
        SceneConstants.stage = stage;
    }
}