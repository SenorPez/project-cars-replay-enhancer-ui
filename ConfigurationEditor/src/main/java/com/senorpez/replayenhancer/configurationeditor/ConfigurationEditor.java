package com.senorpez.replayenhancer.configurationeditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ConfigurationEditor extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ConfigurationEditor.fxml"));
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMaxWidth(stage.getWidth());
        stage.setMaxHeight(stage.getHeight());

        if (stage.getHeight() > primaryScreenBounds.getHeight()) {
            stage.setHeight(primaryScreenBounds.getHeight()*.90);
        }

        stage.setTitle("Project CARS Replay Enhancer Configuration Editor");
    }
    
}
