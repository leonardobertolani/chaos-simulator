package com.example.chaossimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



public class SimulatorWindow extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SimulatorWindow.class.getResource("simulation-view.fxml"));
        Scene scene = new Scene(loader.load());
        SimulatorWindowController controller = loader.getController();
        stage.setTitle("Chaos Simulator!");
        stage.setScene(scene);
        stage.show();
        //controller.initializeSimulation();
        controller.onStart();
    }

    public static void main(String[] args) {
        launch();
    }
}