package com.example.chaossimulator;


import com.example.chaossimulator.objects.BouncingSprite;
import com.example.chaossimulator.objects.PVector;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class AddObjectController {

    @FXML private ColorPicker objectColorPicker;

    @FXML private TextField xPositionField;

    @FXML private TextField xVelocityField;

    @FXML private TextField yPositionField;

    @FXML private TextField yVelocityField;

    BouncingSprite sprite;
    double xLocation;
    double yLocation;
    double xVelocity;
    double yVelocity;

    public void initialize(Canvas canvas) {

        try {
            xPositionField.textProperty().addListener((observable, oldValue, newValue) -> xLocation = Double.parseDouble(newValue));
            xPositionField.setText("500");

            yPositionField.textProperty().addListener((observable, oldValue, newValue) -> yLocation = canvas.getHeight() - Double.parseDouble(newValue));
            yPositionField.setText("300");


            xVelocityField.textProperty().addListener(((observable, oldValue, newValue) -> xVelocity = Double.parseDouble(newValue)));
            xVelocityField.setText("0");

            yVelocityField.textProperty().addListener(((observable, oldValue, newValue) -> yVelocity = -Double.parseDouble(newValue)));
            yVelocityField.setText("0");
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Inserisci solo caratteri numerici!").showAndWait();
        }
    }

    public BouncingSprite getNewObject() {

        return new BouncingSprite(objectColorPicker.getValue(), new PVector(xLocation, yLocation), new PVector(xVelocity, yVelocity));

    }

}
