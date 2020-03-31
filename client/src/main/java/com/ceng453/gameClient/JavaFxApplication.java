package com.ceng453.gameClient;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Button buttonSuccess = new Button("Success");
        Polygon triangle = new Polygon();

        buttonSuccess.setLayoutX(165.0);
        buttonSuccess.setLayoutY(50.0);
        buttonSuccess.getOnMouseClicked();

        buttonSuccess.setOnMouseClicked((event -> {
            System.out.println("Hello World");
            triangle.setFill(Color.WHITE);
        }));

        triangle.getPoints().addAll(new Double[]{
                200.0, 100.0,
                300.0, 300.0,
                100.0, 300.0,
        });
        triangle.setFill(Color.AQUA);


        Group root = new Group(triangle, buttonSuccess);
        Scene scene = new Scene(root, 400, 400);

        scene.setOnMouseMoved(e->{
            triangle.setLayoutX(e.getSceneX() - 200);
            triangle.setLayoutY(e.getSceneY() - 200);
        });

        scene.setFill(Color.BLACK);
        stage.setTitle("Group7"); // Set the stage title
        stage.setScene(scene); // Place the scene in the stage
        stage.show(); // Display the stage
    }
}