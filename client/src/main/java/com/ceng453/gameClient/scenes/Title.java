package com.ceng453.gameClient.scenes;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Title extends Pane {
    private final Text text;

    public Title(String name) {
        String[] nameList = name.split(" ");
        StringBuilder spread = new StringBuilder();
        for (String item : nameList) {
            for (char c : item.toCharArray()) {
                spread.append(c).append(" ");
            }
            spread.append("\n");
        }

        text = new Text(spread.toString());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("Verdana", 52));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(30, Color.BLACK));

        getChildren().addAll(text);
    }

    public double getTitleWidth() {
        return text.getLayoutBounds().getWidth();
    }

    public double getTitleHeight() {
        return text.getLayoutBounds().getHeight();
    }
}



