package com.example.chaossimulator;

import com.example.chaossimulator.objects.BallsSettings;
import com.example.chaossimulator.objects.BouncingSprite;
import com.example.chaossimulator.objects.PVector;
import com.example.chaossimulator.objects.Sprite;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class AddObjectController {

    @FXML
    private ColorPicker objectColorPicker;

    @FXML  private TextField xAccelerationField;

    @FXML private TextField xPositionField;

    @FXML private TextField xVelocityField;

    @FXML private TextField yAccelerationField;

    @FXML private TextField yPositionField;

    @FXML private TextField yVelocityField;

    BouncingSprite sprite;

    public void initialize() {
        //objectColorPicker.promptTextProperty().addListener(((observable, oldValue, newValue) -> sprite.setView(new Circle())));
        xPositionField.textProperty().addListener((observable, oldValue, newValue) -> sprite.setXLocation(Double.parseDouble(newValue)));
        yPositionField.textProperty().addListener(((observable, oldValue, newValue) -> sprite.setYLocation(Double.parseDouble(newValue))));

        xVelocityField.textProperty().addListener(((observable, oldValue, newValue) -> sprite.setXVelocity(Double.parseDouble(newValue))));
        yVelocityField.textProperty().addListener(((observable, oldValue, newValue) -> sprite.setYVelocity(Double.parseDouble(newValue))));

        xAccelerationField.textProperty().addListener(((observable, oldValue, newValue) -> sprite.setXAcceleration(Double.parseDouble(newValue))));
        yAccelerationField.textProperty().addListener(((observable, oldValue, newValue) -> sprite.setYAcceleration(Double.parseDouble(newValue))));
    }

    public BouncingSprite getNewObject() {  return sprite; }

    public void setPhysicalObject() {
        sprite = new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.GREEN), new PVector(2 / 4, 2 / 2), new PVector(4, 0), new PVector(0, 1));


    }
}
