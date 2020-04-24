package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

import java.util.List;

import static com.ceng453.gameClient.constants.SceneConstants.WINDOW_WIDTH;

public class MenuItem extends GridPane {

    public MenuItem(List<String> list, boolean mainMenuFlag, boolean polygonFlag) {

        Polygon bg;
        if (polygonFlag) {
            bg = new Polygon(
                    0, 0,
                    WINDOW_WIDTH * 0.75, 0,
                    WINDOW_WIDTH * 0.75 + 15, 15,
                    WINDOW_WIDTH * 0.75, 30,
                    0, 30
            );
        } else {
            bg = new Polygon(
                    0, 0,
                    200, 0,
                    215, 15,
                    200, 30,
                    0, 30
            );

        }
        bg.setStroke(Color.web(SceneConstants.MENU_COLOR, 0.6));
        bg.setEffect(new GaussianBlur());

        bg.fillProperty().bind(
                Bindings.when(pressedProperty())
                        .then(Color.color(0, 0, 0, 0.75))
                        .otherwise(Color.color(0, 0, 0, 0.25))
        );

        setAlignment(Pos.TOP_LEFT);

        if (mainMenuFlag) {
            Label text = createText(list.get(0));
            getChildren().addAll(bg, text);
        } else {
            ColumnConstraints col1 = new ColumnConstraints();
            ColumnConstraints col2 = new ColumnConstraints();
            ColumnConstraints col3 = new ColumnConstraints();
            ColumnConstraints col4 = new ColumnConstraints();
            col1.setPercentWidth(15);
            col2.setPercentWidth(25);
            col3.setPercentWidth(15);
            col4.setPercentWidth(30);
            getColumnConstraints().addAll(col1, col2, col3, col4);
            add(bg, 0, 0, 4, 1);
            for (int i = 0; i < 4; i++) {
                Label text = createText(list.get(i));
                add(text, i, 0);
            }
        }
    }

    public Label createText(String input) {
        Label text = new Label(input);
        text.setFont(Font.font("Verdana", 20));
        text.setTextFill(Color.WHITE);

        Effect shadow = new DropShadow(5, Color.WHITE);
        Effect blur = new BoxBlur(1, 1, 3);
        text.effectProperty().bind(
                Bindings.when(hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );
        return text;
    }

    public void setOnAction(Runnable action) {
        setOnMouseClicked(e -> action.run());
    }
}
