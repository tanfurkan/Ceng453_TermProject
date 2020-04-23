package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.NetworkConstants;
import com.ceng453.gameClient.constants.SceneConstants;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;

public class MainMenuScreen {

    private static List<Pair<String, Runnable>> menuData = Arrays.asList(
            new Pair<String, Runnable>("Play", () -> {}),
            new Pair<String, Runnable>("Leaderboard", () -> {}),
            new Pair<String, Runnable>("Log Out", () -> {
                NetworkConstants.jwtToken = null;
                SceneConstants.stage.setScene(AuthenticationScreen.createContent());;
            })
    );

    private static Pane root = null;
    private static VBox menuBox = null;
    private static Line line;

    public static Scene createContent() {

        root = new Pane();

        addBackground();
        addTitle();

        double lineX = SceneConstants.WINDOW_WIDTH / 2.0 - 100;
        double lineY = SceneConstants.WINDOW_HEIGHT / 3.0 + 100;

        addLine(lineX, lineY);
        addMenu(lineX + 5, lineY + 5);

        startAnimation();

        return new Scene(root);
    }

    private static void addBackground() {
        ImageView imageView = new ImageView(new Image(MainMenuScreen.class.getResource("/pictures/astronaut.jpg").toExternalForm()));
        imageView.setFitWidth(SceneConstants.WINDOW_WIDTH);
        imageView.setFitHeight(SceneConstants.WINDOW_HEIGHT);

        root.getChildren().add(imageView);
    }

    private static void addTitle() {
        Title title = new Title("Space Shooter");
        title.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - title.getTitleWidth() / 2.0);
        title.setTranslateY(SceneConstants.WINDOW_HEIGHT / 3.0 - title.getTitleHeight() / 1.5);

        root.getChildren().add(title);
    }

    private static void addLine(double x, double y) {
        line = new Line(x, y, x, y + 125);
        line.setStrokeWidth(3);
        line.setStroke(Color.web(SceneConstants.MENU_COLOR, 0.6));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    private static void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);
        st.setOnFinished(e -> {

            for (int i = 0; i < menuBox.getChildren().size(); i++) {
                Node n = menuBox.getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    private static void addMenu(double x, double y) {
        menuBox = new VBox(-5);
        
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
        menuData.forEach(data -> {
            MenuItem item = new MenuItem(data.getKey());
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

            menuBox.getChildren().addAll(item);
        });
        root.getChildren().add(menuBox);
    }


}
