package com.ceng453.gameClient.scenes.utils;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Title extends Pane {
    private final Text text;

    /**
     * This constructor sets a Title instance.
     * This instance creates a title that will be used in
     * Leaderboard, Login and Main Menu Screen.
     * @param name name of the title
     * @param fontSize font size of the title
     */
    public Title(String name, int fontSize) {
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
        text.setFont(Font.font("Verdana", fontSize));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(30, Color.BLACK));

        getChildren().addAll(text);
    }

    /**
     * This method is used for getting the title width.
     * @return the width of the title
     */
    public double getTitleWidth() {
        return text.getLayoutBounds().getWidth();
    }

    /**
     * This method is used for getting the title height.
     * @return the height of the title
     */
    public double getTitleHeight() {
        return text.getLayoutBounds().getHeight();
    }
}



