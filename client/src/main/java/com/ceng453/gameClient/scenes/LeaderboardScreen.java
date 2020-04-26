package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.controller.LeaderboardController;
import com.ceng453.gameClient.dao.RecordDAO;
import com.ceng453.gameClient.scenes.utils.ScreenTemplate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ceng453.gameClient.constants.SceneConstants.stage;

public class LeaderboardScreen extends ScreenTemplate {

    private static final LeaderboardController leaderboardController = new LeaderboardController();
    private static final double lineX = SceneConstants.WINDOW_WIDTH / 8.0;
    private static final double lineY = SceneConstants.WINDOW_HEIGHT / 3.0 + 25;
    private static final double length = SceneConstants.WINDOW_HEIGHT / 2.0;

    /**
     * This method is used for creating a scene that will be shown on stage.
     * It fills the scene with the Leaderboard Screen information and sets
     * background and title.
     * @return Leaderboard Scene
     */
    public static Scene createContent() {
        root = new Pane();
        menuBox = new VBox();
        scrollMenuBox = new ScrollPane(menuBox);

        addBackground(root);
        addTitle(root);

        addSelectionBar();

        addLine(lineX, lineY, length);
        addMenu(lineX + 5, lineY + 5, false, true);

        startAnimation();

        return new Scene(root, SceneConstants.WINDOW_WIDTH, SceneConstants.WINDOW_HEIGHT);
    }

    /**
     * This method is used for passing records to the parseRecords method
     * and create the Leaderboard Scene.
     * @return Leaderboard Scene
     */
    public static Scene getScene(RecordDAO[] records) {
        parseRecords(records);
        return createContent();
    }

    /**
     * This method is used for creating leaderboard that will hold records
     */
    private static void addSelectionBar() {
        ComboBox<String> timeRange =
                new ComboBox<>(FXCollections
                        .observableArrayList("Weekly", "Monthly", "All"));
        ComboBox<String> pageLimit =
                new ComboBox<>(FXCollections
                        .observableArrayList("20", "50", "100"));
        timeRange.setValue("All");
        pageLimit.setValue("20");

        Label timeRangeLabel = new Label("Show: ");
        timeRangeLabel.setFont(Font.font("Verdana", 20));
        timeRangeLabel.setTextFill(Color.WHITE);
        Effect blur = new BoxBlur(1, 1, 3);
        timeRangeLabel.setEffect(blur);


        Label pageLimitLabel = new Label("Page Size: ");
        pageLimitLabel.setFont(Font.font("Verdana", 20));
        pageLimitLabel.setTextFill(Color.WHITE);
        pageLimitLabel.setEffect(blur);

        EventHandler<ActionEvent> event =
                e -> {
                    Optional<RecordDAO[]> records = leaderboardController.getRecords(pageLimit.getValue(), timeRange.getValue().toLowerCase());
                    root.getChildren().remove(scrollMenuBox);
                    root.getChildren().remove(line);
                    menuBox = new VBox();
                    scrollMenuBox = new ScrollPane(menuBox);
                    records.ifPresent(LeaderboardScreen::parseRecords);
                    addLine(lineX, lineY, length);
                    addMenu(lineX + 5, lineY + 5, false, true);
                    startAnimation();
                };


        timeRange.setOnAction(event);
        pageLimit.setOnAction(event);


        Button backButton = new Button("Back");

        backButton.setOnAction(e -> stage.setScene(MainMenuScreen.createContent()));

        HBox selectionBar = new HBox(15);
        selectionBar.setAlignment(Pos.BOTTOM_CENTER);
        selectionBar.setTranslateX(SceneConstants.WINDOW_WIDTH / 4.0);
        selectionBar.setTranslateY(SceneConstants.WINDOW_HEIGHT / 3.0);
        selectionBar.getChildren().addAll(timeRangeLabel, timeRange, pageLimitLabel, pageLimit, backButton);

        root.getChildren().addAll(selectionBar);
    }

    /**
     * This method is used for parsing records to use them creating
     * corresponding MenuItem instances.
     */
    private static void parseRecords(RecordDAO[] records) {
        List<Pair<List<String>, Runnable>> tempItems = new java.util.ArrayList<>(Collections.emptyList());
        int i = 1;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss 'at' dd-MM-yyyy");
        List<String> title = Arrays.asList("Rank", "Username", "Score", "Date");
        tempItems.add(new Pair<>(title, () -> {
        }));
        for (RecordDAO record : records) {
            List<String> stringHolder = new java.util.ArrayList<>(Collections.emptyList());
            stringHolder.add(Integer.toString(i));
            stringHolder.add(record.getUsername());
            stringHolder.add(record.getScore().toString());
            stringHolder.add(dateFormatter.format(record.getDate()));
            tempItems.add(new Pair<>(stringHolder, () -> {
            }));
            i++;
        }
        menuData = tempItems;
    }


}
