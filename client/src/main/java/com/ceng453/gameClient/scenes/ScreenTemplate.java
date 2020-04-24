package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
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

import java.util.Collections;
import java.util.List;

public class ScreenTemplate {

    protected static List<Pair<List<String>,Runnable>> menuData = Collections.emptyList();

    protected static Pane root = new Pane();
    protected static Line line;
    protected static VBox menuBox = new VBox();
    protected static ScrollPane scrollMenuBox = new ScrollPane(menuBox);


    protected static void addBackground() {
        ImageView imageView = new ImageView(new Image(MainMenuScreen.class.getResource("/pictures/astronaut.jpg").toExternalForm()));
        imageView.setFitWidth(SceneConstants.WINDOW_WIDTH);
        imageView.setFitHeight(SceneConstants.WINDOW_HEIGHT);

        root.getChildren().add(imageView);
    }

    protected static void addTitle() {
        Title title = new Title("Space Shooter");
        title.setTranslateX(SceneConstants.WINDOW_WIDTH / 2.0 - title.getTitleWidth() / 2.0);
        title.setTranslateY(SceneConstants.WINDOW_HEIGHT / 3.0 - title.getTitleHeight() / 1.5);

        root.getChildren().add(title);
    }

    protected static void addLine(double x, double y, double length) {
        line = new Line(x, y, x, y + length);
        line.setStrokeWidth(3);
        line.setStroke(Color.web(SceneConstants.MENU_COLOR, 0.6));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    protected static void startAnimation() {
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

    protected static void addMenu(double x, double y, boolean mainMenuflag, boolean polygonFlag) {
        menuBox.prefWidthProperty().bind(SceneConstants.stage.widthProperty().multiply(0.80));
        menuBox.prefHeightProperty().bind(SceneConstants.stage.heightProperty().multiply(0.5));
        menuData.forEach(data -> {
            MenuItem item = new MenuItem(data.getKey(), mainMenuflag, polygonFlag);
            item.setOnAction(data.getValue());
            item.setTranslateX(-600);

            Rectangle clip = new Rectangle(600, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

            menuBox.getChildren().addAll(item);
        });
        scrollMenuBox.setTranslateX(x);
        scrollMenuBox.setTranslateY(y);
        scrollMenuBox.setFitToHeight(true);
        scrollMenuBox.setFitToWidth(true);
        scrollMenuBox.setMaxHeight(SceneConstants.WINDOW_HEIGHT / 2.0);
        scrollMenuBox.setMaxWidth(SceneConstants.WINDOW_WIDTH * 0.8);
        scrollMenuBox.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");
        root.getChildren().add(scrollMenuBox);
    }
}
