package com.example.chaossimulator;


import com.example.chaossimulator.objects.BouncingSprite;
import com.example.chaossimulator.objects.PVector;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/*
TO DO

- Metti a posto colorPicker che se non selezioni niente deve comunque prenderti il bianco
 */

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
    Color color;

    public void initialize(Canvas canvas) {
        //objectColorPicker.promptTextProperty().addListener(((observable, oldValue, newValue) -> sprite.setView(new Circle())));
        xPositionField.textProperty().addListener((observable, oldValue, newValue) -> xLocation = Double.parseDouble(newValue));
        xPositionField.setText("0");

        yPositionField.textProperty().addListener(((observable, oldValue, newValue) -> yLocation = canvas.getHeight() - Double.parseDouble(newValue)));
        yPositionField.setText("0");


        xVelocityField.textProperty().addListener(((observable, oldValue, newValue) -> xVelocity = Double.parseDouble(newValue)));
        xVelocityField.setText("0");

        yVelocityField.textProperty().addListener(((observable, oldValue, newValue) -> yVelocity = -Double.parseDouble(newValue)));
        yVelocityField.setText("0");

        objectColorPicker.setOnAction(event -> color = objectColorPicker.getValue());
        //objectColorPicker.promptTextProperty().addListener(((observable, oldValue, newValue) -> color = Color.valueOf(newValue)));
        objectColorPicker.setValue(Color.WHITE);

    }

    public BouncingSprite getNewObject() {  return new BouncingSprite(color, new PVector(xLocation, yLocation), new PVector(xVelocity, yVelocity)); }

}
