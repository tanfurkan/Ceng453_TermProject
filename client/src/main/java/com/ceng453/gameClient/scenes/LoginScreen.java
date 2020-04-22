package com.ceng453.gameClient.scenes;

import com.ceng453.gameClient.constants.ErrorConstants;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static javafx.scene.text.TextAlignment.CENTER;

public class LoginScreen {

    private static final GridPane root = new GridPane();
    private static final LoginController loginController = new LoginController();

    public static Scene createContent() {

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        col1.setPercentWidth(20);
        col2.setPercentWidth(30);
        root.getColumnConstraints().addAll(col1, col2);

        root.setAlignment(Pos.TOP_CENTER);

        root.setHgap(10);
        root.setVgap(20);
        root.setPadding(new Insets(SceneConstants.WINDOW_HEIGHT / 4.0 - 38, 25, 25, 25));

        /*  To Get Username */
        Label userName = new Label("Username");
        userName.setFont(Font.font("Verdana", 20));
        userName.setTextFill(Color.WHITE);
        userName.setPadding(new Insets(0, 0, 0, 20));
        root.add(userName, 0, 1);

        TextField userNameTextField = new TextField();
        root.add(userNameTextField, 1, 1);

        /*  To Get Password */
        Label password = new Label("Password");
        password.setFont(Font.font("Verdana", 20));
        password.setTextFill(Color.WHITE);
        password.setPadding(new Insets(0, 0, 0, 20));
        root.add(password, 0, 2);

        PasswordField passwordField = new PasswordField();
        root.add(passwordField, 1, 2);


        Button signInButton = new Button("Sign In");
        Button signUpButton = new Button("Sign Up");

        HBox hBoxButtons = new HBox(15);
        hBoxButtons.setAlignment(Pos.BOTTOM_CENTER);
        hBoxButtons.getChildren().add(signInButton);
        hBoxButtons.getChildren().add(signUpButton);

        root.add(hBoxButtons, 1, 3);

        /* To Inform User */
        final Text errorMessage = new Text();
        errorMessage.setWrappingWidth(SceneConstants.WINDOW_WIDTH / 4.0);
        errorMessage.setFill(Color.web(SceneConstants.MENU_COLOR, 0.6));
        errorMessage.setTextAlignment(CENTER);
        root.add(errorMessage, 1, 4);

        signInButton.setOnAction(e -> {
            if (userNameTextField.getText().length() < 1 || passwordField.getText().length() < 1)
                errorMessage.setText(ErrorConstants.EMPTY_ERROR);
            else {
                String response = loginController.login(userNameTextField.getText(), passwordField.getText());
                errorMessage.setText(response);
            }
        });

        signUpButton.setOnAction(e -> SceneConstants.stage.setScene(SignUpPage.createContent()));

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
        Title title = new Title("Space Shooter"); // TODO MAKE THE NAME CONSTANT
        HBox hbBtn = new HBox(15);
        hbBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbBtn.setPadding(new Insets(0, 0, 50, 30));
        hbBtn.getChildren().add(title);
        root.getChildren().add(hbBtn);
    }

}