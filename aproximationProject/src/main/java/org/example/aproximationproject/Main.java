package org.example.aproximationproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int X_START_COORDINATE = 1020;
    private static final int Y_START_COORDINATE = 800;
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root , X_START_COORDINATE , Y_START_COORDINATE);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stage.setScene(scene);
        stage.show();


    }
}
