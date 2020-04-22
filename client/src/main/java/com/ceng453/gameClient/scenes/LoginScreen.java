package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.SceneConstants;
import com.ceng453.gameClient.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginScreen {

    private static GridPane root = new GridPane();
    private static LoginController loginController = new LoginController();

    public static Scene createContent(){

        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(25, 25, 25, 25));

        Label userName = new Label("User Name:");
        root.add(userName, 0, 1);

        TextField userTextField = new TextField();
        root.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        root.add(pw, 0, 2);

        PasswordField passwordField = new PasswordField();
        root.add(passwordField, 1, 2);

        Button signUpButton = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signUpButton);
        root.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        root.add(actiontarget, 1, 6);

        signUpButton.setOnAction(e -> {
            String response = loginController.login(userTextField.getText(), passwordField.getText());
            actiontarget.setFill(Color.web(SceneConstants.MENU_COLOR, 0.6));
            actiontarget.setText(response);
        });

        addTitle();
        addBackground();

        return new Scene(root, SceneConstants.WINDOW_WIDTH, SceneConstants.WINDOW_HEIGHT);
    }

    private static void addBackground() {
        BackgroundImage backgroundImage = new BackgroundImage(new Image(LoginScreen.class.getResource("/pictures/astronaut.jpg").toExternalForm()),
                BackgroundRepeat.ROUND,
                BackgroundRepeat.ROUND,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        root.setBackground(new Background(backgroundImage));
    }

    private static void addTitle() {
        Title title = new Title("Space Shooter");
        root.getChildren().add(title);
    }

}
