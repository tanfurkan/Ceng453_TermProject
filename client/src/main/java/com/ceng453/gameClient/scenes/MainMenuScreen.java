package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.controller.LeaderboardController;
import com.ceng453.gameClient.dao.RecordDAO;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.*;

public class MainMenuScreen extends ScreenTemplate{

    public static Scene createContent() {
        menuData = Arrays.asList(
                new Pair<List<String>, Runnable>(new ArrayList<>(Collections.singleton("Play")), () -> {}),
                new Pair<List<String>, Runnable>(new ArrayList<>(Collections.singleton("Leaderboard")), MainMenuScreen::goToLeaderboard),
                new Pair<List<String>, Runnable>(new ArrayList<>(Collections.singleton("Log Out")), MainMenuScreen::logout)
        );
        root = new Pane();
        menuBox = new VBox();
        scrollMenuBox = new ScrollPane(menuBox);

        addBackground();
        addTitle();

        double lineX = SceneConstants.WINDOW_WIDTH / 2.0 - 100;
        double lineY = SceneConstants.WINDOW_HEIGHT / 3.0 + 100;
        double length = SceneConstants.WINDOW_HEIGHT / 10.0;

        addLine(lineX, lineY, length);
        addMenu(lineX + 5, lineY + 5, true, false);

        startAnimation();

        return new Scene(root);
    }

    private static LeaderboardController leaderboardController = new LeaderboardController();

    private static void logout(){
        SceneConstants.stage.setScene(AuthenticationScreen.createContent());
    }

    private static void goToLeaderboard(){
        Optional<RecordDAO[]> records = leaderboardController.getRecords("20","all");
        SceneConstants.stage.setScene(LeaderboardScreen.getScene(records));
    }

}
